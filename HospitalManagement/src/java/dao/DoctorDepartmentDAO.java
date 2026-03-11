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
public class DoctorDepartmentDAO {
    private final String SELECT_JOIN_SQL = 
        "SELECT dd.doctorId, dd.departmentId, doc.name AS doctorName, dep.name AS departmentName " +
        "FROM DoctorDepartments dd " +
        "JOIN Doctors doc ON dd.doctorId = doc.id " +
        "JOIN Departments dep ON dd.departmentId = dep.id ";

    // 1. LẤY DANH SÁCH BÁC SĨ THUỘC 1 KHOA CỤ THỂ (Dùng cho trang Chi tiết Khoa)
    public List<DoctorDepartmentDTO> getDoctorsByDepartment(int departmentId) {
        List<DoctorDepartmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE dd.departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DoctorDepartmentDTO(
                        rs.getInt("doctorId"), rs.getInt("departmentId"),
                        rs.getString("doctorName"), rs.getString("departmentName")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. LẤY DANH SÁCH KHOA MÀ 1 BÁC SĨ ĐANG CÔNG TÁC (Một bác sĩ có thể làm nhiều khoa)
    public List<DoctorDepartmentDTO> getDepartmentsByDoctor(int doctorId) {
        List<DoctorDepartmentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "WHERE dd.doctorId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DoctorDepartmentDTO(
                        rs.getInt("doctorId"), rs.getInt("departmentId"),
                        rs.getString("doctorName"), rs.getString("departmentName")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. PHÂN CÔNG BÁC SĨ VÀO KHOA
    public boolean assignDoctorToDepartment(int doctorId, int departmentId) {
        String sql = "INSERT INTO DoctorDepartments (doctorId, departmentId) VALUES (?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, departmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. GỠ BÁC SĨ KHỎI KHOA (Xóa cứng)
    public boolean removeDoctorFromDepartment(int doctorId, int departmentId) {
        String sql = "DELETE FROM DoctorDepartments WHERE doctorId = ? AND departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, departmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    // 5. KIỂM TRA XEM BÁC SĨ ĐÃ CÓ TRONG KHOA CHƯA (Tránh lỗi trùng lặp Khóa chính)
    public boolean isAssigned(int doctorId, int departmentId) {
        String sql = "SELECT 1 FROM DoctorDepartments WHERE doctorId = ? AND departmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu đã tồn tại
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
