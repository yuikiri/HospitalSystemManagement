/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Yuikiri
 */
public class AppointmentDTO {
    private final int id;
    private final String patientName;
    private final String patientPhone; // Bổ sung để Bác sĩ tiện liên hệ
    private final String doctorName;
    private final int roomNumber;
    private final String startTimeStr;
    private final String endTimeStr;
    private final String status;
    //constructor

    public AppointmentDTO(int id, String patientName, String patientPhone, String doctorName, int roomNumber, String startTimeStr, String endTimeStr, String status) {
        this.id = id;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.doctorName = doctorName;
        this.roomNumber = roomNumber;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" + "id=" + id + ", patientName=" + patientName + ", patientPhone=" + patientPhone + ", doctorName=" + doctorName + ", roomNumber=" + roomNumber + ", startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", status=" + status + '}';
    }

    
    
}
