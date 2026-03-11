/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class PaymentDTO {
    private int id;
    private int medicalRecordId;
    
    private String patientName; // MỚI: Tên khách hàng cần thu tiền (Lấy qua JOIN)
    private String diagnosis;   // MỚI: Chẩn đoán (Để in lên biên lai cho rõ ràng)
    
    private double totalAmount;
    private String paymentMethod;
    private String status;
    private Timestamp paidAt;
    private int isActive;
    //constructor

    public PaymentDTO() {
    }

    public PaymentDTO(int id, int medicalRecordId, String patientName, String diagnosis, double totalAmount, String paymentMethod, String status, Timestamp paidAt, int isActive) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
        this.patientName = patientName;
        this.diagnosis = diagnosis;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paidAt = paidAt;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", patientName=" + patientName + ", diagnosis=" + diagnosis + ", totalAmount=" + totalAmount + ", paymentMethod=" + paymentMethod + ", status=" + status + ", paidAt=" + paidAt + ", isActive=" + isActive + '}';
    }

    
}
