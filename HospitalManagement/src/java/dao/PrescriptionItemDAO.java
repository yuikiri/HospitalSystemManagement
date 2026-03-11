/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
import entity.PrescriptionItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

public class PrescriptionItemDAO {
    // Lấy toàn bộ thuốc của MỘT ĐƠN THUỐC CỤ THỂ
    public List<PrescriptionItemDTO> getItemsByPrescriptionId(int prescriptionId) {
        List<PrescriptionItemDTO> list = new ArrayList<>();
        String sql = "SELECT pi.*, m.name AS medicineName, m.unit, m.price " +
                     "FROM PrescriptionItems pi " +
                     "JOIN Medicines m ON pi.medicineId = m.id " +
                     "WHERE pi.prescriptionId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new PrescriptionItemDTO(
                        rs.getInt("id"), rs.getInt("prescriptionId"), rs.getInt("medicineId"),
                        rs.getString("medicineName"), rs.getString("unit"), rs.getDouble("price"),
                        rs.getInt("quantity"), rs.getString("dosage"), 
                        rs.getString("frequency"), rs.getString("duration")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // THÊM 1 MÓN THUỐC VÀO ĐƠN
    public boolean insertItem(int prescriptionId, int medicineId, int quantity, String dosage, String frequency, String duration) {
        String sql = "INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            ps.setInt(2, medicineId);
            ps.setInt(3, quantity);
            ps.setString(4, dosage);
            ps.setString(5, frequency);
            ps.setString(6, duration);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // GỠ 1 MÓN THUỐC RA KHỎI ĐƠN (Xóa cứng luôn vì nó chỉ là chi tiết)
    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM PrescriptionItems WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
