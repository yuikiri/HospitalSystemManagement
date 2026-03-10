package dao;

import entity.Doctor;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import entity.User;
import java.sql.SQLException;

public class DoctorDAO {
    public DoctorDTO getDoctorByUserId(int userId) {
        // SQL đã được làm sạch: Bỏ u.isActive đi vì không dùng đến nữa
        String sql = "SELECT d.id AS docId, d.userId, d.name, d.gender, d.position, d.phone, d.licenseNumber, " +
                     "u.email " +
                     "FROM Doctors d " +
                     "JOIN Users u ON d.userId = u.id " +
                     "WHERE d.userId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Xử lý logic định dạng trực tiếp khi lấy lên
                    String genderStr = (rs.getInt("gender") == 1) ? "Nam" : "Nữ";
                    
                    // Khởi tạo DTO khớp 100% với constructor mới (không còn isActive)
                    return new DoctorDTO(
                        rs.getInt("docId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        genderStr,
                        rs.getString("position"),
                        rs.getString("phone"),
                        rs.getString("licenseNumber")
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
            // Có thể quăng RuntimeException hoặc log lỗi tùy kiến trúc chung
        }
        return null;
    }
}