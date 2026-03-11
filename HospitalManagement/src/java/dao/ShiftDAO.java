/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class ShiftDAO {
    public List<ShiftDTO> getAllShifts() {
        List<ShiftDTO> list = new ArrayList<>();
        // JOIN bảng Shifts với bảng Rooms để lấy số phòng
        String sql = "SELECT s.id, s.startTime, s.endTime, r.roomNumber " +
                     "FROM Shifts s " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "ORDER BY s.startTime DESC";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            // Công cụ định dạng ngày giờ chuẩn Việt Nam
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            
            while (rs.next()) {
                String startStr = sdf.format(rs.getTimestamp("startTime"));
                String endStr = sdf.format(rs.getTimestamp("endTime"));
                String roomName = "Phòng " + rs.getInt("roomNumber");
                
                list.add(new ShiftDTO(
                    rs.getInt("id"),
                    roomName,
                    startStr,
                    endStr
                ));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public ShiftDTO getShiftById(int id) {
        String sql = "SELECT s.id, s.startTime, s.endTime, r.roomNumber " +
                     "FROM Shifts s " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "WHERE s.id = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    
                    return new ShiftDTO(
                        rs.getInt("id"),
                        "Phòng " + rs.getInt("roomNumber"),
                        sdf.format(rs.getTimestamp("startTime")),
                        sdf.format(rs.getTimestamp("endTime"))
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }
}
