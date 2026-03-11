/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class ShiftDTO {
    private final int shiftId;
    private final String roomName;     // VD: "Phòng 101" (Kéo từ bảng Rooms)
    private final String startTimeStr; // VD: "07:00 25/10/2023"
    private final String endTimeStr;   // VD: "11:30 25/10/2023"
    //constructor

    public ShiftDTO(int shiftId, String roomName, String startTimeStr, String endTimeStr) {
        this.shiftId = shiftId;
        this.roomName = roomName;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    @Override
    public String toString() {
        return "ShiftDTO{" + "shiftId=" + shiftId + ", roomName=" + roomName + ", startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + '}';
    }
    
}
