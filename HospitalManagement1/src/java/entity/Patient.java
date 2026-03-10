// entity/patients.java
package entity;

import java.util.Date;

public class Patient {
    private int id;
    private Integer userId; // Dùng Integer để có thể chứa giá trị null từ DB
    private String name;
    private Date dob;
    private int gender; // 1: Nam, 2: Nữ
    private String phone;
    private String address;
    //constructor

    public Patient() {
    }

    public Patient(int id, Integer userId, String name, Date dob, int gender, String phone, String address) {
        this.id = id;
        this.userId = userId;
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
        return "Patient{" + "id=" + id + ", userId=" + userId + ", name=" + name + ", dob=" + dob + ", gender=" + gender + ", phone=" + phone + ", address=" + address + '}';
    }
    

    
}