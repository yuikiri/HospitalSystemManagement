/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.StaffDepartmentDAO;
import dao.StaffDepartmentDTO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class StaffDepartmentService {
    private final StaffDepartmentDAO sdDAO = new StaffDepartmentDAO();

    // =========================================================
    // 1. LẤY DANH SÁCH XEM CHUNG
    // =========================================================
    
    public List<StaffDepartmentDTO> getStaffsByDepartment(int departmentId) {
        return sdDAO.getStaffsByDepartment(departmentId);
    }

    public List<StaffDepartmentDTO> getDepartmentsByStaff(int staffId) {
        return sdDAO.getDepartmentsByStaff(staffId);
    }

    // =========================================================
    // 2. ADMIN PHÂN CÔNG NHÂN VIÊN VÀO KHOA
    // =========================================================
    
    public void assignStaffToDepartment(int staffId, int departmentId) throws ErrorMessages.AppException {
        // A. Validate dữ liệu cơ bản
        if (staffId <= 0 || departmentId <= 0) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Chống "Spam" / Chống phân công trùng
        if (sdDAO.isAssigned(staffId, departmentId)) {
            // Ném lỗi 409 (Conflict) báo cho Admin biết là đã thêm rồi
            throw new ErrorMessages.AppException(new ErrorMessages.ErrorInfo(409, "Nhân viên này đã được phân công vào khoa này từ trước!"));
        }

        // C. Thực thi xuống Database
        if (!sdDAO.assignStaffToDepartment(staffId, departmentId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. ADMIN GỠ NHÂN VIÊN KHỎI KHOA
    // =========================================================
    
    public void removeStaffFromDepartment(int staffId, int departmentId) throws ErrorMessages.AppException {
        // A. Validate tham số
        if (staffId <= 0 || departmentId <= 0) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Kiểm tra xem có thực sự đang nằm trong khoa đó không
        if (!sdDAO.isAssigned(staffId, departmentId)) {
            throw new ErrorMessages.AppException(new ErrorMessages.ErrorInfo(404, "Không thể gỡ! Nhân viên này hiện không thuộc khoa này."));
        }

        // C. Thực thi lệnh Xóa
        if (!sdDAO.removeStaffFromDepartment(staffId, departmentId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
}
