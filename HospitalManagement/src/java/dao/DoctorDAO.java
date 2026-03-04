// dao/doctordao.java
package dao;

import entity.Doctors;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorDAO {
    public Doctors getDoctorByUserId(int userId) {
        String query = "SELECT * FROM Doctors WHERE userId = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Doctors d = new Doctors();
                    d.setId(rs.getInt("id"));
                    d.setUserId(rs.getInt("userId"));
                    d.setName(rs.getString("name"));
                    d.setPosition(rs.getString("position"));
                    d.setPhone(rs.getString("phone"));
                    d.setLicenseNumber(rs.getString("licenseNumber"));
                    d.setStatus(rs.getString("status"));
                    return d;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}