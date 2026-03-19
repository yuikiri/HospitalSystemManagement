package controller;

import dao.UserDAO;
import dao.StaffDTO;
import dao.PatientDTO;
import dao.DoctorDTO; // Đảm bảo đã import
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.StaffService;
import service.PatientService;
import service.DoctorService; // Đảm bảo đã import

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String cp = request.getContextPath(); 
        HttpSession session = request.getSession();

        String txtEmail = request.getParameter("txtEmail");
        String txtPassword = request.getParameter("txtPassword");

        if (txtEmail != null && txtPassword != null) {
            UserDAO udao = new UserDAO();
            User user = udao.checkLogin(txtEmail, txtPassword);

            if (user != null) {
                if (user.getIsActive() == 1) {
                    // Lưu User vào session
                    session.setAttribute("LOGIN_USER", user);

                    String role = user.getRole().toLowerCase().trim();
                    String redirectUrl = "";

                    // --- PHÂN QUYỀN ĐIỀU HƯỚNG ---
                    if (role.equals("admin")) {
                        redirectUrl = cp + "/AdminController";
                        
                    }else if (role.equals("doctor")) {

    try {
        service.DoctorService doctorService = new service.DoctorService();
        dao.DoctorDTO doctor = doctorService.getProfileByUserId(user.getId());
        session.setAttribute("doctor", doctor);
        
        // SỬA LẠI ĐƯỜNG DẪN Ở ĐÂY (Bỏ chữ /contents/ đi)
       redirectUrl = cp + "/component/doctor/contents/doctorDashboard.jsp";
        
        System.out.println(">>> Đăng nhập Doctor thành công! Đang chuyển hướng tới: " + redirectUrl);
        
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", "Tài khoản bác sĩ chưa có hồ sơ!");
        request.getRequestDispatcher("index.jsp").forward(request, response);
        return;
    }


                        
                    } else if (role.equals("staff")) {
                        try {
                            StaffService staffService = new StaffService();
                            StaffDTO staff = staffService.getProfileByUserId(user.getId());
                            session.setAttribute("staff", staff);
                            redirectUrl = cp + "/component/staff/staffDashboard.jsp";
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute("message", "Lỗi hồ sơ nhân viên!");
                            request.getRequestDispatcher("index.jsp").forward(request, response);
                            return;
                        }
                        
                    } else {
                        // Mặc định là Patient
                        try {
                            PatientService patientService = new PatientService();
                            PatientDTO patient = patientService.getPatientByUserId(user.getId());
                            session.setAttribute("patient", patient);
                            redirectUrl = cp + "/component/patient/patientDashboard.jsp";
                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // Chuyển hướng thành công
                    response.sendRedirect(redirectUrl);
                    return;

                } else {
                    request.setAttribute("message", "Tài khoản của bạn hiện đang bị khóa!");
                }
            } else {
                request.setAttribute("message", "Email hoặc mật khẩu không đúng!");
            }
        }
        
        // Quay về trang login nếu thất bại
        request.setAttribute("tempEmail", txtEmail);
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
}