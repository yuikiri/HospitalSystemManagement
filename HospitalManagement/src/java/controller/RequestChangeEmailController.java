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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        
        
        
        
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("patient")) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        
        
        
        
        String currentEmail = request.getParameter("currentEmail");
        String currentPassword = request.getParameter("currentPassword");
        String newEmail = request.getParameter("newEmail");
        
        try {
            if (!currentEmail.equals(currentUser.getEmail())) {
                throw new ErrorMessages.AppException(ErrorMessages.WRONG_CURRENT_EMAIL);
            }
            UserDAO userDAO = new UserDAO();
            if (!userDAO.checkCurrentPassword(currentUser.getId(), currentPassword)) {
                throw new ErrorMessages.AppException(ErrorMessages.WRONG_PASSWORD);
            }
            if (userDAO.checkEmailExist(newEmail)) {
                throw new ErrorMessages.AppException(ErrorMessages.EMAIL_EXISTED);
            }
            
            String otp = String.format("%06d", new java.util.Random().nextInt(999999));
            
//            System.out.println("\n=================================");
//            System.out.println("MÃ OTP ĐỔI EMAIL CỦA SẾP LÀ: " + otp);
//            System.out.println("=================================\n");
            
            session.setAttribute("savedEmailOtp", otp);
            session.setAttribute("pendingEmail", newEmail);
            
            util.SenMailUtil.sendOTP(newEmail, otp); // Gửi OTP vào mail MỚI
            response.sendRedirect(request.getContextPath() + "/verifyEmailOTP.jsp");
            
        } catch (ErrorMessages.AppException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
        }
    }
}
