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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String url = "index.jsp"; // Mặc định nếu lỗi thì quay về trang chủ
        HttpSession session = request.getSession();

        // 1. NẾU CHƯA ĐĂNG NHẬP
        if (session.getAttribute("user") == null) {

            // Lấy dữ liệu từ form (Khớp với name trong index.jsp của sếp)
            String txtEmail = request.getParameter("txtEmail");
            String txtPassword = request.getParameter("txtPassword");

            UserDAO udao = new UserDAO();
            User user = udao.checkLogin(txtEmail, txtPassword); 

            if (user != null) {
                if (user.getIsActive() == 1) {
                    // LƯU SESSION
                    session.setAttribute("user", user);

                    String role = user.getRole().toLowerCase().trim();
                    if(role.equals("admin")) url = "component/admin/adminDashboard.jsp";
                    else if(role.equals("doctor")) url = "component/doctor/doctorDashboard.jsp";
                    else if(role.equals("staff")) url = "component/staff/staffDashboard.jsp";
                    else url = "component/patient/patientDashboard.jsp";

                    // ĐĂNG NHẬP THÀNH CÔNG: DÙNG REDIRECT ĐỂ ÉP NHẢY TRANG
                    response.sendRedirect(url);
                    return; // Chốt hạ tại đây, không chạy xuống dưới nữa

                } else {
                    request.setAttribute("message", "Tài khoản của bạn đã bị khóa!"); 
                    request.setAttribute("tempEmail", txtEmail); 
                    url = "index.jsp";
                }
            } else {
                request.setAttribute("message", "Email hoặc mật khẩu không chính xác!");
                request.setAttribute("tempEmail", txtEmail); 
                url = "index.jsp";
            }

        } else {
            // 2. NẾU ĐÃ ĐĂNG NHẬP TỪ TRƯỚC
            User user = (User) session.getAttribute("user");
            String role = user.getRole().toLowerCase().trim();
            
            if(role.equals("admin")) url = "component/admin/adminDashboard.jsp";
            else if(role.equals("doctor")) url = "component/doctor/doctorDashboard.jsp";
            else if(role.equals("staff")) url = "component/staff/staffDashboard.jsp";
            else url = "component/patient/patientDashboard.jsp";

            // ĐÃ CÓ SESSION: DÙNG REDIRECT ĐỂ NHẢY TRANG
            response.sendRedirect(url);
            return; // Chốt hạ tại đây
        }
        
        // ĐOẠN NÀY CHỈ CHẠY KHI ĐĂNG NHẬP THẤT BẠI (Để đẩy biến message về index.jsp)
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
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
