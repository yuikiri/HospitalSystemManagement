// entity/staffs.java
package entity;

public class Staff {
    private int id;
    private int userId; // NOT NULL trong DB
    private String name;
    private int gender; // 1: Nam, 2: Nữ
    private String position;
    private String phone;
    //constructor

    public Staff() {
    }

    public Staff(int id, int userId, String name, int gender, String position, String phone) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.phone = phone;
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

    @Override
    public String toString() {
        return "Staff{" + "id=" + id + ", userId=" + userId + ", name=" + name + ", gender=" + gender + ", position=" + position + ", phone=" + phone + '}';
    }
    

    
}