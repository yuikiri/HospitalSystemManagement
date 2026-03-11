/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;

/**
 *
 * @author Yuikiri
 */
public class PaymentDTO {
    private final int paymentId;
    private final int medicalRecordId;
    private final String patientName;
    private final String diagnosis;    // Bệnh lý (lấy từ MedicalRecords)
    private final BigDecimal totalAmount;
    private final String paymentMethod;
    private final String status;
    private final String paidAtStr;
    //constructor

    public PaymentDTO(int paymentId, int medicalRecordId, String patientName, String diagnosis, BigDecimal totalAmount, String paymentMethod, String status, String paidAtStr) {
        this.paymentId = paymentId;
        this.medicalRecordId = medicalRecordId;
        this.patientName = patientName;
        this.diagnosis = diagnosis;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paidAtStr = paidAtStr;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public String getPaidAtStr() {
        return paidAtStr;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" + "paymentId=" + paymentId + ", medicalRecordId=" + medicalRecordId + ", patientName=" + patientName + ", diagnosis=" + diagnosis + ", totalAmount=" + totalAmount + ", paymentMethod=" + paymentMethod + ", status=" + status + ", paidAtStr=" + paidAtStr + '}';
    }
    
}
