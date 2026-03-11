/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class DoctorShiftDTO {
    private final int doctorId;
    private final int shiftId;
    private final String doctorName;
    private final String role;
    private final String startTimeStr;
    private final String endTimeStr;
    private final int roomNumber;
    //constructor

    public DoctorShiftDTO(int doctorId, int shiftId, String doctorName, String role, String startTimeStr, String endTimeStr, int roomNumber) {
        this.doctorId = doctorId;
        this.shiftId = shiftId;
        this.doctorName = doctorName;
        this.role = role;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
        this.roomNumber = roomNumber;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getRole() {
        return role;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return "DoctorShiftDTO{" + "doctorId=" + doctorId + ", shiftId=" + shiftId + ", doctorName=" + doctorName + ", role=" + role + ", startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", roomNumber=" + roomNumber + '}';
    }
    
}
