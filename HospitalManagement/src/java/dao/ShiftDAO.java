/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class ShiftDAO {
    // CÂU LỆNH JOIN CHUẨN ĐỂ LẤY THÔNG TIN PHÒNG VÀ KHOA
    private final String SELECT_JOIN_SQL = 
        "SELECT s.*, r.roomNumber, d.name AS departmentName " +
        "FROM Shifts s " +
        "JOIN Rooms r ON s.roomId = r.id " +
        "JOIN Departments d ON r.departmentId = d.id ";

    // 1. LẤY CA TRỰC ĐANG HOẠT ĐỘNG (Dành cho Bác sĩ xem lịch)
    public List<ShiftDTO> getAllActiveShifts() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE s.isActive = 1 ORDER BY s.startTime ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY TOÀN BỘ CA TRỰC (Dành cho Admin quản lý)
    public List<ShiftDTO> getAllShiftsForAdmin() {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY s.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftDTO(
                    rs.getInt("id"), rs.getInt("roomId"), rs.getInt("roomNumber"),
                    rs.getString("departmentName"), rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"), rs.getString("status"),
                    rs.getInt("isActive"), rs.getString("note")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. KIỂM TRA ĐỤNG LỊCH (Phòng này đã có ca trực nào trong khung giờ này chưa?)
    public boolean checkTimeConflict(int roomId, Timestamp start, Timestamp end) {
        String sql = "SELECT id FROM Shifts WHERE roomId = ? AND isActive = 1 " +
                     "AND ((startTime < ? AND endTime > ?) " +
                     "OR (startTime >= ? AND startTime < ?))";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, start);
            ps.setTimestamp(5, end);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu bị trùng lịch
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true; 
    }

    // 4. THÊM CA TRỰC MỚI (Mặc định status = 'scheduled', isActive = 1)
    public boolean insertShift(int roomId, Timestamp startTime, Timestamp endTime, String note) {
        String sql = "INSERT INTO Shifts (roomId, startTime, endTime, status, isActive, note) VALUES (?, ?, ?, 'scheduled', 1, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, endTime);
            ps.setString(4, note);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. CẬP NHẬT CA TRỰC
    public boolean updateShift(int id, int roomId, Timestamp startTime, Timestamp endTime, String status, String note) {
        String sql = "UPDATE Shifts SET roomId = ?, startTime = ?, endTime = ?, status = ?, note = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, endTime);
            ps.setString(4, status);
            ps.setString(5, note);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. BẬT / TẮT TRẠNG THÁI CA TRỰC
    public boolean toggleShiftStatus(int id, int isActive) {
        String sql = "UPDATE Shifts SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
