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
public class MedicineDAO {
    // 1. DÀNH CHO BÁC SĨ (Chỉ hiện thuốc đang được lưu hành để kê đơn)
    public List<MedicineDTO> getAllActiveMedicines() {
        List<MedicineDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicines WHERE isActive = 1 ORDER BY name ASC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO THỦ KHO / ADMIN (Xem toàn bộ, kể cả thuốc đã ngưng bán)
    public List<MedicineDTO> getAllMedicinesForAdmin() {
        List<MedicineDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicines ORDER BY id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public MedicineDTO getMedicineById(int id) {
        String sql = "SELECT * FROM Medicines WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private MedicineDTO extractDTO(ResultSet rs) throws Exception {
        return new MedicineDTO(
            rs.getInt("id"), rs.getString("name"), rs.getString("unit"), 
            rs.getDouble("price"), rs.getInt("stockquantity"), 
            rs.getString("description"), rs.getInt("isActive")
        );
    }

    // 3. THÊM THUỐC MỚI VÀO KHO
    public boolean insertMedicine(String name, String unit, double price, int stockQuantity, String description) {
        String sql = "INSERT INTO Medicines (name, unit, price, stockquantity, description, isActive) VALUES (?, ?, ?, ?, ?, 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setDouble(3, price);
            ps.setInt(4, stockQuantity);
            ps.setString(5, description);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. SỬA THÔNG TIN THUỐC (Giá, Đơn vị tính...)
    public boolean updateMedicine(int id, String name, String unit, double price, String description) {
        String sql = "UPDATE Medicines SET name = ?, unit = ?, price = ?, description = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setDouble(3, price);
            ps.setString(4, description);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. CẬP NHẬT TRỰC TIẾP SỐ LƯỢNG TỒN KHO (Tăng hoặc Giảm)
    // Nghiệp vụ: Khi phát thuốc thì truyền số Âm (-), khi nhập hàng thì truyền số Dương (+)
    public boolean updateStock(int id, int quantityChange) {
        String sql = "UPDATE Medicines SET stockquantity = stockquantity + ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityChange);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 6. XÓA MỀM / KHÔI PHỤC (Đình chỉ lưu hành thuốc)
    public boolean toggleMedicineStatus(int id, int isActive) {
        String sql = "UPDATE Medicines SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
