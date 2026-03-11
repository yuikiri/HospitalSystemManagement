/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class DoctorDepartmentDTO {
    private int doctorId;
    private int departmentId;
    
    private String doctorName;     // Tên bác sĩ (JOIN từ bảng Doctors)
    private String departmentName; // Tên khoa (JOIN từ bảng Departments)
    //construcotr

    public DoctorDepartmentDTO() {
    }

    public DoctorDepartmentDTO(int doctorId, int departmentId, String doctorName, String departmentName) {
        this.doctorId = doctorId;
        this.departmentId = departmentId;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "DoctorDepartmentDTO{" + "doctorId=" + doctorId + ", departmentId=" + departmentId + ", doctorName=" + doctorName + ", departmentName=" + departmentName + '}';
    }
    
}
