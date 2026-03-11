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
    // 1. SIÊU QUAN TRỌNG: Kiểm tra trùng lịch (Overlap Check)
    // Trả về true nếu bị trùng (Bác sĩ hoặc Phòng khám đã kẹt cứng ở khung giờ đó)
    public boolean isTimeSlotConflict(int doctorId, int roomId, Timestamp newStart, Timestamp newEnd) {
        // Logic chồng lấp: (start1 < end2) AND (end1 > start2) và Lịch hẹn đó chưa bị Hủy
        String sql = "SELECT COUNT(*) FROM Appointments " +
                     "WHERE (doctorId = ? OR roomId = ?) " +
                     "AND status != 'cancelled' " +
                     "AND (startTime < ? AND endTime > ?)";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, roomId);
            ps.setTimestamp(3, newEnd);
            ps.setTimestamp(4, newStart);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có > 0 dòng tức là bị trùng
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; // Mặc định trả về true (có lỗi) để an toàn nếu DB lỗi
    }

    // 2. Thêm lịch hẹn (Chỉ gọi khi đã pass qua check trùng)
    public boolean insertAppointment(Appointment app) {
        String sql = "INSERT INTO Appointments (patientId, doctorId, roomId, startTime, endTime, status, createdAt) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getPatientId());
            ps.setInt(2, app.getDoctorId());
            ps.setInt(3, app.getRoomId());
            ps.setTimestamp(4, app.getStartTime());
            ps.setTimestamp(5, app.getEndTime());
            ps.setString(6, app.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 3. Lấy danh sách Lịch hẹn THEO BÁC SĨ (Dành cho Dashboard của Bác sĩ)
    public List<AppointmentDTO> getAppointmentsByDoctor(int doctorId) {
        return getAppointmentsByCondition("a.doctorId = ?", doctorId);
    }

    // 4. Lấy danh sách Lịch hẹn THEO BỆNH NHÂN (Dành cho Lịch sử khám của Bệnh nhân)
    public List<AppointmentDTO> getAppointmentsByPatient(int patientId) {
        return getAppointmentsByCondition("a.patientId = ?", patientId);
    }

    // Hàm dùng chung nội bộ để tránh lặp code (DRY Principle)
    private List<AppointmentDTO> getAppointmentsByCondition(String condition, int paramValue) {
        List<AppointmentDTO> list = new ArrayList<>();
        String sql = "SELECT a.id, p.name AS pName, p.phone AS pPhone, d.name AS dName, r.roomNumber, a.startTime, a.endTime, a.status " +
                     "FROM Appointments a " +
                     "JOIN Patients p ON a.patientId = p.id " +
                     "JOIN Doctors d ON a.doctorId = d.id " +
                     "JOIN Rooms r ON a.roomId = r.id " +
                     "WHERE " + condition + " " +
                     "ORDER BY a.startTime DESC";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, paramValue);
            
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                while (rs.next()) {
                    list.add(new AppointmentDTO(
                        rs.getInt("id"),
                        rs.getString("pName"),
                        rs.getString("pPhone"),
                        rs.getString("dName"),
                        rs.getInt("roomNumber"),
                        sdf.format(rs.getTimestamp("startTime")),
                        rs.getTimestamp("endTime") != null ? sdf.format(rs.getTimestamp("endTime")) : "Chưa xác định",
                        rs.getString("status")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 5. Cập nhật trạng thái (Dùng khi Bác sĩ khám xong hoặc Lễ tân hủy lịch)
    public boolean updateStatus(int appId, String status) {
        String sql = "UPDATE Appointments SET status = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
