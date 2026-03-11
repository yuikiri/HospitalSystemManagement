/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import entity.RoomType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class RoomTypeDAO {
    // 1. DÀNH CHO NGƯỜI DÙNG / BỆNH NHÂN: Lấy các loại phòng ĐANG HOẠT ĐỘNG
    public List<RoomTypeDTO> getAllActiveRoomTypes() {
        List<RoomTypeDTO> list = new ArrayList<>();
        // Sắp xếp theo giá từ thấp đến cao cho người dùng dễ chọn
        String sql = "SELECT * FROM RoomType WHERE isActive = 1 ORDER BY price ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomTypeDTO(
                    rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), 
                    rs.getTimestamp("createdAt"), rs.getInt("isActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN: Lấy TOÀN BỘ (cả 0 và 1)
    public List<RoomTypeDTO> getAllRoomTypesForAdmin() {
        List<RoomTypeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomTypeDTO(
                    rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), 
                    rs.getTimestamp("createdAt"), rs.getInt("isActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. Lấy 1 Loại phòng theo ID
    public RoomTypeDTO getRoomTypeById(int id) {
        String sql = "SELECT * FROM RoomType WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RoomTypeDTO(
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), 
                        rs.getTimestamp("createdAt"), rs.getInt("isActive")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 4. KIỂM TRA TRÙNG TÊN LOẠI PHÒNG
    public boolean checkNameExist(String name) {
        String sql = "SELECT id FROM RoomType WHERE name = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // 5. THÊM MỚI LOẠI PHÒNG (Mặc định isActive = 1, createdAt SQL tự tạo)
    public boolean insertRoomType(String name, double price) {
        String sql = "INSERT INTO RoomType (name, price, isActive) VALUES (?, ?, 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. CẬP NHẬT THÔNG TIN LOẠI PHÒNG
    public boolean updateRoomType(int id, String name, double price) {
        String sql = "UPDATE RoomType SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 7. BẬT/TẮT TRẠNG THÁI (0 = Tạm ngưng, 1 = Hoạt động)
    public boolean toggleRoomTypeStatus(int id, int status) {
        String sql = "UPDATE RoomType SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
