/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AppointmentDAO;
import dao.AppointmentDTO;
import dao.MedicalHistoryDTO;
import dao.PaymentDAO;
import dao.PrescriptionItemDAO;
import dao.PrescriptionItemDTO;
import dao.RoomDAO;
import dao.RoomDTO;
import java.util.List;
import util.ErrorMessages;
import entity.Appointment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class AppointmentService {
    private AppointmentDAO appointmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
    }

    // 1. Lấy danh sách hiển thị
    public List<AppointmentDTO> getActiveAppointments() {
        return appointmentDAO.getAllActiveAppointments();
    }

    public List<AppointmentDTO> getAdminAppointments() {
        return appointmentDAO.getAllAppointmentsForAdmin();
    }

    // 2. Tạo lịch hẹn khám (Bệnh nhân đặt qua web hoặc Lễ tân đặt dùm)
    public boolean bookAppointment(int patientId, int doctorId, int roomId, Timestamp startTime, Timestamp endTime) {
        // Kiểm tra logic thời gian cơ bản
        if (endTime != null && !endTime.after(startTime)) {
            return false; // Thời gian kết thúc phải diễn ra SAU thời gian bắt đầu
        }

        // Tương lai có thể thêm hàm kiểm tra xem Bác sĩ đó có đang rảnh trong khung giờ này không
        
        return appointmentDAO.insertAppointment(patientId, doctorId, roomId, startTime, endTime);
    }

    // 3. Cập nhật trạng thái Quy trình khám (Dành cho Lễ tân / Bác sĩ)
    public boolean confirmAppointment(int id) {
        return appointmentDAO.updateAppointmentStatus(id, "confirmed");
    }

    public boolean cancelAppointment(int id) {
        // Lưu ý: Hủy lịch (Cancel) khác với Xóa mềm (Deactivate)
        // Hủy lịch là bệnh nhân gọi lên báo bận, ta giữ data và đánh dấu 'cancelled'
        return appointmentDAO.updateAppointmentStatus(id, "cancelled");
    }

    public boolean completeAppointment(int id) {
        return appointmentDAO.updateAppointmentStatus(id, "completed");
    }

    // 4. Bật / Tắt trạng thái dữ liệu (Xóa mềm - Chỉ dành cho Admin)
    public boolean deactivateAppointment(int id) {
        return appointmentDAO.toggleAppointmentActive(id, 0); // Đẩy vào thùng rác
    }

    public boolean activateAppointment(int id) {
        return appointmentDAO.toggleAppointmentActive(id, 1); // Khôi phục từ thùng rác
    }
    
    //=========================================================
    ////////////////////////Hoàng
    //=========================================================
    
    public List<MedicalHistoryDTO> getProcessedMedicalHistory(int patientId, String tabType) {
        List<MedicalHistoryDTO> list = appointmentDAO.getHistoryByPatient(patientId, tabType);
        
        if (tabType.equals("completed")) {
            PrescriptionItemDAO itemDAO = new PrescriptionItemDAO();
            RoomDAO roomDAO = new RoomDAO();
            
            for (MedicalHistoryDTO dto : list) {
                try {
                    // 1. Lấy chi tiết chẩn đoán và ghi chú
                    String[] recordDetail = appointmentDAO.getMedicalRecordDetail(dto.getAppointmentId());
                    if (recordDetail[0] != null) {
                        dto.setDiagnosis(recordDetail[0]);
                        dto.setNotes(recordDetail[1]);
                    } else {
                        dto.setDiagnosis("Không có chẩn đoán");
                        dto.setNotes("");
                    }

                    // 2. Lấy danh sách thuốc và Tính tổng tiền thuốc
                    List<PrescriptionItemDTO> meds = appointmentDAO.getPrescriptionItemsByAppId(dto.getAppointmentId());
                    dto.setMedicines(meds);
                    
                    double medTotal = 0;
                    for (PrescriptionItemDTO m : meds) {
                        medTotal += (m.getMedicinePrice() * m.getQuantity());
                    }
                    dto.setTotalMedPrice(medTotal);
                    
                    // 3. Tính tiền phòng (dựa theo mã phòng đã khám)
                    RoomDTO room = roomDAO.getRoomByNumber(dto.getRoomNumber()); 
                    double roomPrice = (room != null) ? room.getPrice() : 0.0; 
                    String roomName = (room != null) ? room.getRoomTypeName() : "Phòng Khám";
                    
                    dto.setRoomName(roomName);
                    dto.setRoomPrice(roomPrice);
                    dto.setDays(1); // Mặc định tính là 1 ngày khám
                    
                    // 4. Tính bù trừ ra công bác sĩ (Không bị âm tiền)
                    double doctorFee = dto.getTotalAmount() - medTotal - (roomPrice * 1);
                    if (doctorFee < 0) doctorFee = 0; 
                    
                    dto.setDoctorFee(doctorFee);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public boolean cancelPendingAppointmentByPatient(int appointmentId, int patientId) {
        return appointmentDAO.cancelPendingAppointment(appointmentId, patientId);
    }

    // =========================================================
    // HÀM CHO BÁC SĨ: HOÀN THÀNH KHÁM & TÍNH TỔNG TIỀN
    // =========================================================
    public boolean completeExaminationWithFee(int appointmentId, int roomId, String diagnosis, String notes, double doctorFee, List<PrescriptionItemDTO> meds) {
        try {
            // 1. Lấy giá phòng
            RoomDAO roomDAO = new RoomDAO();
            RoomDTO room = roomDAO.getRoomById(roomId);
            double roomPrice = (room != null) ? room.getPrice() : 0;

            // 2. Tính tổng tiền thuốc
            double medicineTotal = 0;
            if (meds != null && !meds.isEmpty()) {
                for (PrescriptionItemDTO med : meds) {
                    // Cần đảm bảo object med có getMedicinePrice()
                    medicineTotal += (med.getQuantity() * med.getMedicinePrice());
                }
            }

            // 3. Tổng tiền cuối cùng
            double finalTotalAmount = roomPrice + medicineTotal + doctorFee;

            // 4. LƯU VÀO DATABASE THEO QUY TRÌNH
            // 4.1: Chuyển status Appointments -> 'completed'
            appointmentDAO.updateAppointmentStatus(appointmentId, "completed");

            // 4.2: Cập nhật Bệnh án (Do lúc nãy bệnh án đang rỗng) & Lấy ID Bệnh án
            int mrId = appointmentDAO.updateAndGetMedicalRecordId(appointmentId, diagnosis, notes);
            if (mrId <= 0) return false;

            // 4.3: Cập nhật Đơn thuốc thành 'active' & Lấy ID Đơn thuốc
            int presId = appointmentDAO.updateAndGetPrescriptionId(mrId, "active");

            // 4.4: Insert Chi tiết thuốc
            if (presId > 0 && meds != null && !meds.isEmpty()) {
                PrescriptionItemDAO itemDAO = new PrescriptionItemDAO();
                for (PrescriptionItemDTO med : meds) {
                    itemDAO.insertItem(presId, med.getMedicineId(), med.getQuantity(), med.getDosage(), med.getFrequency(), med.getDuration());
                }
            }

            // 4.5: Tạo Hóa Đơn Thanh Toán (Payment)
            PaymentDAO payDAO = new PaymentDAO();
            payDAO.insertPayment(mrId, finalTotalAmount);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //===========================================================================================================================
    //===========================================================================================================================
}
