package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 * Data Access Object for Shifts
 * @author Yuikiri
 */
public class ShiftDAO {

    // CÂU LỆNH JOIN CHUẨN ĐỂ LẤY THÔNG TIN PHÒNG VÀ KHOA
    private final String SELECT_JOIN_SQL = 
        "SELECT s.*, r.roomNumber, d.name AS departmentName " +
        "FROM Shifts s " +
        "JOIN Rooms r ON s.roomId = r.id " +
        "JOIN Departments d ON r.departmentId = d.id ";

    // 1. LẤY CA TRỰC ĐANG HOẠT ĐỘNG
    public List<ShiftDTO> getAllActiveShifts() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE s.isActive = 1 ORDER BY s.startTime ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY TOÀN BỘ CA TRỰC CHO ADMIN
    public List<ShiftDTO> getAllShiftsForAdmin() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY s.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. KIỂM TRA ĐỤNG LỊCH PHÒNG
    public boolean checkTimeConflict(int roomId, Timestamp start, Timestamp end) {
        String sql = "SELECT id FROM Shifts WHERE roomId = ? AND isActive = 1 " +
                     "AND ((startTime < ? AND endTime > ?) " +
                     "OR (startTime >= ? AND startTime < ?))";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, start);
            ps.setTimestamp(5, end);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false; 
    }

    // 4. LẤY LỊCH TRỰC TRONG TUẦN THEO EMAIL
    public List<ShiftDTO> getWeeklyShiftsByEmail(String email, String personType, Timestamp startOfWeek, Timestamp endOfWeek) {
    List<ShiftDTO> list = new ArrayList<>();
    String sql = "";
    
    // ĐÃ SỬA: JOIN bắc cầu qua bảng Doctors/Staffs để lấy đúng ID chuyên môn
    if ("doctor".equals(personType)) {
        sql = "SELECT s.id, s.roomId, r.roomNumber, dpt.name AS departmentName, s.startTime, s.endTime, s.status, s.isActive, ds.role AS note " +
              "FROM Shifts s " +
              "JOIN Rooms r ON s.roomId = r.id " +
              "JOIN Departments dpt ON r.departmentId = dpt.id " +
              "JOIN DoctorShifts ds ON s.id = ds.shiftId " +
              "JOIN Doctors doc ON ds.doctorId = doc.id " + // Cầu nối quan trọng
              "JOIN Users u ON doc.userId = u.id " +       // Khớp với Email
              "WHERE u.email = ? AND s.startTime >= ? AND s.startTime < ? AND s.isActive = 1";
    } else {
        sql = "SELECT s.id, s.roomId, r.roomNumber, dpt.name AS departmentName, s.startTime, s.endTime, s.status, s.isActive, ss.role AS note " +
              "FROM Shifts s " +
              "JOIN Rooms r ON s.roomId = r.id " +
              "JOIN Departments dpt ON r.departmentId = dpt.id " +
              "JOIN StaffShifts ss ON s.id = ss.shiftId " +
              "JOIN Staffs stf ON ss.staffid = stf.id " +   // Cầu nối quan trọng
              "JOIN Users u ON stf.userId = u.id " +       // Khớp với Email
              "WHERE u.email = ? AND s.startTime >= ? AND s.startTime < ? AND s.isActive = 1";
    }

    try (Connection conn = new util.DbUtils().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, email);
        ps.setTimestamp(2, startOfWeek);
        ps.setTimestamp(3, endOfWeek);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
    // 5. LẤY DANH SÁCH PHÒNG TRỐNG (AJAX)
   public List<RoomDTO> getAvailableRooms(Timestamp start, Timestamp end) {
    List<RoomDTO> list = new ArrayList<>();
    // Logic: Lấy những phòng KHÔNG nằm trong danh sách đang có ca trực trùng giờ
    String sql = "SELECT r.id, r.roomNumber, d.name as departmentName " +
                 "FROM Rooms r " +
                 "JOIN Departments d ON r.departmentId = d.id " +
                 "WHERE r.isActive = 1 AND r.id NOT IN (" +
                 "    SELECT roomId FROM Shifts " +
                 "    WHERE isActive = 1 " +
                 "    AND ( (startTime >= ? AND startTime < ?) " + // Bắt đầu nằm trong khoảng
                 "       OR (endTime > ? AND endTime <= ?) " +    // Kết thúc nằm trong khoảng
                 "       OR (startTime <= ? AND endTime >= ?) )" + // Bao trùm cả khoảng
                 ")";
    try (Connection conn = new DbUtils().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setTimestamp(1, start); ps.setTimestamp(2, end);
        ps.setTimestamp(3, start); ps.setTimestamp(4, end);
        ps.setTimestamp(5, start); ps.setTimestamp(6, end);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RoomDTO r = new RoomDTO();
                r.setId(rs.getInt("id"));
                r.setRoomNumber(rs.getInt("roomNumber"));
                r.setDepartmentName(rs.getString("departmentName"));
                list.add(r);
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}

    // 6. TÌM SHIFT ID ĐÃ TỒN TẠI (Sửa lại logic JOIN cho Staff)
    public int getExistingShiftId(String email, String personType, Timestamp startTime) {
        String sql = "";
        if ("doctor".equals(personType)) {
            sql = "SELECT s.id FROM Shifts s " +
                  "JOIN DoctorShifts ds ON s.id = ds.shiftId " +
                  "JOIN Users u ON ds.doctorId = u.id " +
                  "WHERE u.email = ? AND s.startTime = ? AND s.isActive = 1";
        } else {
            // FIX LỖI Ở ĐÂY: Phải JOIN đúng bảng StaffShifts và cột staffId
            sql = "SELECT s.id FROM Shifts s " +
                  "JOIN StaffShifts ss ON s.id = ss.shiftId " +
                  "JOIN Users u ON ss.staffId = u.id " +
                  "WHERE u.email = ? AND s.startTime = ? AND s.isActive = 1";
        }
        
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setTimestamp(2, startTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 7. CẬP NHẬT PHÒNG CHO SHIFT CÓ SẴN
    public boolean updateShiftRoom(int shiftId, int roomId, String note) {
        String sql = "UPDATE Shifts SET roomId = ?, note = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, note);
            ps.setInt(3, shiftId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 8. TẠO MỚI SHIFT TRẢ VỀ ID
    public int insertShiftReturnId(int roomId, Timestamp startTime, Timestamp endTime, String note) {
        String sql = "INSERT INTO Shifts (roomId, startTime, endTime, status, isActive, note) VALUES (?, ?, ?, 'scheduled', 1, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, endTime);
            ps.setString(4, note);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 9. PHÂN CÔNG (ASSIGN) CHO NGƯỜI DÙNG
   public boolean assignShiftToPerson(int shiftId, String email, String personType, String role) {
    // JOIN bảng Users với Doctors/Staffs để lấy đúng cái 'id' (cột đầu tiên trong hình bạn gửi)
    String queryId = "doctor".equals(personType) 
            ? "SELECT d.id FROM Doctors d JOIN Users u ON d.userId = u.id WHERE u.email = ?" 
            : "SELECT s.id FROM Staffs s JOIN Users u ON s.userId = u.id WHERE u.email = ?";
    
    int targetId = -1; 
    
    try (Connection conn = new util.DbUtils().getConnection()) {
        // 1. Lấy đúng cái 'id' chuyên môn
        try (PreparedStatement psId = conn.prepareStatement(queryId)) {
            psId.setString(1, email);
            try (ResultSet rs = psId.executeQuery()) {
                if (rs.next()) targetId = rs.getInt("id");
            }
        }

        if (targetId == -1) {
            System.err.println(">>> KHÔNG TÌM THẤY HỒ SƠ CHO: " + email);
            return false;
        }

        // 2. XÓA PHÂN CÔNG CŨ (Dọn đường tránh trùng Primary Key)
        String deleteSql = "doctor".equals(personType) 
                ? "DELETE FROM DoctorShifts WHERE doctorId = ? AND shiftId = ?" 
                : "DELETE FROM StaffShifts WHERE staffid = ? AND shiftId = ?";
        
        try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
            psDel.setInt(1, targetId);
            psDel.setInt(2, shiftId);
            psDel.executeUpdate();
        }

        // 3. CHÈN DỮ LIỆU MỚI (Dùng targetId chuẩn)
        String insertSql = "doctor".equals(personType) 
                ? "INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES (?, ?, ?)"
                : "INSERT INTO StaffShifts (staffid, shiftId, role) VALUES (?, ?, ?)";

        try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
            psInsert.setInt(1, targetId);
            psInsert.setInt(2, shiftId);
            psInsert.setString(3, role);
            return psInsert.executeUpdate() > 0;
        }
        
    } catch (Exception e) {
        System.err.println(">>> LỖI SQL PHÂN CÔNG: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}
    // BẬT / TẮT TRẠNG THÁI CA TRỰC (Hủy hoặc Khôi phục)
    public boolean toggleShiftStatus(int id, int isActive) {
        String sql = "UPDATE Shifts SET isActive = ? WHERE id = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }
    
}