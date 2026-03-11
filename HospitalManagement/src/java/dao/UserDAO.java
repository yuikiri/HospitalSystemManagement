// dao/userdao.java
package dao;

import entity.User;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Hàm kiểm tra đăng nhập
     * Sử dụng PreparedStatement để chống SQL Injection
     */
    // 1. CHỈ ĐĂNG NHẬP BẰNG EMAIL & BẮT BUỘC ĐÚNG MẬT KHẨU
    public User checkLogin(String email, String password) {
        // Dùng AND để bắt buộc cả email và pass phải khớp
        String sql = "SELECT * FROM Users WHERE email = ? AND passwordHash = ? AND isActive = 1";
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
                        rs.getString("passwordHash"),
                        rs.getString("role").trim(), // LƯU Ý: Thêm .trim() để sửa lỗi nhảy nhầm role
                        rs.getInt("isActive"),
                        rs.getTimestamp("createdAt")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null; // Sai pass hoặc sai email sẽ trả về null
    }

    // 2. HÀM CHỐNG TRÙNG EMAIL (Dùng cho đăng ký)
    public boolean checkEmailExist(String email) {
        String sql = "SELECT id FROM Users WHERE email = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có data -> true (đã trùng)
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }
    
    // LƯU Ý: Thêm hàm này vào UserDAO.java
    public boolean registerPatient(String name, String email, String password, String phone, String address) {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psPatient = null;
        ResultSet rs = null;

        try {
            conn = new DbUtils().getConnection();
            
            // KỸ THUẬT QUAN TRỌNG: Tắt auto commit để quản lý Transaction
            conn.setAutoCommit(false); 

            // ==========================================
            // BƯỚC 1: THÊM VÀO BẢNG USERS (Mặc định role = 'patient')
            // ==========================================
            String sqlUser = "INSERT INTO Users (userName, email, passwordHash, role, isActive, createdAt) VALUES (?, ?, ?, 'patient', 1, GETDATE())";
            // Cờ Statement.RETURN_GENERATED_KEYS dùng để lấy lại cái ID mà SQL Server vừa tạo ra
            psUser = conn.prepareStatement(sqlUser, java.sql.Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, name);
            psUser.setString(2, email);
            psUser.setString(3, password); // Thực tế sau này có thể hash pass ở đây
            
            int affectedRows = psUser.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback(); // Bị lỗi thì hủy bỏ toàn bộ
                return false;
            }

            // Hứng lấy cái userId vừa được sinh ra
            int newUserId = -1;
            rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                newUserId = rs.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // ==========================================
            // BƯỚC 2: THÊM VÀO BẢNG PATIENTS
            // ==========================================
            String sqlPatient = "INSERT INTO Patients (userId, name, phone, address) VALUES (?, ?, ?, ?)";
            psPatient = conn.prepareStatement(sqlPatient);
            psPatient.setInt(1, newUserId); // Gắn ID của bảng Users sang làm khóa ngoại
            psPatient.setString(2, name);
            psPatient.setString(3, phone);
            psPatient.setString(4, address);
            
            psPatient.executeUpdate();

            // Nếu code chạy trót lọt tới đây mà không bị văng lỗi -> CHỐT DỮ LIỆU
            conn.commit();
            return true;

        } catch (Exception e) {
            // Nếu có bất kỳ lỗi gì (VD: mất mạng giữa chừng, lỗi CSDL) -> UNDO TOÀN BỘ
            try {
                if (conn != null) {
                    conn.rollback(); 
                }
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Dọn dẹp bộ nhớ
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psPatient != null) psPatient.close(); } catch (Exception e) {}
            try { if (psUser != null) psUser.close(); } catch (Exception e) {}
            try { 
                if (conn != null) { 
                    conn.setAutoCommit(true); // Bật lại trạng thái mặc định cho Connection Pool
                    conn.close(); 
                } 
            } catch (Exception e) {}
        }
        return false;
    }


    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}