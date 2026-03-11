/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class Payment {
    private int id;
    private int medicalRecordId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status; // 'pending', 'paid', 'refunded'
    private Timestamp paidAt;
    //constructor

    public Payment() {
    }

    public Payment(int id, int medicalRecordId, BigDecimal totalAmount, String paymentMethod, String status, Timestamp paidAt) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paidAt = paidAt;
    }

    public int getId() {
        return id;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
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

    public Timestamp getPaidAt() {
        return paidAt;
    }

    @Override
    public String toString() {
        return "Payment{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", totalAmount=" + totalAmount + ", paymentMethod=" + paymentMethod + ", status=" + status + ", paidAt=" + paidAt + '}';
    }
    
}
