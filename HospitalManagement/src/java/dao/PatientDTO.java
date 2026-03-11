/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.Date;

/**
 *
 * @author Yuikiri
 */
public class PatientDTO {
    private int id;
    private Integer userId; 
    
    // Vay mượn từ Users (Sẽ bị NULL nếu bệnh nhân không có tài khoản)
    private String email;
    private String avatarUrl;
    private Integer isActive; 
    
    // Gốc của Patient
    private String name;
    private Date dob;
    private int gender;
    private String phone;
    private String address;
    //constructor

    public PatientDTO() {
    }

    public PatientDTO(int id, Integer userId, String email, String avatarUrl, Integer isActive, String name, Date dob, int gender, String phone, String address) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public int getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "PatientDTO{" + "id=" + id + ", userId=" + userId + ", email=" + email + ", avatarUrl=" + avatarUrl + ", isActive=" + isActive + ", name=" + name + ", dob=" + dob + ", gender=" + gender + ", phone=" + phone + ", address=" + address + '}';
    }

    
}
