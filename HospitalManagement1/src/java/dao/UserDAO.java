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
    public User checkLogin(String email, String password) {
        // Query khớp chính xác với tên bảng/cột trong SQL Server của bạn
        String query = "SELECT id, userName, email, passwordHash, role, isActive, createdAt "
                     + "FROM Users WHERE email = ? AND passwordHash = ? AND isActive = 1";
        
        try {
            conn = new DbUtils().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Khởi tạo đối tượng bằng Constructor toàn phần
                // Cách này giúp bạn không cần dùng Setter trong lớp User
                return new User(
                    rs.getInt("id"),
                    rs.getString("userName"),
                    rs.getString("email"),
                    rs.getString("passwordHash"),
                    rs.getString("role"),
                    rs.getInt("isActive"),
                    rs.getTimestamp("createdAt")
                );
            }
        } catch (Exception e) {
            // Log lỗi (Trong thực tế nên dùng Logger)
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
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