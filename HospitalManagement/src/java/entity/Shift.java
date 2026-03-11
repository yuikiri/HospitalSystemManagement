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
    //constructor

    public Shift() {
    }

    public Shift(int id, int roomId, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return "Shift{" + "id=" + id + ", roomId=" + roomId + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }
    
}
