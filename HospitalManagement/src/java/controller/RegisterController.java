package controller;

import service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    String name = request.getParameter("username");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String phone = request.getParameter("phone");
    String address = request.getParameter("address");

    UserService service = new UserService();

    try {

        service.registerNewPatient(name, email, password, phone, address);

        // Thông báo đăng ký thành công
        request.setAttribute("regSuccess", "Register successful! Please login.");

    } catch (Exception e) {

        request.setAttribute("regError", e.getMessage());

    }

    // quay lại trang index
    request.getRequestDispatcher("index.jsp").forward(request, response);
}
}