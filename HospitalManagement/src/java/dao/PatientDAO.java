// dao/patientdao.java
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import util.DbUtils;

public class PatientDAO {
    public PatientDTO getPatientByUserId(int userId) {
        // Dùng LEFT JOIN cực kỳ quan trọng ở đây
        String sql = "SELECT p.id AS patientId, p.userId, p.name, p.dob, p.gender, p.phone, p.address, " +
                     "u.email " +
                     "FROM Patients p " +
                     "LEFT JOIN Users u ON p.userId = u.id " +
                     "WHERE p.userId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Xử lý logic định dạng giới tính
                    String genderStr = (rs.getInt("gender") == 1) ? "Nam" : "Nữ";
                    
                    // Xử lý định dạng ngày sinh một cách an toàn
                    Date dobRaw = rs.getDate("dob");
                    String dobStr = (dobRaw != null) ? new SimpleDateFormat("dd/MM/yyyy").format(dobRaw) : "Chưa cập nhật";
                    
                    // Xử lý email (Trường hợp khách vãng lai không có email)
                    String email = rs.getString("email") != null ? rs.getString("email") : "Chưa cập nhật";

                    return new PatientDTO(
                        rs.getInt("patientId"),
                        // Lấy Object thay vì Int nguyên thủy để xử lý NULL an toàn
                        (Integer) rs.getObject("userId"), 
                        rs.getString("name"),
                        dobStr,
                        genderStr,
                        rs.getString("phone"),
                        rs.getString("address"),
                        email
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }
}