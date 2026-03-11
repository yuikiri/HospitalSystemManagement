/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Yuikiri
 */
public class Room {
    private int id;
    private int departmentId;
    private int roomType; // Khóa ngoại trỏ tới bảng RoomType
    private int roomNumber;
    private String status; // 'available', 'maintenance', 'occupied'
    private int isActive;
    //constructor

    public Room() {
    }

    public Room(int id, int departmentId, int roomType, int roomNumber, String status, int isActive) {
        this.id = id;
        this.departmentId = departmentId;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.status = status;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getRoomType() {
        return roomType;
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

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", departmentId=" + departmentId + ", roomType=" + roomType + ", roomNumber=" + roomNumber + ", status=" + status + ", isActive=" + isActive + '}';
    }

    
}
