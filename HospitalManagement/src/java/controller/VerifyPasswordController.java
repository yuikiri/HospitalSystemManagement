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
        
        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            new UserDAO().updatePassword(currentUser.getId(), pendingNewPassword);
            
            session.removeAttribute("savedPwdOtp");
            session.removeAttribute("pendingNewPassword");
            
            session.setAttribute("successMessage", "Đổi Mật khẩu thành công!");
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Mã OTP không chính xác!");
            request.getRequestDispatcher("verifyPasswordOTP.jsp").forward(request, response);
        }
    }
}
