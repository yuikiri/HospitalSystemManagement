// dao/staffdao.java
package dao;

import entity.Staffs;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StaffDAO {
    public Staffs getStaffByUserId(int userId) {
        String query = "SELECT * FROM Staffs WHERE userId = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Staffs s = new Staffs();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("userId"));
                    s.setName(rs.getString("name"));
                    s.setPosition(rs.getString("position"));
                    s.setPhone(rs.getString("phone"));
                    s.setStatus(rs.getString("status"));
                    return s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}