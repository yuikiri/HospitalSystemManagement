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
private final UserDAO userDAO = new UserDAO();

    // =========================================================
    // 1. LUỒNG ĐĂNG NHẬP (Khách & Hệ thống)
    // =========================================================
    
    public UserDTO authenticate(String email, String password) throws ErrorMessages.AppException {
        User user = userDAO.checkLogin(email, password);

        if (user == null) {
            // Ném lỗi 401: Sai tài khoản / mật khẩu
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_CREDENTIALS);
        }

        if (user.getIsActive() == 0) {
            // Ném lỗi 403: Tài khoản đã bị Admin khóa (Soft Delete)
            throw new ErrorMessages.AppException(ErrorMessages.ACCOUNT_BANNED);
        }

        // Trả về DTO để Controller lưu vào Session
        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getRole());
    }

    // =========================================================
    // 2. LUỒNG ĐĂNG KÝ BỆNH NHÂN (Từ index.jsp)
    // =========================================================
    
    public void registerNewPatient(String name, String email, String password, String phone, String address) throws ErrorMessages.AppException {
        // Kiểm tra email tồn tại
        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        // Thực thi Transaction DAO
        boolean isSuccess = userDAO.registerPatient(name, email, password, phone, address);
        
        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // =========================================================
    // 3. QUYỀN ADMIN: LẤY DANH SÁCH & QUẢN LÝ
    // =========================================================

    public List<User> getAllUsersForAdmin() {
        return userDAO.getAllUsers();
    }

    // Admin tạo tài khoản mới
    public void addUserByAdmin(String name, String email, String password, String role) throws ErrorMessages.AppException {
        if (name == null || email == null || password == null || role == null) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }
        
        if (userDAO.checkEmailExist(email)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean isSuccess = userDAO.addUserByAdminTransaction(name, email, password, role);
        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Admin cập nhật tài khoản
    public void updateUserByAdmin(int userId, String name, String email, String role) throws ErrorMessages.AppException {
        // Kiểm tra trùng email nhưng bỏ qua chính User đang được cập nhật
        if (userDAO.checkEmailExistForUpdate(email, userId)) {
            throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
        }

        boolean isSuccess = userDAO.updateUserByAdmin(userId, name, email, role);
        if (!isSuccess) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }

    // Admin Khóa / Mở khóa tài khoản (Xóa mềm)
    public void toggleUserStatus(int userId, int newStatus) throws ErrorMessages.AppException {
        // Status: 0 = Khóa, 1 = Hoạt động
        if (!userDAO.toggleUserStatus(userId, newStatus)) {
            throw new ErrorMessages.AppException(ErrorMessages.USER_NOT_FOUND);
        }
    }
}
