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
    //login
    public static final ErrorInfo ACCOUNT_BANNED = new ErrorInfo(403, "Your account is not working. Please contact Admin.");
    public static final ErrorInfo INVALID_CREDENTIALS = new ErrorInfo(401, "Sai tài khoản hoặc mật khẩu.");
    public static final ErrorInfo DOCTOR_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ bác sĩ.");
    public static final ErrorInfo PATIENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ bệnh nhân.");
    public static final ErrorInfo STAFF_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy hồ sơ nhân viên.");
    //group 1: các ban danh muc
    public static final ErrorInfo DEPARTMENT_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin Khoa.");
    public static final ErrorInfo ROOM_TYPE_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thông tin Loại phòng.");
    public static final ErrorInfo MEDICINE_NOT_FOUND = new ErrorInfo(404, "Không tìm thấy thuôc.");
    //group 2:
    public static final ErrorInfo ROOM_NOT_FOUND = new ErrorInfo(404, "Phòng khám không tồn tại hoặc đã bị xóa.");
    //500
    public static final ErrorInfo SYSTEM_ERROR = new ErrorInfo(500, "Hệ thống gặp sự cố. Vui lòng thử lại sau.");

    // --- Object chứa cấu trúc lỗi của bạn ---
    public static class ErrorInfo {
        public final int code;
        public final String message;

        public ErrorInfo(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    // --- Class Exception tùy chỉnh để có thể dùng lệnh 'throw' ---
    public static class AppException extends Exception {
        public final ErrorInfo errorInfo;

        // Ép buộc khi ném lỗi phải truyền vào một ErrorInfo
        public AppException(ErrorInfo errorInfo) {
            super(errorInfo.message); // Gọi super() để JSP có thể lấy message bằng e.getMessage()
            this.errorInfo = errorInfo;
        }
        
        public ErrorInfo getErrorInfo() { return errorInfo; }
    }
    
}
