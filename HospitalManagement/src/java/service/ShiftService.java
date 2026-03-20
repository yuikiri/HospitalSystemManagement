package service;

import dao.ShiftDAO;
import dao.ShiftDTO;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service xử lý logic cho Ca trực
 * @author Yuikiri
 */
public class ShiftService {
    private ShiftDAO shiftDAO;

    public ShiftService() {
        this.shiftDAO = new ShiftDAO();
    }

    // =========================================================
    // PHẦN 1: CÁC HÀM QUẢN LÝ CƠ BẢN
    // =========================================================

    public List<ShiftDTO> getActiveList() {
        return shiftDAO.getAllActiveShifts();
    }

    public List<ShiftDTO> getListForAdmin() {
        return shiftDAO.getAllShiftsForAdmin();
    }

    // THÊM CA TRỰC MỚI (Dùng cho giao diện quản lý danh sách)
    public boolean createNewShift(int roomId, Timestamp startTime, Timestamp endTime, String note) {
        if (!endTime.after(startTime)) return false; 

        // Chống đụng lịch phòng
        if (shiftDAO.checkTimeConflict(roomId, startTime, endTime)) {
            return false; 
        }

        return shiftDAO.insertShiftReturnId(roomId, startTime, endTime, note) > 0;
    }

    // HỦY / KHÔI PHỤC CA TRỰC
    public boolean deactivateShift(int id) {
        return shiftDAO.toggleShiftStatus(id, 0);
    }

    public boolean activateShift(int id) {
        return shiftDAO.toggleShiftStatus(id, 1);
    }

    // =========================================================
    // PHẦN 2: XỬ LÝ MA TRẬN THỜI KHÓA BIỂU (TIMETABLE)
    // =========================================================

    // =========================================================
    // PHẦN 2: XỬ LÝ MA TRẬN THỜI KHÓA BIỂU 8 CA
    // =========================================================

    public ShiftDTO[][] getScheduleMatrix(String email, String personType, int weekOffset) {
        // Khởi tạo ma trận 8 dòng (8 Ca) x 7 cột (7 Ngày)
        ShiftDTO[][] matrix = new ShiftDTO[8][7]; 
        
        // 1. Lấy ngày đầu tuần (Thứ 2) và đầu tuần sau
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY).plusWeeks(weekOffset);
        LocalDate nextMonday = monday.plusWeeks(1);

        Timestamp startOfWeek = Timestamp.valueOf(monday.atStartOfDay());
        Timestamp endOfWeek = Timestamp.valueOf(nextMonday.atStartOfDay());

        // 2. Kéo dữ liệu từ Database
        List<ShiftDTO> rawShifts = shiftDAO.getWeeklyShiftsByEmail(email, personType, startOfWeek, endOfWeek);

        // 3. Xếp vào Ma trận
        for (ShiftDTO shift : rawShifts) {
            LocalDateTime dt = shift.getStartTime().toLocalDateTime();
            
            // Xác định CỘT (Ngày trong tuần: Thứ 2 = 0, ..., CN = 6)
            int colIndex = dt.getDayOfWeek().getValue() - 1; 
            
            // Xác định HÀNG (Dựa vào giờ bắt đầu)
            int hour = dt.getHour();
            int rowIndex = -1;
            
            // Công thức tối ưu của bạn: (hour - 6) / 2
            // Tuy nhiên, để an toàn 100% không bị văng lỗi (ArrayIndexOutOfBounds) 
            // nếu database có ai đó lưu nhầm giờ (ví dụ: ca 5h sáng), ta chặn if như sau:
            if (hour >= 6 && hour <= 20) {
                rowIndex = (hour - 6) / 2; 
            }

            // Đưa vào ma trận nếu index hợp lệ
            if (rowIndex >= 0 && rowIndex < 8 && colIndex >= 0 && colIndex < 7) {
                matrix[rowIndex][colIndex] = shift;
            }
        }
        
        return matrix;
    }

    /**
     * HÀM QUAN TRỌNG: Lưu hoặc Cập nhật ca trực từ giao diện kéo thả/click
     */
    public boolean saveOrUpdateShift(String personType, String email, int roomId, String roleNote, int dayOfWeek, int shiftNumber, int weekOffset) {
        // 1. Tính toán thời gian
        LocalDate today = LocalDate.now();
        // dayOfWeek: 2 (Thứ 2) -> plusDays(0), 3 (Thứ 3) -> plusDays(1)...
        LocalDate targetDate = today.with(DayOfWeek.MONDAY).plusWeeks(weekOffset).plusDays(dayOfWeek - 2); 
        
        int startHour = 6 + (shiftNumber - 1) * 2;
        Timestamp startTS = Timestamp.valueOf(targetDate.atTime(startHour, 0));
        Timestamp endTS = Timestamp.valueOf(targetDate.atTime(startHour + 2, 0));

        // 2. KIỂM TRA: Ca trực này đã tồn tại cho người này chưa?
        int existingShiftId = shiftDAO.getExistingShiftId(email, personType, startTS);

        if (existingShiftId > 0) {
            // TRƯỜNG HỢP UPDATE: Đã có ca trực vào giờ này -> Chỉ đổi phòng và nhiệm vụ
            // Kiểm tra xem phòng mới có bị ai khác trực chưa (ngoại trừ chính ca này)
            // (Để đơn giản, ta cứ cho update nếu dùng Dropdown lọc phòng trống đã xử lý ở giao diện)
            System.out.println(">>> DEBUG: Cập nhật ca trực ID = " + existingShiftId);
            boolean upShift = shiftDAO.updateShiftRoom(existingShiftId, roomId, "Updated via Timetable");
            boolean upAssign = shiftDAO.assignShiftToPerson(existingShiftId, email, personType, roleNote);
            return upShift && upAssign;
        } else {
            // TRƯỜNG HỢP INSERT MỚI: Chưa có ca trực
            // Kiểm tra đụng phòng trước khi tạo mới
            if (shiftDAO.checkTimeConflict(roomId, startTS, endTS)) {
                System.out.println(">>> DEBUG LỖI: Phòng " + roomId + " đã bị bận vào giờ này!");
                return false;
            }

            int newShiftId = shiftDAO.insertShiftReturnId(roomId, startTS, endTS, "Created via Timetable");
            if (newShiftId > 0) {
                return shiftDAO.assignShiftToPerson(newShiftId, email, personType, roleNote);
            }
        }
        return false;
    }
    public java.sql.Timestamp[] calculateShiftRange(int dayOfWeek, int shiftNumber, int weekOffset) {
    java.time.LocalDate today = java.time.LocalDate.now();
    // Tính ngày mục tiêu (Thứ 2 = 2, ..., Chủ Nhật = 8)
    java.time.LocalDate targetDate = today.with(java.time.DayOfWeek.MONDAY)
            .plusWeeks(weekOffset)
            .plusDays(dayOfWeek - 2);
    
    // Tính giờ bắt đầu: Ca 1 = 6h, Ca 2 = 8h...
    int startHour = 6 + (shiftNumber - 1) * 2;
    
    // Ép về đúng 00 phút, 00 giây để SQL không bị lệch milis giây
    java.time.LocalDateTime startLDT = targetDate.atTime(startHour, 0, 0, 0);
    java.time.LocalDateTime endLDT = startLDT.plusHours(2);
    
    return new java.sql.Timestamp[]{
        java.sql.Timestamp.valueOf(startLDT), 
        java.sql.Timestamp.valueOf(endLDT)
    };
}
}