/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.MedicalRecord;
/**
 *
 * @author Yuikiri
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;
public class MedicalRecordDAO {
    // Kỹ thuật JOIN để lấy Tên Bệnh nhân và Tên Bác sĩ từ Lịch hẹn
    private final String SELECT_JOIN_SQL = 
        "SELECT mr.*, p.name AS patientName, d.name AS doctorName " +
        "FROM MedicalRecords mr " +
        "JOIN Appointments a ON mr.appointmentId = a.id " +
        "JOIN Patients p ON a.patientId = p.id " +
        "JOIN Doctors d ON a.doctorId = d.id ";

    // 1. DÀNH CHO BÁC SĨ (Chỉ lấy Bệnh án Đang hoạt động)
    public List<MedicalRecordDTO> getAllActiveMedicalRecords() {
        List<MedicalRecordDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE mr.isActive = 1 ORDER BY mr.createdAt DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN (Xem toàn bộ, kể cả đã Xóa mềm)
    public List<MedicalRecordDTO> getAllMedicalRecordsForAdmin() {
        List<MedicalRecordDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY mr.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Lấy Bệnh án DỰA TRÊN LỊCH HẸN (Rất quan trọng vì 1 Lịch hẹn = 1 Bệnh án)
    public MedicalRecordDTO getByAppointmentId(int appointmentId) {
        String sql = SELECT_JOIN_SQL + "WHERE mr.appointmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private MedicalRecordDTO extractDTO(ResultSet rs) throws Exception {
        return new MedicalRecordDTO(
            rs.getInt("id"), rs.getInt("appointmentId"), 
            rs.getString("patientName"), rs.getString("doctorName"),
            rs.getString("diagnosis"), rs.getString("notes"),
            rs.getTimestamp("createdAt"), rs.getInt("isActive")
        );
    }

    // 3. TẠO BỆNH ÁN MỚI (Mặc định isActive = 1, createdAt tự lấy GETDATE())
    public boolean insertMedicalRecord(int appointmentId, String diagnosis, String notes) {
        String sql = "INSERT INTO MedicalRecords (appointmentId, diagnosis, notes, isActive, createdAt) VALUES (?, ?, ?, 1, GETDATE())";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.setString(2, diagnosis);
            ps.setString(3, notes);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. SỬA BỆNH ÁN (Chỉ bác sĩ quản lý ca khám mới nên có quyền sửa cái này)
    public boolean updateMedicalRecord(int id, String diagnosis, String notes) {
        String sql = "UPDATE MedicalRecords SET diagnosis = ?, notes = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diagnosis);
            ps.setString(2, notes);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. XÓA MỀM / KHÔI PHỤC (Dành cho Admin)
    public boolean toggleMedicalRecordStatus(int id, int isActive) {
        String sql = "UPDATE MedicalRecords SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
