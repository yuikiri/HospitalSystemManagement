/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.logging.Logger;

/**
 *
 * @author Yuikiri
 */
public class MedicalRecordDTO {
    private final int recordId;
    private final int appointmentId;
    private final String patientName;
    private final String doctorName;
    private final String diagnosis;
    private final String notes;
    private final String createdAtStr; // Thời gian lập bệnh án
    //constructor
    private static final Logger LOG = Logger.getLogger(MedicalRecordDTO.class.getName());

    public MedicalRecordDTO(int recordId, int appointmentId, String patientName, String doctorName, String diagnosis, String notes, String createdAtStr) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.createdAtStr = createdAtStr;
    }

    public int getRecordId() {
        return recordId;
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

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    public String toString() {
        return "MedicalRecordTO{" + "recordId=" + recordId + ", appointmentId=" + appointmentId + ", patientName=" + patientName + ", doctorName=" + doctorName + ", diagnosis=" + diagnosis + ", notes=" + notes + ", createdAtStr=" + createdAtStr + '}';
    }
    
}
