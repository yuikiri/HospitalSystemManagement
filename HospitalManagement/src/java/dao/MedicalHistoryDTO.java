/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Yuikiri
 */
public class MedicalHistoryDTO {
    private int appointmentId;
    private Timestamp startTime;
    private String departmentName;
    private Integer doctorId; // Dùng Integer để có thể chứa null nếu chưa có BS
    private String doctorName;
    private int roomNumber;
    private String status; // pending, accepted, completed, cancelled
    
    // Phần dành cho Tab Đã hoàn thành
    private double totalAmount; // Tổng viện phí
    private String paymentStatus; // paid, unpaid
    private String diagnosis; // Chẩn đoán
    private String notes; // Lời khuyên
    private double serviceFee; // Tiền khám bệnh (Tổng tiền - Tiền thuốc)
    private List<PrescriptionItemDTO> medicines; // Danh sách thuốc
    //constructor

    public MedicalHistoryDTO() {
    }

    public MedicalHistoryDTO(int appointmentId, Timestamp startTime, String departmentName, Integer doctorId, String doctorName, int roomNumber, String status, double totalAmount, String paymentStatus, String diagnosis, String notes, double serviceFee, List<PrescriptionItemDTO> medicines) {
        this.appointmentId = appointmentId;
        this.startTime = startTime;
        this.departmentName = departmentName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.roomNumber = roomNumber;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.serviceFee = serviceFee;
        this.medicines = medicines;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public void setMedicines(List<PrescriptionItemDTO> medicines) {
        this.medicines = medicines;
    }

    

    public int getAppointmentId() {
        return appointmentId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public List<PrescriptionItemDTO> getMedicines() {
        return medicines;
    }

    @Override
    public String toString() {
        return "MedicalHistoryDTO{" + "appointmentId=" + appointmentId + ", startTime=" + startTime + ", departmentName=" + departmentName + ", doctorId=" + doctorId + ", doctorName=" + doctorName + ", roomNumber=" + roomNumber + ", status=" + status + ", totalAmount=" + totalAmount + ", paymentStatus=" + paymentStatus + ", diagnosis=" + diagnosis + ", notes=" + notes + ", serviceFee=" + serviceFee + ", medicines=" + medicines + '}';
    }
    
    
}
