/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 *
 * @author Yuikiri
 */
public class Shift {
    private int id;
    private int roomId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status; // 'scheduled', 'completed', 'cancelled'
    private int isActive;
    private String note;
    //constructor

    public Shift() {
    }

    public Shift(int id, int roomId, Timestamp startTime, Timestamp endTime, String status, int isActive, String note) {
        this.id = id;
        this.roomId = roomId;
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
        return "Shift{" + "id=" + id + ", roomId=" + roomId + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", isActive=" + isActive + ", note=" + note + '}';
    }

    
}
