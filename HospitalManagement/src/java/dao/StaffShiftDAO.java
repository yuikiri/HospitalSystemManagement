/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.StaffShift;
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
public class StaffShiftDAO {
    // 1. PHÂN CÔNG (INSERT)
    public boolean assignStaff(int staffId, int shiftId, String role) {
        String sql = "INSERT INTO StaffShifts (staffId, shiftId, role) VALUES (?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, shiftId);
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 2. GỠ NHÂN VIÊN (DELETE)
    public boolean removeStaffFromShift(int staffId, int shiftId) {
        String sql = "DELETE FROM StaffShifts WHERE staffId = ? AND shiftId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, shiftId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 3. KIỂM TRA ĐỤNG LỊCH (Logic: (StartA < EndB) AND (EndA > StartB))
    public boolean hasTimeConflict(int staffId, int targetShiftId) {
        String sql = "SELECT 1 FROM StaffShifts ss " +
                     "JOIN Shifts s1 ON ss.shiftId = s1.id " +
                     "JOIN Shifts s2 ON s2.id = ? " +
                     "WHERE ss.staffId = ? " +
                     "AND s1.startTime < s2.endTime AND s1.endTime > s2.startTime";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetShiftId);
            ps.setInt(2, staffId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. LẤY DANH SÁCH NHÂN VIÊN TRONG 1 CA
    public List<StaffShiftDTO> getStaffsByShift(int shiftId) {
        List<StaffShiftDTO> list = new ArrayList<>();
        String sql = "SELECT ss.*, st.name, s.startTime, s.endTime, r.roomNumber " +
                     "FROM StaffShifts ss " +
                     "JOIN Staffs st ON ss.staffId = st.id " +
                     "JOIN Shifts s ON ss.shiftId = s.id " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "WHERE ss.shiftId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new StaffShiftDTO(
                        rs.getInt("staffId"), rs.getInt("shiftId"), rs.getString("role"),
                        rs.getString("name"), rs.getTimestamp("startTime"),
                        rs.getTimestamp("endTime"), rs.getInt("roomNumber")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
