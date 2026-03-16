package controller;

import dao.UserDAO;
import dao.StaffDTO;
import dao.PatientDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.StaffService;
import service.PatientService;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String cp = request.getContextPath(); // Ví dụ: /HospitalManagement
        HttpSession session = request.getSession();

        // Lấy thông tin từ form index.jsp
        String txtEmail = request.getParameter("txtEmail");
        String txtPassword = request.getParameter("txtPassword");

        // Nếu người dùng nhấn Submit (có dữ liệu email/pass)
        if (txtEmail != null && txtPassword != null) {
            UserDAO udao = new UserDAO();
            User user = udao.checkLogin(txtEmail, txtPassword);

            if (user != null) {
                if (user.getIsActive() == 1) {
                    // QUAN TRỌNG: Phải đặt tên là LOGIN_USER để các Controller khác nhận diện được
                    session.setAttribute("LOGIN_USER", user);

                    String role = user.getRole().toLowerCase().trim();
                    String redirectUrl = "";

                    // Phân quyền điều hướng
                    if (role.equals("admin")) {
                        // Admin nên chạy qua Controller để load dữ liệu thay vì vào thẳng file JSP
                        redirectUrl = cp + "/AdminController";
                    } 
                    else if (role.equals("doctor")) {
                        redirectUrl = cp + "/component/doctor/doctorDashboard.jsp";
                    } 
                    else if (role.equals("staff")) {
                        // Lấy thêm thông tin Staff nếu cần
                        try {
                            StaffService staffService = new StaffService();
                            StaffDTO staff = staffService.getProfileByUserId(user.getId());
                            session.setAttribute("staff", staff);
                        } catch (Exception e) { e.printStackTrace(); }
                        redirectUrl = cp + "/component/staff/staffDashboard.jsp";
                    } 
                    else {
                        // Mặc định là Patient
                        try {
                            PatientService patientService = new PatientService();
                            PatientDTO patient = patientService.getPatientByUserId(user.getId());
                            session.setAttribute("patient", patient);
                        } catch (Exception e) { e.printStackTrace(); }
                        redirectUrl = cp + "/component/patient/patientDashboard.jsp";
                    }

                    // Dùng sendRedirect để đổi URL trên thanh địa chỉ, tránh lỗi F5 reset form
                    response.sendRedirect(redirectUrl);
                    return;

                } else {
                    request.setAttribute("message", "Tài khoản của bạn hiện đang bị khóa!");
                    request.setAttribute("tempEmail", txtEmail);
                }
            } else {
                request.setAttribute("message", "Email hoặc mật khẩu không đúng!");
                request.setAttribute("tempEmail", txtEmail);
            }
        }

        // Nếu đăng nhập thất bại hoặc mới mở trang, quay về index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
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
        return "Login Controller Optimized";
    }
}