/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class DoctorShift {
    private int doctorId;
    private int shiftId;
    private String role;
    //constructor

    public DoctorShift() {
    }

    public DoctorShift(int doctorId, int shiftId, String role) {
        this.doctorId = doctorId;
        this.shiftId = shiftId;
        this.role = role;
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

    @Override
    public String toString() {
        return "DoctorShift{" + "doctorId=" + doctorId + ", shiftId=" + shiftId + ", role=" + role + '}';
    }
    
}
