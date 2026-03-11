/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class DoctorDTO {
    private int id;
    private int userId;
    
    // Các thông tin vay mượn từ bảng Users (Qua lệnh JOIN)
    private String email;
    private String avatarUrl;
    private int isActive; // Biến này dùng để Admin biết bác sĩ này đã nghỉ việc hay chưa
    
    // Các thông tin gốc của Doctor
    private String name;
    private int gender;
    private String position;
    private String phone;
    private String licenseNumber;
    //constructor

    public DoctorDTO() {
    }

    public DoctorDTO(int id, int userId, String email, String avatarUrl, int isActive, String name, int gender, String position, String phone, String licenseNumber) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    DoctorDTO(int aInt, int aInt0, String string, int aInt1, String string0, String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" + "id=" + id + ", userId=" + userId + ", email=" + email + ", avatarUrl=" + avatarUrl + ", isActive=" + isActive + ", name=" + name + ", gender=" + gender + ", position=" + position + ", phone=" + phone + ", licenseNumber=" + licenseNumber + '}';
    }

    
}

