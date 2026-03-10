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
    private final int doctorId;
    private final int userId;
    private final String name;
    private final String email; // Kéo từ bảng Users chỉ để hiển thị
    private final String genderStr; // "Nam" hoặc "Nữ"
    private final String position;
    private final String phone;
    private final String licenseNumber;
    //constructor

    public DoctorDTO(int doctorId, int userId, String name, String email, String genderStr, String position, String phone, String licenseNumber) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.genderStr = genderStr;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    public int getDoctorId() {
        return doctorId;
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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" + "doctorId=" + doctorId + ", userId=" + userId + ", name=" + name + ", email=" + email + ", genderStr=" + genderStr + ", position=" + position + ", phone=" + phone + ", licenseNumber=" + licenseNumber + '}';
    }

}

