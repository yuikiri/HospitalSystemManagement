/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class RequestChangeEmailController extends HttpServlet {

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
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String currentEmail = request.getParameter("currentEmail");
        String currentPassword = request.getParameter("currentPassword");
        String newEmail = request.getParameter("newEmail");

        // ĐÃ FIX: TRỎ VỀ ĐÚNG KHUNG DASHBOARD THEO ROLE
        String role = currentUser.getRole().toLowerCase().trim();
        String errorRedirectUrl = request.getContextPath() + "/MainController?action=LoadPatientDashboard"; 
        if (role.equals("doctor")) errorRedirectUrl = request.getContextPath() + "/component/doctor/doctorDashboard.jsp";
        else if (role.equals("staff")) errorRedirectUrl = request.getContextPath() + "/component/staff/staffDashboard.jsp";

        try {
            UserDAO userDAO = new UserDAO();
            
            if (!currentUser.getEmail().equalsIgnoreCase(currentEmail)) {
                session.setAttribute("errorMessage", "Email hiện tại không khớp với tài khoản của bạn!");
                response.sendRedirect(errorRedirectUrl); return;
            }
            if (!userDAO.checkCurrentPassword(currentUser.getId(), currentPassword)) {
                session.setAttribute("errorMessage", "Mật khẩu xác nhận không chính xác!");
                response.sendRedirect(errorRedirectUrl); return;
            }
            if (userDAO.checkEmailExistForUpdate(newEmail, currentUser.getId())) {
                session.setAttribute("errorMessage", "Email mới này đã được người khác sử dụng!");
                response.sendRedirect(errorRedirectUrl); return;
            }

            String otpCode = String.format("%06d", new Random().nextInt(999999));
            // Gửi OTP
            util.SenMailUtil.sendOTP(newEmail, otpCode);
            
            session.setAttribute("savedEmailOtp", otpCode);
            session.setAttribute("pendingEmail", newEmail);
            response.sendRedirect(request.getContextPath() + "/verifyEmailOTP.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống!");
            response.sendRedirect(errorRedirectUrl);
        }
    }
}
