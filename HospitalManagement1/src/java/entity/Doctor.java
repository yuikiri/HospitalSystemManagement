// entity/doctors.java
package entity;

public class Doctor {
    private int id;
    private User user; 
    
    private String name;
    private int gender; // Vẫn giữ nguyên kiểu số nguyên (1, 2) của Database
    private String position;
    private String phone;
    private String licenseNumber;
    //constructor

    public Doctor() {
    }

    public Doctor(int id, User user, String name, int gender, String position, String phone, String licenseNumber) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
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
        return "Doctor{" + "id=" + id + ", user=" + user + ", name=" + name + ", gender=" + gender + ", position=" + position + ", phone=" + phone + ", licenseNumber=" + licenseNumber + '}';
    }

    
}