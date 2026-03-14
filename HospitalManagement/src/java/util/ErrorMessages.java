/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Yuikiri
 */
public class ErrorMessages {
    // ========================================================================
    // NHÓM 1: AUTHENTICATION & TÀI KHOẢN (Users)
    // ========================================================================
    public static final ErrorInfo INVALID_CREDENTIALS = new ErrorInfo(401, "Sai tài khoản hoặc mật khẩu.");
    public static final ErrorInfo ACCOUNT_BANNED = new ErrorInfo(403, "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Quản trị viên.");
    public static final ErrorInfo USER_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin tài khoản.");
    public static final ErrorInfo EMAIL_EXISTED = new ErrorInfo(409, "Email này đã được đăng ký trong hệ thống. Vui lòng dùng email khác.");
    public static final ErrorInfo WRONG_PASSWORD = new ErrorInfo(401, "Mật khẩu hiện tại không chính xác.");
    public static final ErrorInfo WRONG_CURRENT_EMAIL = new ErrorInfo(400, "Email xác nhận không khớp với tài khoản hiện tại.");
    public static final ErrorInfo PASSWORD_MISMATCH = new ErrorInfo(400, "Mật khẩu mới và xác nhận mật khẩu không khớp.");

    // ========================================================================
    // NHÓM 2: QUẢN LÝ NHÂN SỰ & BỆNH NHÂN (Doctors, Staffs, Patients)
    // ========================================================================
    public static final ErrorInfo DOCTOR_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ bác sĩ.");
    public static final ErrorInfo STAFF_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ nhân viên.");
    public static final ErrorInfo PATIENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ bệnh nhân.");
    public static final ErrorInfo PHONE_EXISTED = new ErrorInfo(409, "Số điện thoại này đã tồn tại trong hệ thống.");

    // ========================================================================
    // NHÓM 3: DANH MỤC CƠ SỞ VẬT CHẤT (Departments, Rooms, Medicines)
    // ========================================================================
    public static final ErrorInfo DEPARTMENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin Khoa.");
    public static final ErrorInfo DEPARTMENT_EXISTED = new ErrorInfo(409, "Tên Khoa này đã tồn tại, vui lòng chọn tên khác.");
    
    public static final ErrorInfo ROOM_TYPE_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin Loại phòng.");
    public static final ErrorInfo ROOM_NOT_FOUND = new ErrorInfo(404, "Phòng khám không tồn tại hoặc đã bị ngừng hoạt động.");
    public static final ErrorInfo ROOM_EXISTED = new ErrorInfo(409, "Số phòng này đã tồn tại trong Khoa.");

    public static final ErrorInfo MEDICINE_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin thuốc.");
    public static final ErrorInfo INVALID_MEDICINE_DATA = new ErrorInfo(400, "Giá thuốc hoặc số lượng tồn kho không được là số âm.");
    public static final ErrorInfo OUT_OF_STOCK = new ErrorInfo(400, "Thuốc trong kho không đủ số lượng để cấp phát.");

    // ========================================================================
    // NHÓM 4: QUẢN LÝ LỊCH TRÌNH (Shifts, Appointments)
    // ========================================================================
    public static final ErrorInfo SHIFT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin ca trực.");
    public static final ErrorInfo INVALID_TIME_RANGE = new ErrorInfo(400, "Thời gian kết thúc phải diễn ra SAU thời gian bắt đầu.");
    public static final ErrorInfo SHIFT_TIME_CONFLICT = new ErrorInfo(409, "Phòng này đã có ca trực được xếp trong khung giờ bạn chọn.");
    public static final ErrorInfo SHIFT_ASSIGN_ERROR = new ErrorInfo(400, "Lỗi phân công: Nhân sự đã tồn tại trong ca trực này.");

    public static final ErrorInfo APPOINTMENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin lịch hẹn.");
    public static final ErrorInfo APPOINTMENT_TIME_CONFLICT = new ErrorInfo(409, "Khung giờ này Bác sĩ hoặc Phòng khám đã có lịch. Vui lòng chọn giờ khác!");

    // ========================================================================
    // NHÓM 5: KHÁM CHỮA BỆNH (MedicalRecords, Prescriptions)
    // ========================================================================
    public static final ErrorInfo MEDICAL_RECORD_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ bệnh án.");
    public static final ErrorInfo MEDICAL_RECORD_EXISTED = new ErrorInfo(409, "Lịch hẹn này đã được lập bệnh án rồi, không thể lập thêm!");
    
    public static final ErrorInfo PRESCRIPTION_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy đơn thuốc.");
    public static final ErrorInfo PRESCRIPTION_EXISTED = new ErrorInfo(409, "Bệnh án này đã được kê đơn thuốc rồi!");
    public static final ErrorInfo INVALID_PRESCRIPTION_QUANTITY = new ErrorInfo(400, "Số lượng thuốc kê đơn phải lớn hơn 0.");

    // ========================================================================
    // NHÓM 6: TÀI CHÍNH & THANH TOÁN (Payments)
    // ========================================================================
    public static final ErrorInfo PAYMENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin hóa đơn.");
    public static final ErrorInfo PAYMENT_EXISTED = new ErrorInfo(409, "Hồ sơ bệnh án này đã xuất hóa đơn rồi!");
    public static final ErrorInfo NEGATIVE_AMOUNT = new ErrorInfo(400, "Tổng tiền thanh toán không được là số âm.");
    public static final ErrorInfo INVALID_PAYMENT_METHOD = new ErrorInfo(400, "Phương thức thanh toán không hợp lệ (Chỉ nhận: cash, card, banking).");

    // ========================================================================
    // NHÓM 7: LỖI HỆ THỐNG CHUNG
    // ========================================================================
    public static final ErrorInfo INVALID_PARAMETER = new ErrorInfo(400, "Dữ liệu đầu vào không hợp lệ hoặc bị thiếu.");
    public static final ErrorInfo SYSTEM_ERROR = new ErrorInfo(500, "Hệ thống gặp sự cố Database. Vui lòng thử lại sau.");

    // ========================================================================
    // CORE CẤU TRÚC LỖI (Không sửa phần này)
    // ========================================================================
    public static class ErrorInfo {
        public final int code;
        public final String message;

        public ErrorInfo(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static class AppException extends Exception {
        public final ErrorInfo errorInfo;

        public AppException(ErrorInfo errorInfo) {
            super(errorInfo.message); 
            this.errorInfo = errorInfo;
        }
        
        public ErrorInfo getErrorInfo() { 
            return errorInfo; 
        }
    }
    
}
