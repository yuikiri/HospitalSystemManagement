/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import entity.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class DepartmentDAO {
    // 1. DÀNH CHO BỆNH NHÂN ĐẶT LỊCH: Chỉ lấy Khoa đang hoạt động
    public List<DepartmentDTO> getAllActiveDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Departments WHERE isActive = 1 ORDER BY name ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(
                    rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("isActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN DASHBOARD: Lấy TOÀN BỘ (cả 0 và 1)
    public List<DepartmentDTO> getAllDepartmentsForAdmin() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Departments ORDER BY id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DepartmentDTO(
                    rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("isActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. Lấy 1 Khoa theo ID (Để đổ dữ liệu lên form sửa)
    public DepartmentDTO getDepartmentById(int id) {
        String sql = "SELECT * FROM Departments WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DepartmentDTO(
                        rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("isActive")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 4. KIỂM TRA TRÙNG TÊN KHOA (Rất quan trọng vì cột name là UNIQUE)
    public boolean checkNameExist(String name) {
        String sql = "SELECT id FROM Departments WHERE name = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return true;
    }

    // 5. THÊM MỚI KHOA (Mặc định isActive = 1)
    public boolean insertDepartment(String name, String description) {
        String sql = "INSERT INTO Departments (name, description, isActive) VALUES (?, ?, 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. CẬP NHẬT THÔNG TIN KHOA (Không cập nhật isActive ở đây)
    public boolean updateDepartment(int id, String name, String description) {
        String sql = "UPDATE Departments SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 7. BẬT/TẮT TRẠNG THÁI (0 = Khóa, 1 = Mở)
    public boolean toggleDepartmentStatus(int id, int status) {
        String sql = "UPDATE Departments SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    //==================================
    ///////////////////hoàng
    //==================================
    public List<DepartmentDTO> getClinicalDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Departments WHERE isActive = 1 AND name LIKE N'Khoa%'";
        
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DepartmentDTO dept = new DepartmentDTO(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("description"), 
                    rs.getInt("isActive")
                );
                list.add(dept);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================================
    // 2. DÀNH CHO NHÂN VIÊN (Lấy các phòng ban KHÔNG có chữ "Khoa")
    // ==========================================================
    public List<DepartmentDTO> getStaffDepartments() {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Departments WHERE isActive = 1 AND name NOT LIKE N'Khoa%'";
        
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DepartmentDTO dept = new DepartmentDTO(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("description"), 
                    rs.getInt("isActive")
                );
                list.add(dept);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    
    //=============================================
    //=============================================
}
