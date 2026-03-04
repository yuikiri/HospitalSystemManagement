// dao/patientdao.java
package dao;

import entity.Patients;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {
    public Patients getPatientByUserId(int userId) {
        String query = "SELECT * FROM Patients WHERE userId = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patients p = new Patients();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("userId"));
                    p.setName(rs.getString("name"));
                    p.setDob(rs.getDate("dob"));
                    p.setGender(rs.getString("gender"));
                    p.setPhone(rs.getString("phone"));
                    p.setAddress(rs.getString("address"));
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}