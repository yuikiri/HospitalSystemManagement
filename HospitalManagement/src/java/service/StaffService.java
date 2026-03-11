/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.StaffDAO;
import dao.StaffDTO;
import dao.UserDAO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class StaffService {
    private StaffDAO staffDAO = new StaffDAO();

    // =========================================================
    // 1. LẤY THÔNG TIN HỒ SƠ
    // =========================================================
    public StaffDTO getProfileByUserId(int userId) throws ErrorMessages.AppException {
        StaffDTO staff = staffDAO.getStaffByUserId(userId);
        if (staff == null) {
            throw new ErrorMessages.AppException(ErrorMessages.STAFF_NOT_FOUND);
        }
        return staff;
    }

    // =========================================================
    // 2. CẬP NHẬT HỒ SƠ (XỬ LÝ NÚT CẬP NHẬT TRÊN APP)
    // =========================================================
    public void updateProfile(int staffId, String name, int gender, String position, String phone) throws ErrorMessages.AppException {
        // A. Validate dữ liệu cơ bản
        if (name == null || name.trim().isEmpty() || position == null || phone == null) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Kiểm tra trùng số điện thoại
        if (staffDAO.checkPhoneExist(phone, staffId)) {
            throw new ErrorMessages.AppException(ErrorMessages.PHONE_EXISTED);
        }

        // C. Thực thi cập nhật dữ liệu
        if (!staffDAO.updateStaffProfile(staffId, name, gender, position, phone)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. QUYỀN ADMIN: SỬA TRỰC TIẾP
    // =========================================================
    public void adminUpdateStaff(int staffId, String name, int gender, String position, String phone) throws ErrorMessages.AppException {
        // Admin có thể chỉnh sửa mọi thứ, logic check trùng vẫn giữ để đảm bảo Data Integrity
        updateProfile(staffId, name, gender, position, phone);
    }

    public List<StaffDTO> getListForAdmin() {
        return staffDAO.getAllActiveStaffs();
    }
}
