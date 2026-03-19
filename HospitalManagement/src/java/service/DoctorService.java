/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DoctorDAO;
import dao.DoctorDTO;
import dao.UserDAO;
import entity.Doctor;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class DoctorService {

    // =========================================================
    // 2. LOGIC CẬP NHẬT HỒ SƠ (Nút "Cập nhật thông tin")
    // ==========================1===============================
    public void updateDoctor1(int doctorId, String name, int gender, String position, String phone, String licenseNumber) throws ErrorMessages.AppException {
        // A. Kiểm tra dữ liệu trống
        if (name == null || name.trim().isEmpty() || phone == null || licenseNumber == null) {
            throw new ErrorMessages.AppException(ErrorMessages.INVALID_PARAMETER);
        }

        // B. Chống trùng số điện thoại
        if (doctorDAO.checkUniqueField("phone", phone, doctorId)) {
            throw new ErrorMessages.AppException(ErrorMessages.PHONE_EXISTED);
        }

        // C. Chống trùng giấy phép hành nghề
        // (Sếp có thể thêm mã lỗi LICENSE_EXISTED vào ErrorMessages nếu muốn kỹ hơn)
        if (doctorDAO.checkUniqueField("licenseNumber", licenseNumber, doctorId)) {
            throw new ErrorMessages.AppException(new ErrorMessages.ErrorInfo(409, "Số giấy phép hành nghề này đã tồn tại!"));
        }

        // D. Thực thi cập nhật
        if (!doctorDAO.updateDoctorProfile(doctorId, name, gender, position, phone, licenseNumber)) {
            throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
        }
    }
    private DoctorDAO doctorDAO = new DoctorDAO();

    // Lấy thông tin Bác sĩ theo ID của User
    public DoctorDTO getProfileByUserId(int userId) throws Exception {
        DoctorDTO doctor = doctorDAO.getDoctorByUserId(userId);
        if (doctor == null) {
            throw new Exception("Không tìm thấy hồ sơ Bác sĩ!");
        }
        return doctor;
    }

    // Xử lý Cập nhật
    public void updateDoctor(int doctorId, String name, int gender, String position, String phone, String licenseNumber) throws Exception {

        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Tên bác sĩ không được để trống!");
        }

        // Validate trùng số điện thoại
        if (doctorDAO.checkUniqueField("phone", phone, doctorId)) {
            throw new Exception("Số điện thoại '" + phone + "' đã bị trùng với nhân viên khác!");
        }

        // Validate trùng số giấy phép hành nghề
        if (doctorDAO.checkUniqueField("licenseNumber", licenseNumber, doctorId)) {
            throw new Exception("Số CCHN '" + licenseNumber + "' đã được đăng ký cho bác sĩ khác!");
        }

        // Thực thi Update vào Database
        boolean isSuccess = doctorDAO.updateDoctorProfile(doctorId, name, gender, position, phone, licenseNumber);
        if (!isSuccess) {
            throw new Exception("Cập nhật thất bại do lỗi kết nối cơ sở dữ liệu!");
        }
    }

    // =========================================================
    // 3. ADMIN SỬA TRỰC TIẾP
    // =========================================================
    public void adminUpdateDoctor(int doctorId, String name, int gender, String position, String phone, String licenseNumber) throws ErrorMessages.AppException {
        // Admin sửa thì không cần quá nhiều bước check mail, nhưng vẫn phải check trùng SĐT/Giấy phép
        updateDoctor1(doctorId, name, gender, position, phone, licenseNumber);
    }
}
