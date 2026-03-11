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
public class AppointmentDTO {
    private int id;
    private int patientId;
    private String patientName;   // MỚI: Tên bệnh nhân
    
    private int doctorId;
    private String doctorName;    // MỚI: Tên bác sĩ
    
    private int roomId;
    private int roomNumber;       // MỚI: Số phòng
    private String departmentName;// MỚI: Tên khoa
    
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private Timestamp createdAt;
    private int isActive;
    //constructor

    public AppointmentDTO() {
    }

    public AppointmentDTO(int id, int patientId, String patientName, int doctorId, String doctorName, int roomId, int roomNumber, String departmentName, Timestamp startTime, Timestamp endTime, String status, Timestamp createdAt, int isActive) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.departmentName = departmentName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" + "id=" + id + ", patientId=" + patientId + ", patientName=" + patientName + ", doctorId=" + doctorId + ", doctorName=" + doctorName + ", roomId=" + roomId + ", roomNumber=" + roomNumber + ", departmentName=" + departmentName + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", createdAt=" + createdAt + ", isActive=" + isActive + '}';
    }

    
}
