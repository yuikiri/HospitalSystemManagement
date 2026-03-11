/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class Prescription {
    private int id;
    private int medicalRecordId;
    private String notes; // Lời dặn dò của bác sĩ (VD: Uống sau ăn, kiêng đồ biển)
    private Timestamp createdAt;
    private String status; // 'active', 'dispensed' (đã phát thuốc), 'cancelled'
    private int isActive;

    //constructor
    public Prescription() {
    }

    public Prescription(int id, int medicalRecordId, String notes, Timestamp createdAt, String status, int isActive) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
        this.notes = notes;
        this.createdAt = createdAt;
        this.status = status;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Prescription{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", notes=" + notes + ", createdAt=" + createdAt + ", status=" + status + ", isActive=" + isActive + '}';
    }

    
}
