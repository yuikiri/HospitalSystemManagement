// dao/staffdao.java
package dao;

import entity.Staff;
import util.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StaffDAO {
    public StaffDTO getStaffByUserId(int userId) {
        // SQL Tối ưu: Chỉ lấy những trường thực sự cần thiết để hiển thị
        String sql = "SELECT s.id AS staffId, s.userId, s.name, s.gender, s.position, s.phone, " +
                     "u.email " +
                     "FROM Staffs s " +
                     "JOIN Users u ON s.userId = u.id " +
                     "WHERE s.userId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Xử lý định dạng giới tính ngay tại đây
                    String genderStr = (rs.getInt("gender") == 1) ? "Nam" : "Nữ";
                    
                    return new StaffDTO(
                        rs.getInt("staffId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        genderStr,
                        rs.getString("position"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }
    
}