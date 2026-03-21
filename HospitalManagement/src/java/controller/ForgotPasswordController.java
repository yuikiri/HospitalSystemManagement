/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yuikiri
 */
public class ForgotPasswordController extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Tạo 1 file forgotPassword.jsp để người dùng nhập Email và Mật khẩu mới
        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
    }

    // Xử lý khi người dùng bấm nút "Gửi mã OTP"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        try {
            // 1. Kiểm tra 2 mật khẩu có khớp không
            if (!newPass.equals(confirmPass)) {
                request.setAttribute("errorMessage", "Mật khẩu nhập lại không khớp!");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                return;
            }

            // 2. Kiểm tra Email có tồn tại trong hệ thống không (Cần hàm check trong DAO)
            UserDAO userDAO = new UserDAO();
            if (!userDAO.checkEmailExist(email)) { // MẸO: Sếp xem Bước 3 để thêm hàm này
                request.setAttribute("errorMessage", "Email không tồn tại trong hệ thống!");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                return;
            }

            // 3. Tạo mã OTP và lưu tạm thông tin vào Session
            String otp = String.format("%06d", new Random().nextInt(999999));
            session.setAttribute("forgotOtp", otp);
            session.setAttribute("forgotEmail", email);
            session.setAttribute("forgotNewPass", newPass);

            // 4. Gửi Mail
            util.SenMailUtil.sendOTP(email, otp); 

            // 5. Chuyển sang trang nhập mã OTP
            response.sendRedirect(request.getContextPath() + "/verifyForgotOTP.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Hệ thống đang bận. Vui lòng thử lại!");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
        }
    }
}
