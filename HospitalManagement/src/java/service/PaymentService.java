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
    private final PaymentDAO paymentDAO = new PaymentDAO();
    
    // Danh sách các phương thức thanh toán hợp lệ (Khớp với CHECK constraint trong SQL)
    private static final List<String> VALID_METHODS = Arrays.asList("cash", "card", "banking");

    // Tạo hóa đơn mới
    public boolean createPayment(Payment p) throws ErrorMessages.AppException {
        try {
            // Kiểm tra phương thức thanh toán (Nếu có truyền vào)
            if (p.getPaymentMethod() != null && !VALID_METHODS.contains(p.getPaymentMethod())) {
                throw new ErrorMessages.AppException(ErrorMessages.INVALID_PAYMENT_METHOD);
            }

            boolean isSuccess = paymentDAO.insertPayment(p);
            if (!isSuccess) {
                // Vi phạm UNIQUE medicalRecordId
                throw new ErrorMessages.AppException(ErrorMessages.PAYMENT_EXISTED);
            }
            return true;
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Thu ngân xác nhận bệnh nhân đã trả tiền
    public boolean processPayment(int paymentId, String paymentMethod) throws ErrorMessages.AppException {
        try {
            if (!VALID_METHODS.contains(paymentMethod)) {
                throw new ErrorMessages.AppException(ErrorMessages.INVALID_PAYMENT_METHOD);
            }
            
            return paymentDAO.markAsPaid(paymentId, paymentMethod);
            
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
    
    // Lấy hóa đơn cho bệnh nhân xem
    public PaymentDTO getInvoice(int medicalRecordId) throws ErrorMessages.AppException {
        try {
            PaymentDTO invoice = paymentDAO.getPaymentByMedicalRecordId(medicalRecordId);
            if (invoice == null) {
                throw new ErrorMessages.AppException(ErrorMessages.PAYMENT_NOT_FOUND);
            }
            return invoice;
        } catch (ErrorMessages.AppException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
