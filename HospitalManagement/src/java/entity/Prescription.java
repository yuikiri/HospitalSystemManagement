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
    private int id; // Bỏ 'final' ở id để có thể set lại sau khi insert
    private int medicalRecordId;
    private String notes;
    private Timestamp createdAt;
    private String status;
    //constructor

    public Prescription() {
    }

    public Prescription(int id, int medicalRecordId, String notes, Timestamp createdAt, String status) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
        this.notes = notes;
        this.createdAt = createdAt;
        this.status = status;
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

    @Override
    public String toString() {
        return "Prescription{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", notes=" + notes + ", createdAt=" + createdAt + ", status=" + status + '}';
    }
    
}
