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
    private final int roomId;
    private final int roomNumber;
    private final String departmentName; // Kéo từ Departments
    private final String roomTypeName;   // Kéo từ RoomType
    private final String priceStr;       // Giá tiền đã format (VD: "500,000 VNĐ")
    private final String statusStr;      // Trạng thái tiếng Việt (VD: "Sẵn sàng", "Bảo trì")
    //constructor

    public RoomDTO(int roomId, int roomNumber, String departmentName, String roomTypeName, String priceStr, String statusStr) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.departmentName = departmentName;
        this.roomTypeName = roomTypeName;
        this.priceStr = priceStr;
        this.statusStr = statusStr;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    @Override
    public String toString() {
        return "RoomDTO{" + "roomId=" + roomId + ", roomNumber=" + roomNumber + ", departmentName=" + departmentName + ", roomTypeName=" + roomTypeName + ", priceStr=" + priceStr + ", statusStr=" + statusStr + '}';
    }
    
}
