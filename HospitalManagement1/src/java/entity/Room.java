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
    private int roomNumber;
    private int departmentId;
    private int roomTypeId; // Trong DB của bạn có thể tên là roomType
    private String status;  // VD: 'available', 'maintenance'
    //constructor

    public Room() {
    }

    public Room(int id, int roomNumber, int departmentId, int roomTypeId, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.departmentId = departmentId;
        this.roomTypeId = roomTypeId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", roomNumber=" + roomNumber + ", departmentId=" + departmentId + ", roomTypeId=" + roomTypeId + ", status=" + status + '}';
    }
    
}
