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
    // KỸ THUẬT JOIN 3 BẢNG ĐỂ LẤY TÊN HIỂN THỊ
    //===========================================
    ///////////////Hoàng
    //===========================================
    // Câu lệnh lôi cả giá tiền (rt.price) lên
    private final String SELECT_JOIN_SQL = 
        "SELECT r.*, d.name AS departmentName, rt.name AS roomTypeName, rt.price " +
        "FROM Rooms r " +
        "JOIN Departments d ON r.departmentId = d.id " +
        "JOIN RoomType rt ON r.roomType = rt.id ";

    // 1. Dành cho trang Đặt lịch Bệnh nhân
    public List<RoomDTO> getAllActiveRoomsAvailable() {
        List<RoomDTO> list = new ArrayList<>();
        
        // 🌟 THÊM ĐIỀU KIỆN: r.status = 'available' VÀO CÂU SQL
        String sql = SELECT_JOIN_SQL + "WHERE r.isActive = 1 AND r.status = 'available' ORDER BY r.roomNumber ASC";
        
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                    rs.getInt("roomType"), rs.getString("roomTypeName"),
                    rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                    rs.getDouble("price") // Lấy giá tiền
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Dành cho Admin (ĐÃ FIX LỖI CONSTRUCTOR)
    public List<RoomDTO> getAllRoomsForAdmin() {
        List<RoomDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY r.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("id"), rs.getInt("departmentId"), rs.getString("departmentName"),
                    rs.getInt("roomType"), rs.getString("roomTypeName"),
                    rs.getInt("roomNumber"), rs.getString("status"), rs.getInt("isActive"),
                    rs.getDouble("price") // <-- ĐÃ FIX: Thêm price vào đây để hết báo lỗi đỏ
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    public int getDefaultRoomByDepartment(int departmentId) {

String sql =
"SELECT TOP 1 r.id " +
"FROM Rooms r " +
"JOIN RoomType rt ON r.roomType = rt.id " +
"WHERE r.departmentId = ? AND rt.price = 0";

try(Connection conn = new DbUtils().getConnection();
PreparedStatement ps = conn.prepareStatement(sql)){

ps.setInt(1, departmentId);

ResultSet rs = ps.executeQuery();

if(rs.next())
return rs.getInt("id");

}catch(Exception e){e.printStackTrace();}

return -1;

}
    //===========================================
    //===========================================

    

    // 3. KIỂM TRA TRÙNG SỐ PHÒNG (Tránh việc 1 Khoa có 2 phòng cùng mang số 101)
    public boolean checkRoomNumberExist(int departmentId, int roomNumber) {
        String sql = "SELECT id FROM Rooms WHERE departmentId = ? AND roomNumber = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.setInt(2, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // 4. THÊM MỚI PHÒNG (Mặc định status là 'available', isActive = 1)
    public boolean insertRoom(int departmentId, int roomType, int roomNumber) {
        String sql = "INSERT INTO Rooms (departmentId, roomType, roomNumber, status, isActive) VALUES (?, ?, ?, 'available', 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.setInt(2, roomType);
            ps.setInt(3, roomNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. CẬP NHẬT THÔNG TIN PHÒNG (Có thể dùng để đổi trạng thái 'maintenance' v.v.)
    public boolean updateRoom(int id, int departmentId, int roomType, int roomNumber, String status) {
        String sql = "UPDATE Rooms SET departmentId = ?, roomType = ?, roomNumber = ?, status = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.setInt(2, roomType);
            ps.setInt(3, roomNumber);
            ps.setString(4, status);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. BẬT/TẮT TRẠNG THÁI (Xóa mềm / Khôi phục)
    public boolean toggleRoomStatus(int id, int isActive) {
        String sql = "UPDATE Rooms SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
}
