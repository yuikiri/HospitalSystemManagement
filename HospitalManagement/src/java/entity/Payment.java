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
    private double totalAmount;
    private String paymentMethod; // 'cash', 'card', 'banking'
    private String status;        // 'unpaid', 'paid', 'refunded'
    private Timestamp paidAt;     // Thời gian khách chuyển tiền
    private int isActive;
    //constructor

    public Payment() {
    }

    public Payment(int id, int medicalRecordId, double totalAmount, String paymentMethod, String status, Timestamp paidAt, int isActive) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
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
        return "Payment{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", totalAmount=" + totalAmount + ", paymentMethod=" + paymentMethod + ", status=" + status + ", paidAt=" + paidAt + ", isActive=" + isActive + '}';
    }

    
}
