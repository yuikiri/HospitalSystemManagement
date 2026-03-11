/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AppointmentDAO;
import dao.AppointmentDTO;
import java.util.List;
import util.ErrorMessages;
import entity.Appointment;
import java.sql.Timestamp;

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
}
