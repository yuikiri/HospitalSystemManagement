/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AppointmentDAO;
import dao.AppointmentDTO;
import dao.MedicalHistoryDTO;
import dao.PrescriptionItemDTO;
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
    // HÀM 1: LẤY LỊCH SỬ VÀ TÍNH TOÁN TIỀN KHÁM
    public List<MedicalHistoryDTO> getProcessedMedicalHistory(int patientId, String tabType) {
        // 1. Nhờ DAO lấy danh sách thô
        List<MedicalHistoryDTO> list = appointmentDAO.getHistoryByPatient(patientId, tabType);

        // 2. Xử lý nghiệp vụ: Chỉ khi là tab "completed" mới đi tìm đơn thuốc và tính tiền
        if ("completed".equals(tabType)) {
            for (MedicalHistoryDTO app : list) {
                // Nạp Chẩn đoán & Ghi chú
                String[] details = appointmentDAO.getMedicalRecordDetail(app.getAppointmentId());
                app.setDiagnosis(details[0]);
                app.setNotes(details[1]);

                // Nạp Danh sách Đơn thuốc
                List<dao.PrescriptionItemDTO> meds = appointmentDAO.getPrescriptionItemsByAppId(app.getAppointmentId());
                app.setMedicines(meds);

                // Tính Phí khám = Tổng hóa đơn - Tổng tiền thuốc
                double medTotal = 0;
                for (dao.PrescriptionItemDTO m : meds) {
                    medTotal += (m.getQuantity() * m.getMedicinePrice());
                }
                app.setServiceFee(app.getTotalAmount() - medTotal);
            }
        }
        return list;
    }

    // HÀM 2: HỦY LỊCH HẸN (Pending)
    public boolean cancelPendingAppointmentByPatient(int appointmentId, int patientId) {
        return appointmentDAO.cancelPendingAppointment(appointmentId, patientId);
    }
    
    // =========================================================
    // HÀM CHO BÁC SĨ: HOÀN THÀNH KHÁM & TÍNH TỔNG TIỀN
    // =========================================================
    public boolean completeExaminationWithFee(int appointmentId, int roomId, String diagnosis, String notes, double doctorFee, List<PrescriptionItemDTO> meds) {
        try {
            // 1. Lấy giá của phòng khám hiện tại (Sếp nhớ tạo hàm getRoomPrice trong RoomDAO nhé)
            // Tạm ví dụ giá phòng lấy được là roomPrice
            double roomPrice = 150000; // Giả sử query ra 150k

            // 2. Tính tổng tiền thuốc
            double medicineTotal = 0;
            if (meds != null) {
                for (PrescriptionItemDTO med : meds) {
                    medicineTotal += (med.getQuantity() * med.getMedicinePrice());
                }
            }

            // 3. TÍNH TỔNG TIỀN CUỐI CÙNG LƯU VÀO DATABASE
            double finalTotalAmount = roomPrice + medicineTotal + doctorFee;

            // 4. Bắt đầu lưu vào DB theo đúng thứ tự:
            // Bước 4.1: Chuyển status Appointments -> 'completed'
            appointmentDAO.updateAppointmentStatus(appointmentId, "completed");

            // Bước 4.2: Tạo MedicalRecords (Lấy ID vừa tạo) -> diagnosis, notes
            int mrId = appointmentDAO.createEmptyMedicalRecordAndGetId(appointmentId); // Cần sửa lại hàm này truyền param diagnosis, notes

            // Bước 4.3: Tạo Prescriptions & PrescriptionItems (Nếu có thuốc)
            // ... Gọi vòng lặp insert thuốc ...

            // Bước 4.4: TẠO PAYMENTS VỚI FINAL TOTAL AMOUNT
            // String sqlPay = "INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, paidAt, isActive) VALUES (?, ?, 'cash', 'unpaid', NULL, 1)";
            // (Thực thi insert finalTotalAmount vào đây)

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
    //=====================================
    //=====================================
}
