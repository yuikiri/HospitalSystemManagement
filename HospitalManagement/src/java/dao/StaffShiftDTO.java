/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class StaffShiftDTO {
    private final int staffId;
    private final int shiftId;
    private final String staffName;
    private final String role;
    private final String startTimeStr;
    private final String endTimeStr;
    private final int roomNumber;
    //constructor

    public StaffShiftDTO(int staffId, int shiftId, String staffName, String role, String startTimeStr, String endTimeStr, int roomNumber) {
        this.staffId = staffId;
        this.shiftId = shiftId;
        this.staffName = staffName;
        this.role = role;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
        this.roomNumber = roomNumber;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getStaffName() {
        return staffName;
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
        return "StaffShiftDTO{" + "staffId=" + staffId + ", shiftId=" + shiftId + ", staffName=" + staffName + ", role=" + role + ", startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", roomNumber=" + roomNumber + '}';
    }
    
}
