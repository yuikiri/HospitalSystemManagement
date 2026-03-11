// entity/doctors.java
package entity;

public class Doctor {
    private int id;
    private int userId;
    private String name;
    private int gender; // 1: Nam, 0: Nữ
    private String position;
    private String phone;
    private String licenseNumber;
    //constructor

    public Doctor() {
    }

    public Doctor(int id, int userId, String name, int gender, String position, String phone, String licenseNumber) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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
        return "Doctor{" + "id=" + id + ", userId=" + userId + ", name=" + name + ", gender=" + gender + ", position=" + position + ", phone=" + phone + ", licenseNumber=" + licenseNumber + '}';
    }

    
}