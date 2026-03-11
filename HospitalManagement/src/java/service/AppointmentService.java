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

/**
 *
 * @author Yuikiri
 */
public class AppointmentService {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    // NGHIỆP VỤ 1: Bệnh nhân/Lễ tân ĐẶT LỊCH (Booking)
    public boolean bookAppointment(Appointment app) throws ErrorMessages.AppException {
        try {
            // Bước kiểm tra quan trọng nhất: Chống đụng lịch!
            boolean isConflict = appointmentDAO.isTimeSlotConflict(
                app.getDoctorId(), 
                app.getRoomId(), 
                app.getStartTime(), 
                app.getEndTime()
            );

            if (isConflict) {
                // Đá văng ra lỗi 409 ngay lập tức
                throw new ErrorMessages.AppException(ErrorMessages.APPOINTMENT_TIME_CONFLICT);
            }

            // Nếu an toàn, tiến hành lưu vào DB
            boolean isSuccess = appointmentDAO.insertAppointment(app);
            if (!isSuccess) {
                throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
            }
            return true;

        } catch (ErrorMessages.AppException e) {
            throw e; // Lỗi nghiệp vụ, ném tiếp ra Controller
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // NGHIỆP VỤ 2: Lấy lịch cho Bác sĩ xem
    public List<AppointmentDTO> getScheduleForDoctor(int doctorId) throws ErrorMessages.AppException {
        try {
            return appointmentDAO.getAppointmentsByDoctor(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // NGHIỆP VỤ 3: Lấy lịch sử khám của Bệnh nhân
    public List<AppointmentDTO> getHistoryForPatient(int patientId) throws ErrorMessages.AppException {
        try {
            return appointmentDAO.getAppointmentsByPatient(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
    
    // NGHIỆP VỤ 4: Đổi trạng thái (Bác sĩ đổi sang 'completed' khi khám xong)
    public boolean changeStatus(int appId, String newStatus) throws ErrorMessages.AppException {
        try {
            return appointmentDAO.updateStatus(appId, newStatus);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
