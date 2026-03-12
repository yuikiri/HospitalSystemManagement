package controller;

import dao.UserDAO;
import dao.UserDTO;
import dao.StaffDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.UserService;
import service.StaffService;
import util.ErrorMessages;

public class LoginController extends HttpServlet {

    private UserService userService = new UserService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String url = "index.jsp";
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {

            String txtEmail = request.getParameter("txtEmail");
            String txtPassword = request.getParameter("txtPassword");

            UserDAO udao = new UserDAO();
            User user = udao.checkLogin(txtEmail, txtPassword);

            if (user != null) {

                if (user.getIsActive() == 1) {

                    session.setAttribute("user", user);

                    String role = user.getRole().toLowerCase().trim();

                    if (role.equals("admin")) {
                        url = "component/admin/adminDashboard.jsp";
                    } 
                    else if (role.equals("doctor")) {
                        url = "component/doctor/doctorDashboard.jsp";
                    } 
                    else if (role.equals("staff")) {

                        // 🔹 LẤY THÔNG TIN STAFF
                        StaffService staffService = new StaffService();
                        try {
                            StaffDTO staff = staffService.getProfileByUserId(user.getId());
                            session.setAttribute("staff", staff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        url = "component/staff/staffDashboard.jsp";
                    } 
                    else {
                        url = "component/patient/patientDashboard.jsp";
                    }

                    response.sendRedirect(url);
                    return;

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

            User user = (User) session.getAttribute("user");
            String role = user.getRole().toLowerCase().trim();

            if (role.equals("admin")) {
                url = "component/admin/adminDashboard.jsp";
            } 
            else if (role.equals("doctor")) {
                url = "component/doctor/doctorDashboard.jsp";
            } 
            else if (role.equals("staff")) {
                url = "component/staff/staffDashboard.jsp";
            } 
            else {
                url = "component/patient/patientDashboard.jsp";
            }

            response.sendRedirect(url);
            return;
        }

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
        return "Login Controller";
    }
}