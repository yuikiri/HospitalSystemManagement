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
    private DoctorDAO doctorDAO = new DoctorDAO();

    // =========================================================
    // 1. LẤY THÔNG TIN ĐỂ HIỂN THỊ
    // =========================================================
    public DoctorDTO getProfileByUserId(int userId) throws ErrorMessages.AppException {
        DoctorDTO doctor = doctorDAO.getDoctorByUserId(userId);
        if (doctor == null) {
            throw new ErrorMessages.AppException(ErrorMessages.DOCTOR_NOT_FOUND);
        }
        return doctor;
    }

    public List<DoctorDTO> getDoctorListForIndex() {
        return doctorDAO.getAllActiveDoctors();
    }

    // =========================================================
    // 2. LOGIC CẬP NHẬT HỒ SƠ (Nút "Cập nhật thông tin")
    // =========================================================
    public void updateProfile(int doctorId, String name, int gender, String position, String phone, String licenseNumber) throws ErrorMessages.AppException {
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

    // =========================================================
    // 3. ADMIN SỬA TRỰC TIẾP
    // =========================================================
    public void adminUpdateDoctor(int doctorId, String name, int gender, String position, String phone, String licenseNumber) throws ErrorMessages.AppException {
        // Admin sửa thì không cần quá nhiều bước check mail, nhưng vẫn phải check trùng SĐT/Giấy phép
        updateProfile(doctorId, name, gender, position, phone, licenseNumber);
    }
}
