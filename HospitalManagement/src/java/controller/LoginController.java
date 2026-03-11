/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.UserDTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.UserService;
import util.ErrorMessages;

/**
 *
 * @author Yuikiri
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */private UserService userService = new UserService();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String url = "index.jsp";
        HttpSession session = request.getSession();

        // 1. Nếu đã có Session, đẩy thẳng vào Dashboard
        if (session.getAttribute("user") != null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            url = getDashboardUrl(user.getRole());
        } 
        // 2. Nếu chưa, xử lý Đăng nhập
        else {
            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");

            if (email != null && password != null) {
                try {
                    // Gọi Service (Trả về DTO - Chuẩn hệ thống)
                    UserDTO user = userService.authenticate(email, password);
                    
                    // Lưu DTO vào session để dùng ở các trang JSP
                    session.setAttribute("user", user);
                    url = getDashboardUrl(user.getRole());
                    
                    // Dùng sendRedirect để URL sạch đẹp (Tránh lỗi F5 lặp form)
                    response.sendRedirect(url);
                    return; 

                } catch (ErrorMessages.AppException e) {
                    // Bắt lỗi từ ErrorMessages (Sai pass, Bị khóa...)
                    request.setAttribute("message", e.getMessage());
                    request.setAttribute("tempEmail", email);
                    url = "index.jsp";
                }
            }
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private String getDashboardUrl(String role) {
        if (role == null) return "index.jsp";
        switch (role.toLowerCase().trim()) {
            case "admin": return "component/admin/adminDashboard.jsp";
            case "doctor": return "component/doctor/doctorDashboard.jsp";
            case "staff": return "component/staff/staffDashboard.jsp";
            default: return "component/patient/patientDashboard.jsp";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
