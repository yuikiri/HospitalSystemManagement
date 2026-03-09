// dao/userdao.java
package dao;

import entity.Users;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAO {

    public int insertUser(String userName, String email, String password) {

        String sql = "INSERT INTO Users(userName,email,passwordHash,role,isActive,createdAt) VALUES (?,?,?,?,?,GETDATE())";

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, "patient");
            ps.setBoolean(5, true);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Users checkLogin(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email = ? AND passwordHash = ? AND isActive = 1";

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Users u = new Users();
                u.setId(rs.getInt("id"));
                u.setUserName(rs.getString("userName"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}