/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Users;
import dao.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;


public class loginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("txtUser");
        String pass = request.getParameter("txtPass");

        UserDAO dao = new UserDAO();
        Users user = dao.checkLogin(email, pass);

        if (user != null) {
            // 4. Đăng nhập thành công -> Tạo Session
            HttpSession session = request.getSession();
            session.setAttribute("account", user);

            // 5. Điều hướng theo Role
            String role = user.getRole().toLowerCase(); // Lấy từ DB

            if ("admin".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/component/admin/style/admin_dashboard.jsp");
            } else if ("doctor".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/view/doctor_dashboard.jsp");
            } else if ("staff".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/view/staff_dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/view/PatientDashboard.jsp");
            }
        } else {
            // 6. Thất bại -> Quay lại index.jsp kèm thông báo lỗi
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}