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

        // 1. Lấy session hiện tại (không tạo mới nếu chưa có)
        HttpSession session = request.getSession(false);

        // 2. Nếu tồn tại thì hủy hoàn toàn các attribute (LOGIN_USER, staff, patient...)
        if (session != null) {
            session.invalidate();
        }

        // 3. Quay về trang index bằng đường dẫn tuyệt đối để tránh lỗi 404
        // request.getContextPath() đảm bảo luôn về đúng gốc dự án
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