/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class StaffDepartmentDTO {
    private int staffId;
    private int departmentId;
    
    private String staffName;      // Tên nhân viên (JOIN từ bảng Staffs)
    private String departmentName; // Tên khoa (JOIN từ bảng Departments)
    //construcotr

    public StaffDepartmentDTO() {
    }

    public StaffDepartmentDTO(int staffId, int departmentId, String staffName, String departmentName) {
        this.staffId = staffId;
        this.departmentId = departmentId;
        this.staffName = staffName;
        this.departmentName = departmentName;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "StaffDepartmentDTO{" + "staffId=" + staffId + ", departmentId=" + departmentId + ", staffName=" + staffName + ", departmentName=" + departmentName + '}';
    }
    
}
