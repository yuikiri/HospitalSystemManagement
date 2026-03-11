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
public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private int roomId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private Timestamp createdAt;
    //constructor

    public Appointment() {
    }

    public Appointment(int id, int patientId, int doctorId, int roomId, Timestamp startTime, Timestamp endTime, String status, Timestamp createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getRoomId() {
        return roomId;
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

    @Override
    public String toString() {
        return "Appointment{" + "id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId + ", roomId=" + roomId + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", createdAt=" + createdAt + '}';
    }

    
    
}
