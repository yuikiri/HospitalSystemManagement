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
public class StaffDepartmentDAO {
    // Lệnh JOIN 3 bảng: StaffDepartments + Staffs + Departments
    private final String SELECT_JOIN_SQL = 
        "SELECT sd.staffId, sd.departmentId, st.name AS staffName, dep.name AS departmentName " +
        "FROM StaffDepartments sd " +
        "JOIN Staffs st ON sd.staffId = st.id " +
        "JOIN Departments dep ON sd.departmentId = dep.id ";

    // 1. LẤY DANH SÁCH NHÂN VIÊN CỦA 1 KHOA (VD: Xem Khoa Nội có bao nhiêu Y tá)
    public List<StaffDepartmentDTO> getStaffsByDepartment(int departmentId) {
        List<StaffDepartmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE sd.departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new StaffDepartmentDTO(
                        rs.getInt("staffId"), rs.getInt("departmentId"),
                        rs.getString("staffName"), rs.getString("departmentName")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY DANH SÁCH KHOA MÀ 1 NHÂN VIÊN ĐANG LÀM VIỆC
    public List<StaffDepartmentDTO> getDepartmentsByStaff(int staffId) {
        List<StaffDepartmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE sd.staffId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new StaffDepartmentDTO(
                        rs.getInt("staffId"), rs.getInt("departmentId"),
                        rs.getString("staffName"), rs.getString("departmentName")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. PHÂN CÔNG NHÂN VIÊN VÀO KHOA
    public boolean assignStaffToDepartment(int staffId, int departmentId) {
        String sql = "INSERT INTO StaffDepartments (staffId, departmentId) VALUES (?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, departmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. GỠ NHÂN VIÊN KHỎI KHOA (Xóa cứng - DELETE)
    public boolean removeStaffFromDepartment(int staffId, int departmentId) {
        String sql = "DELETE FROM StaffDepartments WHERE staffId = ? AND departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, departmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    // 5. CHECK TRÙNG LẶP (Ngăn lỗi add 1 người 2 lần vào cùng 1 khoa)
    public boolean isAssigned(int staffId, int departmentId) {
        String sql = "SELECT 1 FROM StaffDepartments WHERE staffId = ? AND departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
