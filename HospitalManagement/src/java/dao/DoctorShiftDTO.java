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
    
    // THÊM 2 BIẾN NÀY ĐỂ HIỂN THỊ LÊN GIAO DIỆN JSP
    private int roomNumber;        // JOIN từ bảng Rooms
    private String departmentName; // JOIN từ bảng Departments

    public DoctorShiftDTO() {
    }

    public DoctorShiftDTO(int doctorId, int shiftId, String role, String doctorName, Timestamp startTime, Timestamp endTime, int roomNumber) {
        this.doctorId = doctorId;
        this.shiftId = shiftId;
        this.role = role;
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
    }

    // 3. CONSTRUCTOR 8 THAM SỐ (Dành cho hàm cũ lúc nãy)
    public DoctorShiftDTO(int doctorId, int shiftId, String role, String doctorName, Timestamp startTime, Timestamp endTime, int roomNumber, String departmentName) {
        this.doctorId = doctorId;
        this.shiftId = shiftId;
        this.role = role;
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
        this.departmentName = departmentName;
    }

    

    public int getDoctorId() { return doctorId; }
    public int getShiftId() { return shiftId; }
    public String getRole() { return role; }
    public String getDoctorName() { return doctorName; }
    public Timestamp getStartTime() { return startTime; }
    public Timestamp getEndTime() { return endTime; }
    public int getRoomNumber() { return roomNumber; }
    public String getDepartmentName() { return departmentName; }

    @Override
    public String toString() {
        return "DoctorShiftDTO{" + "doctorId=" + doctorId + ", shiftId=" + shiftId + ", role=" + role + ", doctorName=" + doctorName + ", startTime=" + startTime + ", endTime=" + endTime + ", roomNumber=" + roomNumber + ", departmentName=" + departmentName + '}';
    }
    
}
