// dao/userdao.java
package dao;

import entity.User;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // =========================================================
    // 1. LUỒNG ĐĂNG NHẬP & KIỂM TRA
    // =========================================================

    public User checkLogin(String email, String password) {
        // LƯU Ý: Không check isActive ở đây nữa để Service bắt lỗi 403 (Banned)
        String sql = "SELECT * FROM Users WHERE email = ? AND passwordHash = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        "", // avatarUrl
                        rs.getString("passwordHash"),
                        rs.getString("role").trim(),
                        rs.getInt("isActive"),
                        rs.getTimestamp("createdAt")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null; 
    }

    public boolean checkEmailExist(String email) {
        String sql = "SELECT id FROM Users WHERE email = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // =========================================================
    // 2. ĐĂNG KÝ BỆNH NHÂN (TỪ INDEX.JSP) - DÙNG TRANSACTION
    // =========================================================

    public boolean registerPatient(String name, String email, String password, String phone, String address) {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psPatient = null;
        ResultSet rs = null;

        try {
            conn = new DbUtils().getConnection();
            conn.setAutoCommit(false); 

            // BƯỚC 1: THÊM VÀO BẢNG USERS
            String sqlUser = "INSERT INTO Users (userName, email, passwordHash, role, isActive, createdAt) VALUES (?, ?, ?, 'patient', 1, GETDATE())";
            psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, name);
            psUser.setString(2, email);
            psUser.setString(3, password); 
            
            if (psUser.executeUpdate() == 0) {
                conn.rollback(); return false;
            }

            // Lấy userId vừa sinh ra
            int newUserId = -1;
            rs = psUser.getGeneratedKeys();
            if (rs.next()) newUserId = rs.getInt(1);
            else { conn.rollback(); return false; }

            // BƯỚC 2: THÊM VÀO BẢNG PATIENTS
            String sqlPatient = "INSERT INTO Patients (userId, name, phone, address) VALUES (?, ?, ?, ?)";
            psPatient = conn.prepareStatement(sqlPatient);
            psPatient.setInt(1, newUserId);
            psPatient.setString(2, name);
            psPatient.setString(3, phone);
            psPatient.setString(4, address);
            psPatient.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psPatient != null) psPatient.close(); } catch (Exception e) {}
            try { if (psUser != null) psUser.close(); } catch (Exception e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }

    // =========================================================
    // 3. QUYỀN ADMIN (QUẢN LÝ TÀI KHOẢN, STATUS, UPDATE)
    // =========================================================

    // Check trùng email khi Admin Cập nhật (Bỏ qua ID hiện tại)
    public boolean checkEmailExistForUpdate(String email, int excludeUserId) {
        String sql = "SELECT id FROM Users WHERE email = ? AND id != ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // Lấy toàn bộ danh sách User cho Admin
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"), rs.getString("userName"), rs.getString("email"), "", "",
                    rs.getString("role").trim(), rs.getInt("isActive"), rs.getTimestamp("createdAt")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Admin Cập nhật thông tin User
    public boolean updateUserByAdmin(int userId, String userName, String email, String role) {
        String sql = "UPDATE Users SET userName = ?, email = ?, role = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setInt(4, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Khóa / Mở khóa tài khoản (Xóa mềm - Status = 1 / 0)
    public boolean toggleUserStatus(int userId, int newStatus) {
        String sql = "UPDATE Users SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Admin thêm User mới (Kèm tạo Profile trống bên Doctor/Staff/Patient)
    public boolean addUserByAdminTransaction(String userName, String email, String password, String role) {
        Connection conn = null;
        PreparedStatement psUser = null, psProfile = null;
        ResultSet rs = null;

        try {
            conn = new DbUtils().getConnection();
            conn.setAutoCommit(false); 

            // 1. Tạo User
            String sqlUser = "INSERT INTO Users (userName, email, passwordHash, role, isActive, createdAt) VALUES (?, ?, ?, ?, 1, GETDATE())";
            psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, userName);
            psUser.setString(2, email);
            psUser.setString(3, password);
            psUser.setString(4, role);
            psUser.executeUpdate();

            rs = psUser.getGeneratedKeys();
            int newUserId = -1;
            if (rs.next()) newUserId = rs.getInt(1);
            else { conn.rollback(); return false; }

            // 2. Tạo Profile
            String sqlProfile = "";
            if (role.equalsIgnoreCase("doctor")) {
                sqlProfile = "INSERT INTO Doctors (userId, name, position, licenseNumber) VALUES (?, ?, 'Chưa cập nhật', 'Chưa cập nhật')";
            } else if (role.equalsIgnoreCase("staff")) {
                sqlProfile = "INSERT INTO Staffs (userId, name, position) VALUES (?, ?, 'Chưa cập nhật')";
            } else if (role.equalsIgnoreCase("patient")) {
                sqlProfile = "INSERT INTO Patients (userId, name) VALUES (?, ?)";
            }

            if (!sqlProfile.isEmpty()) {
                psProfile = conn.prepareStatement(sqlProfile);
                psProfile.setInt(1, newUserId);
                psProfile.setString(2, userName);
                psProfile.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psProfile != null) psProfile.close(); } catch (Exception e) {}
            try { if (psUser != null) psUser.close(); } catch (Exception e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }
}