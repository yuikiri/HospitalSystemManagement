/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionDTO {
    private int id;
    private int medicalRecordId;
    
    private String patientName; // MỚI: Tên bệnh nhân (Lấy qua JOIN)
    private String doctorName;  // MỚI: Tên bác sĩ kê đơn (Lấy qua JOIN)
    private String diagnosis;   // MỚI: Chẩn đoán bệnh (Từ bảng MedicalRecord)
    
    private String notes;
    private Timestamp createdAt;
    private String status;
    private int isActive;
    //constructor

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(int id, int medicalRecordId, String patientName, String doctorName, String diagnosis, String notes, Timestamp createdAt, String status, int isActive) {
        this.id = id;
        this.medicalRecordId = medicalRecordId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
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

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
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

    public String getStatus() {
        return status;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "PrescriptionDTO{" + "id=" + id + ", medicalRecordId=" + medicalRecordId + ", patientName=" + patientName + ", doctorName=" + doctorName + ", diagnosis=" + diagnosis + ", notes=" + notes + ", createdAt=" + createdAt + ", status=" + status + ", isActive=" + isActive + '}';
    }
    
}
