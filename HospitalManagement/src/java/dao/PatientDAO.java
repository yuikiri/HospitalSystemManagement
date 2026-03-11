// dao/patientdao.java
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

public class PatientDAO {
    // LEFT JOIN: Lấy cả bệnh nhân có tài khoản (userId) VÀ bệnh nhân vãng lai (userId = NULL)
    private final String SELECT_JOIN_SQL = 
        "SELECT p.*, u.email, u.avatarUrl, u.isActive " +
        "FROM Patients p " +
        "LEFT JOIN Users u ON p.userId = u.id ";

    // 1. LẤY DANH SÁCH TOÀN BỘ BỆNH NHÂN
    public List<PatientDTO> getAllPatientsForAdmin() {
        List<PatientDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY p.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY 1 BỆNH NHÂN THEO ID
    public PatientDTO getPatientById(int id) {
        String sql = SELECT_JOIN_SQL + "WHERE p.id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 3. KIỂM TRA TRÙNG SỐ ĐIỆN THOẠI (Loại trừ ID đang sửa nếu có)
    public boolean checkPhoneExist(String phone, int excludeId) {
        String sql = "SELECT id FROM Patients WHERE phone = ? AND id != ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // Hàm phụ trợ extract DTO
    private PatientDTO extractDTO(ResultSet rs) throws Exception {
        return new PatientDTO(
            rs.getInt("id"), 
            rs.getObject("userId") != null ? rs.getInt("userId") : null, 
            rs.getString("email"), rs.getString("avatarUrl"), 
            rs.getObject("isActive") != null ? rs.getInt("isActive") : null, 
            rs.getString("name"), rs.getDate("dob"),
            rs.getInt("gender"), rs.getString("phone"), rs.getString("address")
        );
    }

    // 4. THÊM BỆNH NHÂN VÃNG LAI (Không có tài khoản User)
    public boolean insertWalkInPatient(String name, java.sql.Date dob, int gender, String phone, String address) {
        String sql = "INSERT INTO Patients (name, dob, gender, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDate(2, dob);
            ps.setInt(3, gender);
            ps.setString(4, phone);
            ps.setString(5, address);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. CẬP NHẬT THÔNG TIN HÀNH CHÍNH
    public boolean updatePatientInfo(int id, String name, java.sql.Date dob, int gender, String phone, String address) {
        String sql = "UPDATE Patients SET name = ?, dob = ?, gender = ?, phone = ?, address = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDate(2, dob);
            ps.setInt(3, gender);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}