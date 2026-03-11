/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorShiftDAO;
import dao.DoctorShiftDTO;
import java.util.List;
import util.ErrorMessages;
import entity.DoctorShift;
/**
 *
 * @author Yuikiri
 */
public class DoctorShiftService {
    private DoctorShiftDAO dsDAO = new DoctorShiftDAO();

    // =========================================================
    // ADMIN PHÂN CÔNG TRỰC (KHÔNG CẦN XÁC NHẬN MAIL)
    // =========================================================
    public void assignDoctorToShift(int doctorId, int shiftId, String role) throws ErrorMessages.AppException {
        // 1. Kiểm tra bác sĩ đã có mặt trong ca này chưa (Tránh lỗi UNIQUE)
        // (Nếu dùng hàm assign mà lỗi SQL UNIQUE thì cũng bắt được, nhưng check trước sẽ tốt hơn)
        
        // 2. Kiểm tra đụng lịch (Conflict Check)
        if (dsDAO.hasTimeConflict(doctorId, shiftId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SHIFT_TIME_CONFLICT);
        }

        // 3. Thực thi
        if (!dsDAO.assignDoctor(doctorId, shiftId, role)) {
            throw new ErrorMessages.AppException(ErrorMessages.SHIFT_ASSIGN_ERROR);
        }
    }

    // =========================================================
    // ADMIN GỠ BÁC SĨ KHỎI CA
    // =========================================================
    public void removeDoctor(int doctorId, int shiftId) throws ErrorMessages.AppException {
        if (!dsDAO.removeDoctorFromShift(doctorId, shiftId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    public List<DoctorShiftDTO> getDoctorsInShift(int shiftId) {
        return dsDAO.getDoctorsByShift(shiftId);
    }
}
