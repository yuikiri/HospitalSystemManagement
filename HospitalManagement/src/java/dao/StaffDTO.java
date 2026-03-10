/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class StaffDTO {
    private final int staffId;
    private final int userId;
    private final String name;
    private final String email; // Kéo từ bảng Users
    private final String genderStr; // Chuyển từ int sang "Nam"/"Nữ"
    private final String position;
    private final String phone;
    //constructor

    public StaffDTO(int staffId, int userId, String name, String email, String genderStr, String position, String phone) {
        this.staffId = staffId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.genderStr = genderStr;
        this.position = position;
        this.phone = phone;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "StaffDTO{" + "staffId=" + staffId + ", userId=" + userId + ", name=" + name + ", email=" + email + ", genderStr=" + genderStr + ", position=" + position + ", phone=" + phone + '}';
    }
    
}
