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
public class MedicalRecord {
    private int id;
    private int appointmentId;
    private String diagnosis;
    private String notes;
    private Timestamp createdAt;
    private int isActive;
    //constructor

    public MedicalRecord() {
    }

    public MedicalRecord(int id, int appointmentId, String diagnosis, String notes, Timestamp createdAt, int isActive) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" + "id=" + id + ", appointmentId=" + appointmentId + ", diagnosis=" + diagnosis + ", notes=" + notes + ", createdAt=" + createdAt + ", isActive=" + isActive + '}';
    }

}
