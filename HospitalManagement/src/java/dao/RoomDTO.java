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
    private String departmentName; // MỚI: Tên khoa (Để hiển thị)
    
    private int roomType; 
    private String roomTypeName; // MỚI: Tên loại phòng (Để hiển thị)
    
    private int roomNumber;
    private String status;
    private int isActive;
    //constructor

    public RoomDTO() {
    }

    public RoomDTO(int id, int departmentId, String departmentName, int roomType, String roomTypeName, int roomNumber, String status, int isActive) {
        this.id = id;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.roomType = roomType;
        this.roomTypeName = roomTypeName;
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

    @Override
    public String toString() {
        return "RoomDTO{" + "id=" + id + ", departmentId=" + departmentId + ", departmentName=" + departmentName + ", roomType=" + roomType + ", roomTypeName=" + roomTypeName + ", roomNumber=" + roomNumber + ", status=" + status + ", isActive=" + isActive + '}';
    }

    
}
