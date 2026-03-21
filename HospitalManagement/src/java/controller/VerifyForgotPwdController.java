/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yuikiri
 */
public class VerifyForgotPwdController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String inputOtp = request.getParameter("otpCode");
        HttpSession session = request.getSession();

        // Lấy thông tin đã cất ở Bước 1 ra
        String savedOtp = (String) session.getAttribute("forgotOtp");
        String forgotEmail = (String) session.getAttribute("forgotEmail");
        String forgotNewPass = (String) session.getAttribute("forgotNewPass");

        // Nếu Session bị chết ngầm (người dùng để treo quá lâu)
        if (savedOtp == null || forgotEmail == null) {
            response.sendRedirect(request.getContextPath() + "/ForgotPasswordController");
            return;
        }

        // Kiểm tra OTP
        if (savedOtp.equals(inputOtp)) {
            // Cập nhật mật khẩu bằng Email
            UserDAO userDAO = new UserDAO();
            userDAO.updatePasswordByEmail(forgotEmail, forgotNewPass); // MẸO: Xem Bước 3

            // Dọn dẹp chiến trường
            session.removeAttribute("forgotOtp");
            session.removeAttribute("forgotEmail");
            session.removeAttribute("forgotNewPass");
            
            // Báo thành công và đá về trang chủ để đăng nhập
            request.setAttribute("message", "Khôi phục mật khẩu thành công! Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            
        } else {
            // Sai OTP
            request.setAttribute("errorMessage", "Mã OTP không chính xác!");
            request.getRequestDispatcher("verifyForgotOTP.jsp").forward(request, response);
        }
    }
}
