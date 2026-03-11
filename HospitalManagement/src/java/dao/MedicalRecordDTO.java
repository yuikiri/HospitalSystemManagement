/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 *
 * @author Yuikiri
 */
public class MedicalRecordDTO {
    private int id;
    private int appointmentId;
    
    private String patientName; // MỚI: Tên bệnh nhân (Lấy qua JOIN)
    private String doctorName;  // MỚI: Tên bác sĩ (Lấy qua JOIN)
    
    private String diagnosis;
    private String notes;
    private Timestamp createdAt;
    private int isActive;
    //constructor

    public MedicalRecordDTO() {
    }

    public MedicalRecordDTO(int id, int appointmentId, String patientName, String doctorName, String diagnosis, String notes, Timestamp createdAt, int isActive) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
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

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "MedicalRecordDTO{" + "id=" + id + ", appointmentId=" + appointmentId + ", patientName=" + patientName + ", doctorName=" + doctorName + ", diagnosis=" + diagnosis + ", notes=" + notes + ", createdAt=" + createdAt + ", isActive=" + isActive + '}';
    }
    
    
}
