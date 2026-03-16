/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PatientDAO;
import dao.PatientDTO;
import dao.UserDAO;
import java.util.List;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class PatientService {
    private PatientDAO patientDAO;
    private UserDAO userDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
        this.userDAO = new UserDAO();
    }

    public List<PatientDTO> getListForAdmin() {
        return patientDAO.getAllPatientsForAdmin();
    }
    
    public PatientDTO getPatientById(int id) {
        return patientDAO.getPatientById(id);
    }

    // =========================================================
    // THÊM BỆNH NHÂN MỚI (CÓ NEM NGOẠI LỆ THÔNG BÁO LỖI)
    // =========================================================
    public void addWalkInPatient(String name, java.sql.Date dob, int gender, String phone, String address) throws Exception {
        // 1. Validate dữ liệu trống
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Tên bệnh nhân không được để trống!");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new Exception("Số điện thoại không được để trống!");
        }
        
        // 2. Validate nghiệp vụ: Số điện thoại đã tồn tại chưa? (Truyền -1 vì đây là tạo mới)
        if (patientDAO.checkPhoneExist(phone, -1)) {
            throw new Exception("Số điện thoại '" + phone + "' đã được đăng ký. Vui lòng kiểm tra lại!");
        }

        // 3. Thực thi Insert
        boolean isSuccess = patientDAO.insertWalkInPatient(name, dob, gender, phone, address);
        
        // 4. Nếu Database lỗi (rớt mạng, sai SQL...)
        if (!isSuccess) {
            throw new Exception("Lỗi hệ thống: Không thể thêm bệnh nhân lúc này. Vui lòng thử lại sau!");
        }
    }

    // =========================================================
    // CẬP NHẬT THÔNG TIN (CÓ NEM NGOẠI LỆ)
    // =========================================================
    public void updatePatient(int id, String name, java.sql.Date dob, int gender, String phone, String address) throws Exception {
        // 1. Kiểm tra bệnh nhân có tồn tại không
        PatientDTO existingPatient = patientDAO.getPatientById(id);
        if (existingPatient == null) {
            throw new Exception("Không tìm thấy dữ liệu bệnh nhân này trong hệ thống!");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Tên bệnh nhân không được để trống!");
        }

        // 2. Validate trùng số điện thoại (Loại trừ chính ID của bệnh nhân này ra)
        if (patientDAO.checkPhoneExist(phone, id)) {
            throw new Exception("Số điện thoại '" + phone + "' đã bị trùng với một bệnh nhân khác!");
        }

        // 3. Thực thi Update
        boolean isSuccess = patientDAO.updatePatientInfo(id, name, dob, gender, phone, address);
        if (!isSuccess) {
            throw new Exception("Cập nhật thất bại do lỗi kết nối cơ sở dữ liệu!");
        }
    }

    // =========================================================
    // KHÓA / MỞ KHÓA TÀI KHOẢN (Chỉ áp dụng nếu bệnh nhân có userId)
    // =========================================================
    public void togglePatientAccountStatus(int patientId, int status) throws Exception {
        PatientDTO patient = patientDAO.getPatientById(patientId);
        
        if (patient == null) {
            throw new Exception("Bệnh nhân không tồn tại!");
        }
        if (patient.getUserId() == null) {
            throw new Exception("Bệnh nhân này là khách vãng lai, chưa có tài khoản web để khóa/mở!");
        }
        
        // Gọi thẳng sang UserDAO để đổi trạng thái
//        boolean isSuccess = userDAO.toggleUserStatus(patient.getUserId(), status);
//        if (!isSuccess) {
//            throw new Exception("Không thể thay đổi trạng thái tài khoản lúc này!");
//        }
    }
    
    //=====================================================
    //////////////////////////////////////Hoàng
    //=====================================================
    // Lấy thông tin bệnh nhân qua ID của User đang đăng nhập
    public PatientDTO getPatientByUserId(int userId) {
        return patientDAO.getPatientByUserId(userId);
    }
    //=====================================================
    //=====================================================
}
