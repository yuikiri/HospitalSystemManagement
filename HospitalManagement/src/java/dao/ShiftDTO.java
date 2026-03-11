/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class ShiftDTO {
    private int id;
    private int roomId;
    private int roomNumber;        // MỚI: Số phòng (Để hiển thị)
    private String departmentName; // MỚI: Tên khoa (Để hiển thị)
    
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private int isActive;
    private String note;
    //constructor

    public ShiftDTO() {
    }

    public ShiftDTO(int id, int roomId, int roomNumber, String departmentName, Timestamp startTime, Timestamp endTime, String status, int isActive, String note) {
        this.id = id;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.departmentName = departmentName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isActive = isActive;
        this.note = note;
    }

    public int getId() {
        return id;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "ShiftDTO{" + "id=" + id + ", roomId=" + roomId + ", roomNumber=" + roomNumber + ", departmentName=" + departmentName + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", isActive=" + isActive + ", note=" + note + '}';
    }

}
