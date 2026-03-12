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
    private UserService userService = new UserService();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String url = "index.jsp";
        HttpSession session = request.getSession();

        // 1. LẤY DỮ LIỆU TỪ FORM (Khớp 100% với name trong index.jsp của sếp)
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        UserDAO udao = new UserDAO();

        // 2. KIỂM TRA TRÙNG EMAIL
        if (udao.checkEmailExist(email)) {
            // Lỗi: Bắn thông báo về form
            request.setAttribute("regError", "Email này đã được sử dụng! Vui lòng chọn email khác.");
            request.setAttribute("tempName", name);
            request.setAttribute("tempEmail", email);
            request.setAttribute("tempPhone", phone);
            url = "index.jsp";
        } else {
            // 3. THỰC HIỆN ĐĂNG KÝ (Truyền chuỗi rỗng "" cho address vì form index không có)
            boolean isSuccess = udao.registerPatient(name, email, password, phone, "");

            if (isSuccess) {
                // ==========================================
                // 4. AUTO-LOGIN: VỪA ĐĂNG KÝ XONG LÀ ĐĂNG NHẬP LUÔN
                // ==========================================
                User user = udao.checkLogin(email, password);
                
                if (user != null && user.getIsActive() == 1) {
                    session.setAttribute("user", user);
                    url = "component/patient/patientDashboard.jsp";

                    // ĐĂNG NHẬP THÀNH CÔNG: ÉP NHẢY TRANG BẰNG REDIRECT
                    response.sendRedirect(url);
                    return; // Chốt hạ tại đây, không chạy xuống dưới nữa
                } else {
                    // Phòng hờ trường hợp hy hữu
                    request.setAttribute("regSuccess", "Đăng ký thành công! Vui lòng đăng nhập.");
                    url = "index.jsp";
                }
            } else {
                request.setAttribute("regError", "Lỗi hệ thống! Không thể đăng ký lúc này.");
                request.setAttribute("tempName", name);
                request.setAttribute("tempEmail", email);
                request.setAttribute("tempPhone", phone);
                url = "index.jsp";
            }
        }

        // ==========================================
        // CHỈ DÙNG FORWARD KHI BỊ LỖI (Để đẩy biến regError về index.jsp)
        // ==========================================
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
