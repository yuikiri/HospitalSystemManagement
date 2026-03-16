/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Appointment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class AppointmentDAO {
    // CÚ JOIN LỊCH SỬ NỐI 5 BẢNG LẠI VỚI NHAU
    private final String SELECT_JOIN_SQL = 
        "SELECT a.*, p.name AS patientName, d.name AS doctorName, " +
        "r.roomNumber, dept.name AS departmentName " +
        "FROM Appointments a " +
        "JOIN Patients p ON a.patientId = p.id " +
        "JOIN Doctors d ON a.doctorId = d.id " +
        "JOIN Rooms r ON a.roomId = r.id " +
        "JOIN Departments dept ON r.departmentId = dept.id ";

    // 1. DÀNH CHO BÁC SĨ / LỄ TÂN (Chỉ xem lịch đang hoạt động)
    public List<AppointmentDTO> getAllActiveAppointments() {
        List<AppointmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE a.isActive = 1 ORDER BY a.startTime DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN (Xem toàn bộ, kể cả lịch đã xóa mềm)
    public List<AppointmentDTO> getAllAppointmentsForAdmin() {
        List<AppointmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY a.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Hàm phụ trợ để tái sử dụng việc móc dữ liệu từ ResultSet (Cho code DAO gọn hơn)
    private AppointmentDTO extractDTO(ResultSet rs) throws Exception {
        return new AppointmentDTO(
            rs.getInt("id"), rs.getInt("patientId"), rs.getString("patientName"),
            rs.getInt("doctorId"), rs.getString("doctorName"),
            rs.getInt("roomId"), rs.getInt("roomNumber"), rs.getString("departmentName"),
            rs.getTimestamp("startTime"), rs.getTimestamp("endTime"),
            rs.getString("status"), rs.getTimestamp("createdAt"), rs.getInt("isActive")
        );
    }

    // 3. THÊM LỊCH HẸN MỚI (Mặc định status = 'pending', isActive = 1, createdAt tự sinh)
    public boolean insertAppointment(int patientId, int doctorId, int roomId, Timestamp startTime, Timestamp endTime) {
        String sql = "INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, isActive, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, 'pending', 1, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setInt(3, roomId);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. CẬP NHẬT TRẠNG THÁI QUY TRÌNH (VD: Từ 'pending' sang 'confirmed' hoặc 'completed')
    public boolean updateAppointmentStatus(int id, String newStatus) {
        String sql = "UPDATE Appointments SET status = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. XÓA MỀM / KHÔI PHỤC LỊCH HẸN (Dành riêng cho Admin dọn dẹp data rác)
    public boolean toggleAppointmentActive(int id, int isActive) {
        String sql = "UPDATE Appointments SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    // ==========================================================
    ///////////////////////hoàng
    // ==========================================================
    // TẠO ĐƠN ĐẶT LỊCH MỚI (Bệnh nhân chỉ chọn Phòng, chưa có Bác sĩ)
    public boolean insertAppointmentWithoutDoctor(int patientId, int roomId, Timestamp startTime, Timestamp endTime) {
        // Cột doctorId = NULL, status = 'pending'
        String sql = "INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, isActive, createdAt) " +
                     "VALUES (?, NULL, ?, ?, ?, 'pending', 1, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, roomId);
            ps.setTimestamp(3, startTime);
            ps.setTimestamp(4, endTime);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    public void autoCancelExpiredAppointments() {
        // Lấy danh sách ID các lịch hẹn quá 7 ngày
        String sqlFind = "SELECT id FROM Appointments WHERE status = 'pending' AND DATEDIFF(day, createdAt, GETDATE()) >= 7";
        
        // Các câu lệnh Update trạng thái và Xóa mềm (isActive = 0)
        String sqlApp = "UPDATE Appointments SET status = 'cancelled', isActive = 0 WHERE id = ?";
        String sqlMR = "UPDATE MedicalRecords SET isActive = 0 WHERE appointmentId = ?";
        String sqlPres = "UPDATE Prescriptions SET isActive = 0, status = 'cancelled' WHERE medicalRecordId IN (SELECT id FROM MedicalRecords WHERE appointmentId = ?)";

        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement psFind = conn.prepareStatement(sqlFind);
             ResultSet rs = psFind.executeQuery()) {
            
            while (rs.next()) {
                int appId = rs.getInt("id");
                
                // 1. Xóa mềm Đơn thuốc (Prescriptions) trước
                try (PreparedStatement psPres = conn.prepareStatement(sqlPres)) {
                    psPres.setInt(1, appId); psPres.executeUpdate();
                }
                // 2. Xóa mềm Hồ sơ bệnh án (MedicalRecords)
                try (PreparedStatement psMR = conn.prepareStatement(sqlMR)) {
                    psMR.setInt(1, appId); psMR.executeUpdate();
                }
                // 3. Xóa mềm & Hủy Lịch hẹn (Appointments)
                try (PreparedStatement psApp = conn.prepareStatement(sqlApp)) {
                    psApp.setInt(1, appId); psApp.executeUpdate();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ==========================================================
    // 2. TẠO ĐƠN ĐẶT LỊCH VÀ TRẢ VỀ ID (Cho Bệnh nhân)
    // ==========================================================
    public int insertAppointmentAndGetId(int patientId, int roomId, Timestamp startTime, Timestamp endTime) {
        String sql = "INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, isActive, createdAt) " +
                     "VALUES (?, NULL, ?, ?, ?, 'pending', 1, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, patientId);
            ps.setInt(2, roomId);
            ps.setTimestamp(3, startTime);
            ps.setTimestamp(4, endTime);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // Trả về ID của Lịch Hẹn
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // ==========================================================
    // 3. TẠO HỒ SƠ BỆNH ÁN RỖNG & TRẢ VỀ ID BỆNH ÁN
    // ==========================================================
    public int createEmptyMedicalRecordAndGetId(int appointmentId) {
        String sql = "INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, createdAt, isActive) " +
                     "VALUES (?, N'Chờ khám', N'', GETDATE(), 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // Trả về ID của Bệnh Án
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // ==========================================================
    // 4. TẠO ĐƠN THUỐC RỖNG DỰA VÀO ID BỆNH ÁN
    // ==========================================================
    public boolean createEmptyPrescription(int medicalRecordId) {
        String sql = "INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) " +
                     "VALUES (?, N'', GETDATE(), 'pending', 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicalRecordId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    // ==========================================================
    // 1. LẤY LỊCH SỬ KHÁM (CHIA LÀM 2 TAB)
    // ==========================================================
    public List<MedicalHistoryDTO> getHistoryByPatient(int patientId, String tabType) {
        List<MedicalHistoryDTO> list = new ArrayList<>();
        String statusCondition = tabType.equals("waiting") ? "a.status IN ('pending', 'accepted')" : "a.status IN ('completed', 'cancelled')";

        String sql = "SELECT a.id, a.startTime, d.name AS deptName, " +
                     "a.doctorId, doc.name AS docName, r.roomNumber, a.status, " +
                     "pay.totalAmount, pay.status AS paymentStatus " +
                     "FROM Appointments a " +
                     "JOIN Rooms r ON a.roomId = r.id " +
                     "JOIN Departments d ON r.departmentId = d.id " +
                     "LEFT JOIN Doctors doc ON a.doctorId = doc.id " +
                     "LEFT JOIN MedicalRecords mr ON a.id = mr.appointmentId " +
                     "LEFT JOIN Payments pay ON mr.id = pay.medicalRecordId " +
                     "WHERE a.patientId = ? AND a.isActive = 1 AND " + statusCondition + " " +
                     "ORDER BY a.startTime DESC";

        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MedicalHistoryDTO dto = new MedicalHistoryDTO();
                dto.setAppointmentId(rs.getInt("id"));
                dto.setStartTime(rs.getTimestamp("startTime"));
                dto.setDepartmentName(rs.getString("deptName"));
                
                int docId = rs.getInt("doctorId");
                if (!rs.wasNull()) {
                    dto.setDoctorId(docId);
                    dto.setDoctorName(rs.getString("docName"));
                }
                dto.setRoomNumber(rs.getInt("roomNumber"));
                dto.setStatus(rs.getString("status"));
                dto.setTotalAmount(rs.getDouble("totalAmount"));
                dto.setPaymentStatus(rs.getString("paymentStatus"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================================
    // 2. LẤY CHI TIẾT BỆNH ÁN
    // ==========================================================
    public String[] getMedicalRecordDetail(int appointmentId) {
        String[] details = new String[2];
        String sql = "SELECT diagnosis, notes FROM MedicalRecords WHERE appointmentId = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                details[0] = rs.getString("diagnosis");
                details[1] = rs.getString("notes");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return details;
    }

    // ==========================================================
    // 3. LẤY DANH SÁCH THUỐC (Dùng Constructor có tham số)
    // ==========================================================
    public List<PrescriptionItemDTO> getPrescriptionItemsByAppId(int appointmentId) {
        List<PrescriptionItemDTO> list = new ArrayList<>();
        String sql = "SELECT pi.id, pi.prescriptionId, pi.medicineId, m.name AS medName, m.unit, m.price, " +
                     "pi.quantity, pi.dosage, pi.frequency, pi.duration " +
                     "FROM PrescriptionItems pi " +
                     "JOIN Medicines m ON pi.medicineId = m.id " +
                     "JOIN Prescriptions p ON pi.prescriptionId = p.id " +
                     "JOIN MedicalRecords mr ON p.medicalRecordId = mr.id " +
                     "WHERE mr.appointmentId = ? AND pi.isActive = 1";
                     
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Đưa thẳng dữ liệu vào Constructor (Đúng thứ tự sếp đã thiết kế)
                PrescriptionItemDTO item = new PrescriptionItemDTO(
                    rs.getInt("id"),
                    rs.getInt("prescriptionId"),
                    rs.getInt("medicineId"),
                    rs.getString("medName"),
                    rs.getString("unit"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("dosage"),
                    rs.getString("frequency"),
                    rs.getString("duration")
                );
                list.add(item);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // ==========================================================
    // 4. HỦY LỊCH HẸN (Chỉ cho phép hủy khi đang Pending)
    // ==========================================================
    public boolean cancelPendingAppointment(int appointmentId, int patientId) {
        String sql = "UPDATE Appointments SET status = 'cancelled' WHERE id = ? AND patientId = ? AND status = 'pending'";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.setInt(2, patientId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    // ==========================================================
    // ==========================================================
}
