/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yuikiri
 */
public class RegisterController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            String url = "index.jsp"; // LƯU Ý: Đăng ký xong (hoặc lỗi) đều quay về trang chủ để hiện Modal

            // 1. Lấy dữ liệu từ Form gửi lên
            String name = request.getParameter("username");
            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            // Tôi lấy thêm phone và address theo đúng Form gốc ban đầu của bạn
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            UserDAO userDAO = new UserDAO();

            // 2. KIỂM TRA TRÙNG EMAIL
            if (userDAO.checkEmailExist(email)) {
                // LƯU Ý: Trả về lỗi nếu Email đã tồn tại trong DB
                request.setAttribute("regError", "Email này đã được sử dụng. Vui lòng chọn Email khác!");

                // Giữ lại các thông tin người dùng đã nhập để họ không phải gõ lại từ đầu
                request.setAttribute("tempName", name);
                request.setAttribute("tempEmail", email);
                request.setAttribute("tempPhone", phone);
                request.setAttribute("tempAddress", address);
            } else {
                // 3. TIẾN HÀNH ĐĂNG KÝ
                boolean isSuccess = userDAO.registerPatient(name, email, pass, phone, address);

                if (isSuccess) {
                    // LƯU Ý: Đăng ký thành công thì báo xanh
                    request.setAttribute("regSuccess", "Đăng ký thành công! Vui lòng đăng nhập.");
                } else {
                    // LƯU Ý: Lỗi hệ thống (VD: rớt mạng DB)
                    request.setAttribute("regError", "Hệ thống đang bận, không thể đăng ký lúc này!");
                    request.setAttribute("tempName", name);
                    request.setAttribute("tempEmail", email);
                }
            }

            // Chuyển trang (Giữ nguyên request để truyền thông báo lỗi/thành công sang JSP)
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
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
