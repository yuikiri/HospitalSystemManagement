/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorDepartmentDAO;
import dao.DoctorDepartmentDTO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class DoctorDepartmentService {
    private final DoctorDepartmentDAO ddDAO = new DoctorDepartmentDAO();

    // =========================================================
    // 1. LẤY DANH SÁCH (Không cần check lỗi phức tạp)
    // =========================================================
    
    public List<DoctorDepartmentDTO> getDoctorsByDepartment(int departmentId) {
        return ddDAO.getDoctorsByDepartment(departmentId);
    }

    public List<DoctorDepartmentDTO> getDepartmentsByDoctor(int doctorId) {
        return ddDAO.getDepartmentsByDoctor(doctorId);
    }

    // =========================================================
    // 2. ADMIN PHÂN CÔNG BÁC SĨ VÀO KHOA
    // =========================================================
    
    public void assignDoctorToDepartment(int doctorId, int departmentId) throws ErrorMessages.AppException {
        // A. Validate tham số
        if (doctorId <= 0 || departmentId <= 0) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Chống phân công trùng (Bác sĩ A đã ở Khoa X rồi mà Admin lại bấm thêm lần nữa)
        if (ddDAO.isAssigned(doctorId, departmentId)) {
            // Ném ra thông báo lỗi tùy chỉnh
            throw new ErrorMessages.AppException(new ErrorMessages.ErrorInfo(409, "Bác sĩ này đã được phân công vào khoa này rồi!"));
        }

        // C. Thực thi lưu vào DB
        if (!ddDAO.assignDoctorToDepartment(doctorId, departmentId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. ADMIN GỠ BÁC SĨ KHỎI KHOA
    // =========================================================
    
    public void removeDoctorFromDepartment(int doctorId, int departmentId) throws ErrorMessages.AppException {
        // A. Validate tham số
        if (doctorId <= 0 || departmentId <= 0) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Check xem có thật sự đang ở trong khoa không mới cho gỡ
        if (!ddDAO.isAssigned(doctorId, departmentId)) {
            throw new ErrorMessages.AppException(new ErrorMessages.ErrorInfo(404, "Không thể gỡ! Bác sĩ không thuộc khoa này."));
        }

        // C. Thực thi xóa
        if (!ddDAO.removeDoctorFromDepartment(doctorId, departmentId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
