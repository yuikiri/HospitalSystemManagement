/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PaymentDAO;
import dao.PaymentDTO;
import java.util.List;
import entity.Payment;
import java.util.Arrays;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class PaymentService {
    private PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    public List<PaymentDTO> getActiveList() {
        return paymentDAO.getAllActivePayments();
    }

    public List<PaymentDTO> getListForAdmin() {
        return paymentDAO.getAllPaymentsForAdmin();
    }

    public PaymentDTO getPaymentByRecordId(int medicalRecordId) {
        return paymentDAO.getByMedicalRecordId(medicalRecordId);
    }

    // TẠO HÓA ĐƠN MỚI
    public boolean createNewPayment(int medicalRecordId, double totalAmount) {
        // Ràng buộc Tài chính 1: Số tiền không được âm
        if (totalAmount < 0) {
            return false;
        }

        // Ràng buộc Tài chính 2: UNIQUE (Một ca khám chỉ xuất 1 hóa đơn tổng)
        if (paymentDAO.getByMedicalRecordId(medicalRecordId) != null) {
            return false; // Hóa đơn cho ca này đã tồn tại!
        }

        return paymentDAO.insertPayment(medicalRecordId, totalAmount);
    }

    // THU NGÂN XÁC NHẬN NHẬN TIỀN
    public boolean processPayment(int id, String paymentMethod) {
        // Kiểm tra phương thức thanh toán hợp lệ theo Constraint trong Database
        if (!paymentMethod.equals("cash") && !paymentMethod.equals("card") && !paymentMethod.equals("banking")) {
            return false;
        }
        
        return paymentDAO.markAsPaid(id, paymentMethod);
    }

    // BẬT / TẮT (Hủy hóa đơn)
    public boolean deactivatePayment(int id) {
        return paymentDAO.togglePaymentStatus(id, 0); 
    }

    public boolean activatePayment(int id) {
        return paymentDAO.togglePaymentStatus(id, 1); 
    }
}
