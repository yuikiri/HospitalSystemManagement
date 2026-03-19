package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="LogoutController", urlPatterns={"/LogoutController"})
public class LogoutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. BẮT BUỘC: Xóa Cache trình duyệt để chống nút Back
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // 2. Lấy session hiện tại (không tạo mới nếu chưa có)
        HttpSession session = request.getSession(false);

        // 3. Nếu tồn tại thì hủy hoàn toàn các attribute (user, doctor, patient...)
        if (session != null) {
            session.invalidate();
        }

        // 4. Quay về trang index 
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}