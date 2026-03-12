package service;

import dao.StaffDAO;
import dao.StaffDTO;
import dao.UserDAO;
import java.util.List;
import util.ErrorMessages;

public class StaffService {

    private StaffDAO staffDAO = new StaffDAO();
    private UserDAO userDAO = new UserDAO();

    // =========================================================
    // 1. LẤY THÔNG TIN HỒ SƠ STAFF
    // =========================================================
    public StaffDTO getProfileByUserId(int userId) throws ErrorMessages.AppException {

        StaffDTO staff = staffDAO.getStaffByUserId(userId);

        if (staff == null) {
            throw new ErrorMessages.AppException(ErrorMessages.STAFF_NOT_FOUND);
        }

        return staff;
    }

    // =========================================================
    // 2. STAFF TỰ UPDATE PROFILE
    // =========================================================
    public void updateProfile(int staffId,
                              int userId,
                              String email,
                              String name,
                              int gender,
                              String position,
                              String phone)
            throws ErrorMessages.AppException {

        // Validate dữ liệu
        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()) {

            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // Check email trùng
        if (userDAO.checkEmailExistForUpdate(email, userId)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        // Check phone trùng
        if (staffDAO.checkPhoneExist(phone, staffId)) {
            throw new ErrorMessages.AppException(ErrorMessages.PHONE_EXISTED);
        }

        // Update email Users
        if (!userDAO.updateEmail(userId, email)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }

        // Update bảng Staff
        if (!staffDAO.updateStaffProfile(staffId, name, gender, position, phone)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. ADMIN UPDATE STAFF
    // =========================================================
    public void adminUpdateStaff(int staffId,
                                 int userId,
                                 String email,
                                 String name,
                                 int gender,
                                 String position,
                                 String phone)
            throws ErrorMessages.AppException {

        updateProfile(staffId, userId, email, name, gender, position, phone);
    }

    // =========================================================
    // 4. ADMIN LẤY DANH SÁCH STAFF
    // =========================================================
    public List<StaffDTO> getListForAdmin() {

        return staffDAO.getAllActiveStaffs();
    }

}