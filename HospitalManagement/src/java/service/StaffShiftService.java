/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.StaffShiftDAO;
import dao.StaffShiftDTO;
import java.util.List;
import util.ErrorMessages;
import entity.StaffShift;

/**
 *
 * @author Yuikiri
 */
public class StaffShiftService {
    private StaffShiftDAO ssDAO = new StaffShiftDAO();

    // =========================================================
    // ADMIN PHÂN CÔNG (KHÔNG CẦN MAIL XÁC NHẬN)
    // =========================================================
    public void assignStaffToShift(int staffId, int shiftId, String role) throws ErrorMessages.AppException {
        // 1. Kiểm tra tham số
        if (role == null || role.trim().isEmpty()) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // 2. Chống đụng lịch (Conflict Check)
        if (ssDAO.hasTimeConflict(staffId, shiftId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SHIFT_TIME_CONFLICT);
        }

        // 3. Thực thi chèn vào DB
        if (!ssDAO.assignStaff(staffId, shiftId, role)) {
            // Nếu lỗi xảy ra, có thể do trùng staffId/shiftId (nếu sếp thêm Unique sau này)
            throw new ErrorMessages.AppException(ErrorMessages.SHIFT_ASSIGN_ERROR);
        }
    }

    // =========================================================
    // ADMIN GỠ NHÂN VIÊN KHỎI CA
    // =========================================================
    public void removeStaff(int staffId, int shiftId) throws ErrorMessages.AppException {
        if (!ssDAO.removeStaffFromShift(staffId, shiftId)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    public List<StaffShiftDTO> getStaffsInShift(int shiftId) {
        return ssDAO.getStaffsByShift(shiftId);
    }
}
