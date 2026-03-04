// dao/userdao.java
package dao;

import entity.Users;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public Users checkLogin(String email, String passwordHash) {
        String query = "SELECT * FROM Users WHERE email = ? AND passwordHash = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users u = new Users();
                    u.setId(rs.getInt("id"));
                    u.setUserName(rs.getString("userName"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("passwordHash"));
                    u.setRole(rs.getString("role"));
                    u.setIsActive(rs.getBoolean("isActive"));
                    u.setCreatedAt(rs.getTimestamp("createdAt"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}