/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class StaffShift {
    private int staffId;
    private int shiftId;
    private String role;
    //constructor

    public StaffShift() {
    }

    public StaffShift(int staffId, int shiftId, String role) {
        this.staffId = staffId;
        this.shiftId = shiftId;
        this.role = role;
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

    @Override
    public String toString() {
        return "StaffShift{" + "staffId=" + staffId + ", shiftId=" + shiftId + ", role=" + role + '}';
    }
    
}
