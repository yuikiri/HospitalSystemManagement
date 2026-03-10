/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class RoomDAO {
    // Hàm tiện ích nội bộ để định dạng trạng thái
    private String formatStatus(String rawStatus) {
        if (rawStatus == null) return "Không xác định";
        switch (rawStatus.toLowerCase()) {
            case "available": return "Sẵn sàng";
            case "maintenance": return "Đang bảo trì";
            default: return rawStatus;
        }
    }

    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> list = new ArrayList<>();
        // Lệnh JOIN lấy thông tin từ Rooms r, Departments d, và RoomType rt
        String sql = "SELECT r.id, r.roomNumber, r.status, d.name AS deptName, rt.name AS typeName, rt.price " +
                     "FROM Rooms r " +
                     "JOIN Departments d ON r.departmentId = d.id " +
                     "JOIN RoomType rt ON r.roomType = rt.id";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            
            while (rs.next()) {
                // Định dạng tiền tệ
                String priceFormatted = formatter.format(rs.getDouble("price")) + " VNĐ";
                // Định dạng trạng thái
                String statusStr = formatStatus(rs.getString("status"));
                
                list.add(new RoomDTO(
                    rs.getInt("id"),
                    rs.getInt("roomNumber"),
                    rs.getString("deptName"),
                    rs.getString("typeName"),
                    priceFormatted,
                    statusStr
                ));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public RoomDTO getRoomById(int id) {
        String sql = "SELECT r.id, r.roomNumber, r.status, d.name AS deptName, rt.name AS typeName, rt.price " +
                     "FROM Rooms r " +
                     "JOIN Departments d ON r.departmentId = d.id " +
                     "JOIN RoomType rt ON r.roomType = rt.id " +
                     "WHERE r.id = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String priceFormatted = formatter.format(rs.getDouble("price")) + " VNĐ";
                    String statusStr = formatStatus(rs.getString("status"));
                    
                    return new RoomDTO(
                        rs.getInt("id"),
                        rs.getInt("roomNumber"),
                        rs.getString("deptName"),
                        rs.getString("typeName"),
                        priceFormatted,
                        statusStr
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }
}
