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

    // 1. Constructor không đối số
    public RoomDTO() {
    }

    // 2. Constructor đầy đủ đối số
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

    // --- CÁC HÀM GETTER ---
    public int getId() { return id; }
    public int getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public int getRoomType() { return roomType; }
    public String getRoomTypeName() { return roomTypeName; }
    public int getRoomNumber() { return roomNumber; }
    public String getStatus() { return status; }
    public int getIsActive() { return isActive; }
    public double getPrice() { return price; }

    // --- CÁC HÀM SETTER (ĐỂ HẾT BÁO LỖI Ở ShiftDAO) ---
    public void setId(int id) { this.id = id; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setRoomType(int roomType) { this.roomType = roomType; }
    public void setRoomTypeName(String roomTypeName) { this.roomTypeName = roomTypeName; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "RoomDTO{" + "id=" + id + ", departmentId=" + departmentId + ", departmentName=" + departmentName + ", roomType=" + roomType + ", roomTypeName=" + roomTypeName + ", roomNumber=" + roomNumber + ", status=" + status + ", isActive=" + isActive + ", price=" + price + '}';
    }
}