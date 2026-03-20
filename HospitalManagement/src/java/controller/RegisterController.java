package controller;

import service.UserService;
import util.ValidationUtil;
import util.ErrorMessages;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // 1. CHỐNG XÂM NHẬP: Quét Regex qua ValidationUtil và gọi ErrorMessages
        if (!ValidationUtil.isValidName(name)) {
            request.setAttribute("regError", ErrorMessages.INVALID_NAME_FORMAT.message);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("regError", ErrorMessages.INVALID_EMAIL_FORMAT.message);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("regError", ErrorMessages.INVALID_PASSWORD_FORMAT.message);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("regError", ErrorMessages.INVALID_PHONE_FORMAT.message);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isSafeAddress(address)) {
            request.setAttribute("regError", ErrorMessages.INVALID_ADDRESS_FORMAT.message);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // 2. Mã hóa mật khẩu sau khi xác nhận an toàn
        String hashedPassword = ValidationUtil.hashPassword(password);
        UserService service = new UserService();

        try {
            // Giả định trong UserService có check trùng email và ném lỗi EMAIL_EXISTED
            service.registerNewPatient(name, email, hashedPassword, phone, address);
            request.setAttribute("regSuccess", "Đăng ký thành công! Vui lòng đăng nhập.");
        } catch (ErrorMessages.AppException ex) {
            // Hứng lỗi chuẩn từ AppException (VD: lỗi trùng email từ service quăng ra)
            request.setAttribute("regError", ex.getErrorInfo().message);
        } catch (Exception e) {
            // Lỗi hệ thống chung (Database rớt, v.v...)
            request.setAttribute("regError", ErrorMessages.SYSTEM_ERROR.message);
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}