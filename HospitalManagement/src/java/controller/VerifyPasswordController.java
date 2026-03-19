/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yuikiri
 */

public class VerifyPasswordController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputOtp = request.getParameter("otpCode");
        HttpSession session = request.getSession();

        String savedOtp = (String) session.getAttribute("savedPwdOtp");
        String pendingNewPassword = (String) session.getAttribute("pendingNewPassword");
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            new UserDAO().updatePassword(currentUser.getId(), pendingNewPassword);

            session.removeAttribute("savedPwdOtp"); 
            session.removeAttribute("pendingNewPassword"); 
            
            session.setAttribute("successMessage", "Đổi mật khẩu thành công!");

            // ĐÃ FIX: TRỎ VỀ ĐÚNG KHUNG DASHBOARD
            String role = currentUser.getRole().toLowerCase().trim();
            if (role.equals("patient")) {
                response.sendRedirect(request.getContextPath() + "/MainController?action=LoadPatientDashboard");
            } else if (role.equals("doctor")) {
                response.sendRedirect(request.getContextPath() + "/component/doctor/doctorDashboard.jsp");
            } else if (role.equals("staff")) {
                response.sendRedirect(request.getContextPath() + "/component/staff/staffDashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            request.setAttribute("errorMessage", "Mã OTP không chính xác!");
            request.getRequestDispatcher("verifyPasswordOTP.jsp").forward(request, response);
        }
    }
}