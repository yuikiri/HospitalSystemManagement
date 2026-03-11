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
public class DoctorShiftDTO {
    private int doctorId;
    private int shiftId;
    private String role; // Vai trò (Trực chính, Trực phụ...)
    
    private String doctorName;     // JOIN từ bảng Doctors
    private Timestamp startTime;   // JOIN từ bảng Shifts
    private Timestamp endTime;     // JOIN từ bảng Shifts
    //constructor

    public DoctorShiftDTO() {
    }

    public DoctorShiftDTO(int doctorId, int shiftId, String role, String doctorName, Timestamp startTime, Timestamp endTime) {
        this.doctorId = doctorId;
        this.shiftId = shiftId;
        this.role = role;
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    DoctorShiftDTO(int aInt, int aInt0, String string, String string0, Timestamp timestamp, Timestamp timestamp0, int aInt1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getRole() {
        return role;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "DoctorShiftDTO{" + "doctorId=" + doctorId + ", shiftId=" + shiftId + ", role=" + role + ", doctorName=" + doctorName + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }

    
}
