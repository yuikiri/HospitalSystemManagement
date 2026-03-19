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
public class RequestChangePasswordController extends HttpServlet {

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

        String currentPass = request.getParameter("currentPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        // ĐÃ FIX: TRỎ VỀ ĐÚNG KHUNG DASHBOARD THEO ROLE
        String role = currentUser.getRole().toLowerCase().trim();
        String errorRedirectUrl = request.getContextPath() + "/MainController?action=LoadPatientDashboard"; 
        if (role.equals("doctor")) errorRedirectUrl = request.getContextPath() + "/component/doctor/doctorDashboard.jsp";
        else if (role.equals("staff")) errorRedirectUrl = request.getContextPath() + "/component/staff/staffDashboard.jsp";

        try {
            if (!newPass.equals(confirmPass)) {
                throw new ErrorMessages.AppException(ErrorMessages.PASSWORD_MISMATCH);
            }
            if (!new UserDAO().checkCurrentPassword(currentUser.getId(), currentPass)) {
                throw new ErrorMessages.AppException(ErrorMessages.WRONG_PASSWORD);
            }

            String otp = String.format("%06d", new Random().nextInt(999999));
            session.setAttribute("savedPwdOtp", otp);
            session.setAttribute("pendingNewPassword", newPass);
            
            util.SenMailUtil.sendOTP(currentUser.getEmail(), otp); 
            
            response.sendRedirect(request.getContextPath() + "/verifyPasswordOTP.jsp");

        } catch (ErrorMessages.AppException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(errorRedirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Hệ thống đang bận. Vui lòng thử lại!");
            response.sendRedirect(errorRedirectUrl);
        }
    }
}
