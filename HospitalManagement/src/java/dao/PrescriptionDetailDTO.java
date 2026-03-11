/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author Yuikiri
 */
public class PrescriptionDetailDTO {
    private final int prescriptionId;
    private final String patientName;
    private final String doctorName;
    private final String notes;
    private final String createdAtStr;
    private final String status;
    // Danh sách các loại thuốc trong đơn này
    private final List<PrescriptionItemDTO> items;
    //constructor

    public PrescriptionDetailDTO(int prescriptionId, String patientName, String doctorName, String notes, String createdAtStr, String status, List<PrescriptionItemDTO> items) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.notes = notes;
        this.createdAtStr = createdAtStr;
        this.status = status;
        this.items = items;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public String getStatus() {
        return status;
    }

    public List<PrescriptionItemDTO> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "PrescriptionDetailDTO{" + "prescriptionId=" + prescriptionId + ", patientName=" + patientName + ", doctorName=" + doctorName + ", notes=" + notes + ", createdAtStr=" + createdAtStr + ", status=" + status + ", items=" + items + '}';
    }
    
}
