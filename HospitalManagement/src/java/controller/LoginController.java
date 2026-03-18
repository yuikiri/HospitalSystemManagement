package controller;

import dao.UserDAO;
import dao.UserDTO;
import dao.StaffDAO; // Thêm DAO để fix lỗi thiếu Service của người làm Staff
import dao.StaffDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.UserService;
import service.PatientService;
import util.ErrorMessages;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private UserService userService = new UserService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String cp = request.getContextPath(); // Lấy đường dẫn gốc (Bảo vệ đường dẫn Admin)
        String url = "/index.jsp";
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {

            String txtEmail = request.getParameter("txtEmail");
            String txtPassword = request.getParameter("txtPassword");

            UserDAO udao = new UserDAO();
            User user = udao.checkLogin(txtEmail, txtPassword);

            if (user != null) {

                if (user.getIsActive() == 1) {

                    // FIX LỖI 1: Bắt buộc dùng "user" thay vì "LOGIN_USER" của Admin để không chết form sếp
                    session.setAttribute("user", user);

                    String role = user.getRole().toLowerCase().trim();

                    if (role.equals("admin")) {
                        // Phần của Admin
                        url = "/AdminController";
                    } 
                    else if (role.equals("doctor")) {
                        // Phần của Doctor (Giữ nguyên logic của sếp)
                        url = "/component/doctor/doctorDashboard.jsp";
                    } 
                    else if (role.equals("staff")) {
                        // FIX LỖI 2: Dùng thẳng StaffDAO để tránh lỗi người khác chưa push StaffService
                        try {
                            StaffDAO staffDAO = new StaffDAO();
                            StaffDTO staff = staffDAO.getStaffByUserId(user.getId());
                            session.setAttribute("staff", staff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        url = "/component/staff/staffDashboard.jsp";
                    } 
                    else {
                        // Phần của Patient (Giữ nguyên 100% logic gốc của sếp)
                        PatientService patientService = new PatientService();
                        try {
                            dao.PatientDTO patient = patientService.getPatientByUserId(user.getId());
                            session.setAttribute("patient", patient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        url = "/component/patient/patientDashboard.jsp";
                    }

                    // FIX LỖI 3: Ghép ContextPath (cp) vào URL để chuyển hướng an toàn cho mọi Role
                    response.sendRedirect(cp + url);
                    return;

                } else {
                    request.setAttribute("message", "Tài khoản của bạn đã bị khóa!");
                    request.setAttribute("txtEmail", txtEmail); // Giữ lại email trên form
                    url = "/index.jsp";
                }

            } else {
                request.setAttribute("message", "Email hoặc mật khẩu không chính xác!");
                request.setAttribute("txtEmail", txtEmail); // Giữ lại email trên form
                url = "/index.jsp";
            }

        } else {
            // Xử lý khi người dùng đang có session mà gõ lại URL Login
            User user = (User) session.getAttribute("user");
            String role = user.getRole().toLowerCase().trim();

            if (role.equals("admin")) {
                url = "/AdminController";
            } 
            else if (role.equals("doctor")) {
                url = "/component/doctor/doctorDashboard.jsp";
            } 
            else if (role.equals("staff")) {
                url = "/component/staff/staffDashboard.jsp";
            } 
            else {
                url = "/component/patient/patientDashboard.jsp";
            }

            response.sendRedirect(cp + url);
            return;
        }

        // Chuyển hướng khi có lỗi đăng nhập
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Login Controller Merged";
    }
}