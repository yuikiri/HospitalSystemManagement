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
    // Thêm 1 loại thuốc vào đơn
    public boolean insertItem(PrescriptionItem item) {
        String sql = "INSERT INTO PrescriptionItems (prescriptionId, medicineId, quantity, dosage, frequency, duration) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, item.getPrescriptionId());
            ps.setInt(2, item.getMedicineId());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getDosage());
            ps.setString(5, item.getFrequency());
            ps.setString(6, item.getDuration());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Lấy danh sách thuốc của 1 Đơn để hiển thị DTO
    public List<PrescriptionItemDTO> getItemsByPrescriptionId(int prescriptionId) {
        List<PrescriptionItemDTO> list = new ArrayList<>();
        String sql = "SELECT pi.id, m.name AS medName, m.unit, pi.quantity, pi.dosage, pi.frequency, pi.duration " +
                     "FROM PrescriptionItems pi " +
                     "JOIN Medicines m ON pi.medicineId = m.id " +
                     "WHERE pi.prescriptionId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new PrescriptionItemDTO(
                        rs.getInt("id"),
                        rs.getString("medName"),
                        rs.getString("unit"),
                        rs.getInt("quantity"),
                        rs.getString("dosage"),
                        rs.getString("frequency"),
                        rs.getString("duration")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
