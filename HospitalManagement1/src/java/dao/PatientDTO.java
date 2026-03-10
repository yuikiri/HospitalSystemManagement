/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class PatientDTO {
    private final int patientId;
    private final Integer userId;
    private final String name;
    private final String dobStr; // Định dạng thành chuỗi (VD: 12/05/2000)
    private final String genderStr; // Chuyển từ int sang "Nam"/"Nữ"
    private final String phone;
    private final String address;
    private final String email; // Kéo từ bảng Users (Có thể null nếu khách vãng lai)
    //constructor

    public PatientDTO(int patientId, Integer userId, String name, String dobStr, String genderStr, String phone, String address, String email) {
        this.patientId = patientId;
        this.userId = userId;
        this.name = name;
        this.dobStr = dobStr;
        this.genderStr = genderStr;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public int getPatientId() {
        return patientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDobStr() {
        return dobStr;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "PatientDTO{" + "patientId=" + patientId + ", userId=" + userId + ", name=" + name + ", dobStr=" + dobStr + ", genderStr=" + genderStr + ", phone=" + phone + ", address=" + address + ", email=" + email + '}';
    }
    
}
