/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;
/**
 *
 * @author Yuikiri
 */
public class PrescriptionDAO {
    // Kỹ thuật JOIN xuyên thấu để lấy thông tin Người bệnh và Chẩn đoán
    private final String SELECT_JOIN_SQL = 
        "SELECT pr.*, mr.diagnosis, pat.name AS patientName, doc.name AS doctorName " +
        "FROM Prescriptions pr " +
        "JOIN MedicalRecords mr ON pr.medicalRecordId = mr.id " +
        "JOIN Appointments a ON mr.appointmentId = a.id " +
        "JOIN Patients pat ON a.patientId = pat.id " +
        "JOIN Doctors doc ON a.doctorId = doc.id ";

    // 1. DÀNH CHO DƯỢC SĨ (Lấy các đơn thuốc chưa bị xóa)
    public List<PrescriptionDTO> getAllActivePrescriptions() {
        List<PrescriptionDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE pr.isActive = 1 ORDER BY pr.createdAt DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN
    public List<PrescriptionDTO> getAllPrescriptionsForAdmin() {
        List<PrescriptionDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY pr.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // LẤY ĐƠN THUỐC DỰA TRÊN BỆNH ÁN (UNIQUE)
    public PrescriptionDTO getByMedicalRecordId(int medicalRecordId) {
        String sql = SELECT_JOIN_SQL + "WHERE pr.medicalRecordId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicalRecordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private PrescriptionDTO extractDTO(ResultSet rs) throws Exception {
        return new PrescriptionDTO(
            rs.getInt("id"), rs.getInt("medicalRecordId"), 
            rs.getString("patientName"), rs.getString("doctorName"), rs.getString("diagnosis"),
            rs.getString("notes"), rs.getTimestamp("createdAt"), 
            rs.getString("status"), rs.getInt("isActive")
        );
    }

    // 3. TẠO ĐƠN THUỐC MỚI (Lưu ý: Trả về ID của đơn thuốc vừa tạo để còn nhét chi tiết viên thuốc vào)
    public int insertPrescription(int medicalRecordId, String notes) {
        String sql = "INSERT INTO Prescriptions (medicalRecordId, notes, createdAt, status, isActive) VALUES (?, ?, GETDATE(), 'active', 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, medicalRecordId);
            ps.setString(2, notes);
            
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1); // Trả về Prescription ID mới
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1; // Thất bại
    }

    // 4. ĐỔI TRẠNG THÁI (VD: Dược sĩ đã phát thuốc xong -> 'dispensed')
    public boolean updatePrescriptionStatus(int id, String status) {
        String sql = "UPDATE Prescriptions SET status = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. XÓA MỀM / KHÔI PHỤC
    public boolean togglePrescriptionActive(int id, int isActive) {
        String sql = "UPDATE Prescriptions SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
