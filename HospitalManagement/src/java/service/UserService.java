/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorDAO;
import dao.PatientDAO;
import dao.StaffDAO;
import dao.UserDAO;
import dao.UserDTO;
import entity.User;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import util.ErrorMessages;
/**
 *
 * @author Yuikiri
 */
public class UserService {
//    private UserDAO userDAO = new UserDAO();
//
//    // =========================================================
//    // 1. ĐĂNG NHẬP
//    // =========================================================
//    public UserDTO login(String email, String password) throws ErrorMessages.AppException {
//        if (email == null || password == null) throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
//        
//        UserDTO user = userDAO.loginUser(email.trim(), password);
//        if (user == null) throw new ErrorMessages.AppException(ErrorMessages.INVALID_CREDENTIALS);
//        if (user.getIsActive() == 0) throw new ErrorMessages.AppException(ErrorMessages.ACCOUNT_BANNED);
//        
//        return user;
//    }
//
//    // =========================================================
//    // 1. YÊU CẦU ĐĂNG KÝ 
//    // =========================================================
//    public void registerPatient(String name, String email, String pass) throws ErrorMessages.AppException {
//        // 1. Validate dữ liệu thô
//        if (name == null || name.trim().isEmpty() || email == null || pass == null) {
//            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
//        }
//
//        // 2. Chống trùng Email
//        if (userDAO.checkEmailExist(email, -1)) {
//            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
//        }
//
//        // 3. Thực thi Transaction qua DAO
//        boolean isSuccess = userDAO.registerPatientTransaction(name, email, pass);
//        
//        if (!isSuccess) {
//            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
//        }
//    }
//
//    // =========================================================
//    // 4. YÊU CẦU ĐỔI EMAIL/PASS (GỬI MAIL XÁC NHẬN)
//    // =========================================================
//    public void requestChangeSecurity(int userId, String newEmail, String newPassword) throws ErrorMessages.AppException {
//        if (newEmail != null && userDAO.checkEmailExist(newEmail, userId)) {
//            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
//        }
//        // Gửi link xác nhận đến mail hiện tại. Khi click Link mới gọi hàm thực thi bên dưới.
//        System.out.println("Hệ thống: Đã gửi link xác thực thay đổi bảo mật.");
//    }
//
//    public void confirmChangeSecurity(int userId, String newEmail, String newPassword) throws ErrorMessages.AppException {
//        if (!userDAO.updateSecurityInfo(userId, newEmail, newPassword)) {
//            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
//        }
//    }
//
//    // =========================================================
//    // 5. ĐẶC QUYỀN ADMIN (SỬA TRỰC TIẾP KHÔNG QUA MAIL)
//    // =========================================================
//    public void adminModifyUser(int userId, String userName, String email, String password) throws ErrorMessages.AppException {
//        if (userDAO.checkEmailExist(email, userId)) throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
//
//        if (!userDAO.adminUpdateUserDirectly(userId, userName, email, password)) {
//            throw new ErrorMessages.AppException(ErrorMessages.USER_NOT_FOUND);
//        }
//    }
//
//    // =========================================================
//    // 6. KHÓA TÀI KHOẢN (ADMIN)
//    // =========================================================
//    public void toggleUser(int userId, int isActive) throws ErrorMessages.AppException {
//        if (!userDAO.toggleUserStatus(userId, isActive)) {
//            throw new ErrorMessages.AppException(ErrorMessages.USER_NOT_FOUND);
//        }
//    }
//
//    public boolean isEmailAvailable(String email) {
//        return !userDAO.checkEmailExist(email, -1);
//    }
    private final UserDAO userDAO = new UserDAO();

    // Ném ra AppException thay vì Exception chung chung
    public UserDTO authenticate(String email, String password) throws ErrorMessages.AppException {
        User user = userDAO.checkLogin(email, password);

        if (user == null) {
            // Ném lỗi 401
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_CREDENTIALS);
        }

        if (user.getIsActive() == 0) {
            // Ném lỗi 403 (Forbidden / Banned)
            throw new ErrorMessages.AppException(ErrorMessages.ACCOUNT_BANNED);
        }

        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getRole());
    }
}
