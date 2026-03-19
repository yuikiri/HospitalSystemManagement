package dao;

import java.util.Date;
import java.util.List;

public class MedicalHistoryDTO {
    private int appointmentId;
    private String departmentName;
    private int doctorId;
    private String doctorName;
    private int roomNumber;
    private Date startTime;
    private String status;
    
    private String diagnosis;
    private String notes;
    private double totalAmount;
    private String paymentStatus;
    
    // Các biến mở rộng để tính tiền và hiện thuốc
    private List<PrescriptionItemDTO> medicines;
    private String roomName;
    private double roomPrice;
    private int days;
    private double totalMedPrice;
    private double doctorFee;

    // Constructor rỗng
    public MedicalHistoryDTO() {
    }

    // ============================================
    // TOÀN BỘ GETTER VÀ SETTER (BẮT BUỘC PHẢI CÓ)
    // ============================================
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public List<PrescriptionItemDTO> getMedicines() { return medicines; }
    public void setMedicines(List<PrescriptionItemDTO> medicines) { this.medicines = medicines; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public double getRoomPrice() { return roomPrice; }
    public void setRoomPrice(double roomPrice) { this.roomPrice = roomPrice; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public double getTotalMedPrice() { return totalMedPrice; }
    public void setTotalMedPrice(double totalMedPrice) { this.totalMedPrice = totalMedPrice; }

    public double getDoctorFee() { return doctorFee; }
    public void setDoctorFee(double doctorFee) { this.doctorFee = doctorFee; }
}