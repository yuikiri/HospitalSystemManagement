/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class DoctorDepartment {
    private int doctorId;
    private int departmentId;
    //constructor

    public DoctorDepartment() {
    }

    public DoctorDepartment(int doctorId, int departmentId) {
        this.doctorId = doctorId;
        this.departmentId = departmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    @Override
    public String toString() {
        return "DoctorDepartment{" + "doctorId=" + doctorId + ", departmentId=" + departmentId + '}';
    }
    
}
