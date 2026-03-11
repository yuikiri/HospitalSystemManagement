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
import util.DbUtils;
import entity.Payment;
/**
 *
 * @author Yuikiri
 */
public class PaymentDAO {
    // 1. Tạo hóa đơn mới (Khi bệnh nhân vừa khám xong)
    public boolean insertPayment(Payment p) {
        String sql = "INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, p.getMedicalRecordId());
            ps.setBigDecimal(2, p.getTotalAmount());
            ps.setString(3, p.getPaymentMethod());
            ps.setString(4, p.getStatus());
            ps.setTimestamp(5, p.getPaidAt()); // Nếu 'pending' thì có thể truyền null
            
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 2. Lấy thông tin Hóa đơn theo MedicalRecordId (để Thu ngân in ra)
    public PaymentDTO getPaymentByMedicalRecordId(int medicalRecordId) {
        String sql = "SELECT pay.id, pay.medicalRecordId, pat.name AS patientName, mr.diagnosis, " +
                     "pay.totalAmount, pay.paymentMethod, pay.status, pay.paidAt " +
                     "FROM Payments pay " +
                     "JOIN MedicalRecords mr ON pay.medicalRecordId = mr.id " +
                     "JOIN Appointments a ON mr.appointmentId = a.id " +
                     "JOIN Patients pat ON a.patientId = pat.id " +
                     "WHERE pay.medicalRecordId = ?";
                     
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, medicalRecordId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp paidAt = rs.getTimestamp("paidAt");
                    String paidAtStr = (paidAt != null) ? new SimpleDateFormat("HH:mm dd/MM/yyyy").format(paidAt) : "Chưa thanh toán";
                    
                    return new PaymentDTO(
                        rs.getInt("id"),
                        rs.getInt("medicalRecordId"),
                        rs.getString("patientName"),
                        rs.getString("diagnosis"),
                        rs.getBigDecimal("totalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("status"),
                        paidAtStr
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 3. Cập nhật trạng thái "Đã thanh toán"
    public boolean markAsPaid(int paymentId, String method) {
        String sql = "UPDATE Payments SET status = 'paid', paymentMethod = ?, paidAt = GETDATE() WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, method);
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
