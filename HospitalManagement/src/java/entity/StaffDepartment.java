/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class StaffDepartment {
    private int staffId;
    private int departmentId;
    //construcot

    public StaffDepartment() {
    }

    public StaffDepartment(int staffId, int departmentId) {
        this.staffId = staffId;
        this.departmentId = departmentId;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    @Override
    public String toString() {
        return "StaffDepartment{" + "staffId=" + staffId + ", departmentId=" + departmentId + '}';
    }
    
}
