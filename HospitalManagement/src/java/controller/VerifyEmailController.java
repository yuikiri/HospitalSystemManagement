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
public class VerifyEmailController extends HttpServlet {

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
        String inputOtp = request.getParameter("otpCode");
        HttpSession session = request.getSession();
        
        String savedOtp = (String) session.getAttribute("savedEmailOtp");
        String pendingEmail = (String) session.getAttribute("pendingEmail");
        User currentUser = (User) session.getAttribute("user");
        
        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            new UserDAO().updateEmail(currentUser.getId(), pendingEmail);
            currentUser.setEmail(pendingEmail);
            session.setAttribute("user", currentUser);
            
            session.removeAttribute("savedEmailOtp");
            session.removeAttribute("pendingEmail");
            
            session.setAttribute("successMessage", "Thay đổi Email thành công!");
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Mã OTP không chính xác!");
            request.getRequestDispatcher("verifyEmailOTP.jsp").forward(request, response);
        }
    }
}
