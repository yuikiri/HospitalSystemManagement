/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DbUtils;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Yuikiri
 */
public class PaymentDAO {
    /// Lấy thông tin Bệnh nhân xuyên qua Bệnh án và Lịch hẹn
    private final String SELECT_JOIN_SQL = 
        "SELECT pay.*, mr.diagnosis, pat.name AS patientName " +
        "FROM Payments pay " +
        "JOIN MedicalRecords mr ON pay.medicalRecordId = mr.id " +
        "JOIN Appointments a ON mr.appointmentId = a.id " +
        "JOIN Patients pat ON a.patientId = pat.id ";

    // 1. DÀNH CHO THU NGÂN (Lấy các hóa đơn Đang hoạt động)
    public List<PaymentDTO> getAllActivePayments() {
        List<PaymentDTO> list = new ArrayList<>();
        // Ưu tiên hiện các hóa đơn 'unpaid' (chưa thanh toán) lên đầu bảng
        String sql = SELECT_JOIN_SQL + "WHERE pay.isActive = 1 ORDER BY pay.status DESC, pay.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. DÀNH CHO ADMIN QUẢN LÝ DOANH THU
    public List<PaymentDTO> getAllPaymentsForAdmin() {
        List<PaymentDTO> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY pay.id DESC";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractDTO(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // TÌM HÓA ĐƠN THEO BỆNH ÁN (Ràng buộc UNIQUE)
    public PaymentDTO getByMedicalRecordId(int medicalRecordId) {
        String sql = SELECT_JOIN_SQL + "WHERE pay.medicalRecordId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicalRecordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private PaymentDTO extractDTO(ResultSet rs) throws Exception {
        return new PaymentDTO(
            rs.getInt("id"), rs.getInt("medicalRecordId"), 
            rs.getString("patientName"), rs.getString("diagnosis"),
            rs.getDouble("totalAmount"), rs.getString("paymentMethod"), 
            rs.getString("status"), rs.getTimestamp("paidAt"), rs.getInt("isActive")
        );
    }

    // 3. TẠO HÓA ĐƠN MỚI (Mặc định 'unpaid', paidAt = NULL, isActive = 1)
    public boolean insertPayment(int medicalRecordId, double totalAmount) {
        // Lúc mới tạo chưa thu tiền nên paymentMethod có thể để NULL hoặc rỗng
        String sql = "INSERT INTO Payments (medicalRecordId, totalAmount, status, isActive) VALUES (?, ?, 'unpaid', 1)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicalRecordId);
            ps.setDouble(2, totalAmount);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 4. XÁC NHẬN ĐÃ THU TIỀN (Chuyển sang 'paid' và lưu lại thời gian đóng tiền)
    public boolean markAsPaid(int id, String paymentMethod) {
        String sql = "UPDATE Payments SET status = 'paid', paymentMethod = ?, paidAt = GETDATE() WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentMethod); // 'cash', 'card', 'banking'
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // 5. XÓA MỀM (Hủy hóa đơn do tính sai)
    public boolean togglePaymentStatus(int id, int isActive) {
        String sql = "UPDATE Payments SET isActive = ? WHERE id = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isActive);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    //======================================================
    //=====================Hoàng
    //======================================================
    // 6. XÁC NHẬN THANH TOÁN XUYÊN TỪ APPOINTMENT ID (Dùng cho QR Code & Webhook)
    public boolean markAsPaidByAppointmentId(int appointmentId, String paymentMethod) {
        String sql = "UPDATE Payments SET status = 'paid', paymentMethod = ?, paidAt = GETDATE() " +
                     "WHERE medicalRecordId IN (SELECT id FROM MedicalRecords WHERE appointmentId = ?)";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentMethod);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    // 7. LẤY SỐ TIỀN CẦN THU TỪ APPOINTMENT ID (Để đối chiếu với số tiền khách chuyển)
    public double getRequiredAmountByAppointmentId(int appointmentId) {
        String sql = "SELECT pay.totalAmount FROM Payments pay " +
                     "JOIN MedicalRecords mr ON pay.medicalRecordId = mr.id " +
                     "WHERE mr.appointmentId = ?";
        try (Connection conn = new DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("totalAmount"); // Trả về số tiền gốc cần thu
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1; // Trả về -1 nếu không tìm thấy hóa đơn
    }
    
    // 8. KIỂM TRA TRẠNG THÁI THANH TOÁN CỦA LỊCH HẸN (Dùng cho Auto-reload giao diện)
    public String getPaymentStatusByAppointmentId(int appointmentId) {
        String sql = "SELECT pay.status FROM Payments pay " +
                     "JOIN MedicalRecords mr ON pay.medicalRecordId = mr.id " +
                     "WHERE mr.appointmentId = ?";
        try (Connection conn = new util.DbUtils().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status"); // Sẽ trả về 'paid' hoặc 'unpaid'
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "unpaid"; // Mặc định nếu lỗi thì coi như chưa trả
    }
    //==================================================================================
    //==================================================================================
}
