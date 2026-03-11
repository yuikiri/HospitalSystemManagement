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
public class StaffShiftDTO {
    private int staffId;
    private int shiftId;
    private String role; 
    
    private String staffName;      // JOIN từ bảng Staffs
    private Timestamp startTime;   // JOIN từ bảng Shifts
    private Timestamp endTime;     // JOIN từ bảng Shifts
    //constructor

    public StaffShiftDTO() {
    }

    public StaffShiftDTO(int staffId, int shiftId, String role, String staffName, Timestamp startTime, Timestamp endTime) {
        this.staffId = staffId;
        this.shiftId = shiftId;
        this.role = role;
        this.staffName = staffName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    StaffShiftDTO(int aInt, int aInt0, String string, String string0, Timestamp timestamp, Timestamp timestamp0, int aInt1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getStaffId() {
        return staffId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getRole() {
        return role;
    }

    public String getStaffName() {
        return staffName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "StaffShiftDTO{" + "staffId=" + staffId + ", shiftId=" + shiftId + ", role=" + role + ", staffName=" + staffName + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }

    
}
