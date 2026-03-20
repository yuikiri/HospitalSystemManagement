/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class DoctorShiftDAO {
    // 1. PHÂN CÔNG BÁC SĨ (INSERT)
    public boolean assignDoctor(int doctorId, int shiftId, String role) {
        String sql = "INSERT INTO DoctorShifts (doctorId, shiftId, role) VALUES (?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, shiftId);
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 2. GỠ BÁC SĨ KHỎI CA (DELETE - Vì không có isActive)
    public boolean removeDoctorFromShift(int doctorId, int shiftId) {
        String sql = "DELETE FROM DoctorShifts WHERE doctorId = ? AND shiftId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, shiftId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 3. KIỂM TRA ĐỤNG LỊCH (QUAN TRỌNG)
    // Check xem bác sĩ X có đang trực ca nào khác mà thời gian đè lên ca Y không
    public boolean hasTimeConflict(int doctorId, int targetShiftId) {
        String sql = "SELECT 1 FROM DoctorShifts ds " +
                     "JOIN Shifts s1 ON ds.shiftId = s1.id " +
                     "JOIN Shifts s2 ON s2.id = ? " +
                     "WHERE ds.doctorId = ? " +
                     "AND s1.startTime < s2.endTime AND s1.endTime > s2.startTime";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetShiftId);
            ps.setInt(2, doctorId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. LẤY DANH SÁCH THEO CA TRỰC
    public List<DoctorShiftDTO> getDoctorsByShift(int shiftId) {
        List<DoctorShiftDTO> list = new ArrayList<>();
        String sql = "SELECT ds.*, d.name, s.startTime, s.endTime, r.roomNumber " +
                     "FROM DoctorShifts ds " +
                     "JOIN Doctors d ON ds.doctorId = d.id " +
                     "JOIN Shifts s ON ds.shiftId = s.id " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "WHERE ds.shiftId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DoctorShiftDTO(
                        rs.getInt("doctorId"), rs.getInt("shiftId"), rs.getString("role"),
                        rs.getString("name"), rs.getTimestamp("startTime"),
                        rs.getTimestamp("endTime"), rs.getInt("roomNumber")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    //==========================================================================
    //=========================Hoàng
    //==========================================================================
    // Lấy danh sách Ca Trực theo ID Bác sĩ (JOIN 4 bảng để gom đủ dữ liệu)
    public List<DoctorShiftDTO> getShiftsByDoctorId(int doctorId) {
        List<DoctorShiftDTO> list = new ArrayList<>();
        String sql = "SELECT ds.doctorId, ds.shiftId, ds.role, d.name AS doctorName, " +
                     "s.startTime, s.endTime, r.roomNumber, dept.name AS departmentName " +
                     "FROM DoctorShifts ds " +
                     "JOIN Doctors d ON ds.doctorId = d.id " +
                     "JOIN Shifts s ON ds.shiftId = s.id " +
                     "JOIN Rooms r ON s.roomId = r.id " +
                     "JOIN Departments dept ON r.departmentId = dept.id " +
                     "WHERE ds.doctorId = ? AND s.isActive = 1 " +
                     "ORDER BY s.startTime ASC";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DoctorShiftDTO(
                        rs.getInt("doctorId"),
                        rs.getInt("shiftId"),
                        rs.getString("role"),
                        rs.getString("doctorName"),
                        rs.getTimestamp("startTime"),
                        rs.getTimestamp("endTime"),
                        rs.getInt("roomNumber"),
                        rs.getString("departmentName")
                    ));
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }
    //========================================================================================================================
}
