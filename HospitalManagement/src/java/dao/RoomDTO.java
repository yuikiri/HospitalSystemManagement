/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class RoomDTO {
    private int id;
    private int departmentId;
    private String departmentName; 
    private int roomType; 
    private String roomTypeName; 
    private int roomNumber;
    private String status;
    private int isActive;
    private double price;
    //constructor

    public RoomDTO() {
    }

    public RoomDTO(int id, int departmentId, String departmentName, int roomType, String roomTypeName, int roomNumber, String status, int isActive, double price) {
        this.id = id;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.roomType = roomType;
        this.roomTypeName = roomTypeName;
        this.roomNumber = roomNumber;
        this.status = status;
        this.isActive = isActive;
        this.price = price;
    }

    RoomDTO(int aInt, int aInt0, String string, int aInt1, String string0, int aInt2, String string1, int aInt3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getId() {
        return id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public int getRoomType() {
        return roomType;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public int getIsActive() {
        return isActive;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "RoomDTO{" + "id=" + id + ", departmentId=" + departmentId + ", departmentName=" + departmentName + ", roomType=" + roomType + ", roomTypeName=" + roomTypeName + ", roomNumber=" + roomNumber + ", status=" + status + ", isActive=" + isActive + ", price=" + price + '}';
    }

    
}
