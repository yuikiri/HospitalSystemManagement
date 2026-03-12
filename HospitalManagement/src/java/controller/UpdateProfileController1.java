package controller;

import dao.StaffDAO;
import dao.StaffDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/UpdateProfileController1")
public class UpdateProfileController1 extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    String userIdRaw = request.getParameter("userId");

if(userIdRaw == null){
    response.sendRedirect("index.jsp");
    return;
}

int userId = Integer.parseInt(userIdRaw);
    String email = request.getParameter("email");
    String name = request.getParameter("name");
    String phone = request.getParameter("phone");
    String position = request.getParameter("position");
    int gender = Integer.parseInt(request.getParameter("gender"));

    StaffDAO dao = new StaffDAO();

    boolean check = dao.updateProfile(userId, email, name, phone, position, gender);

    HttpSession session = request.getSession();

    if (check) {

        StaffDTO staff = dao.getStaffByUserId(userId);
        session.setAttribute("staff", staff);

        response.sendRedirect("component/staff/staffDashboard.jsp");

    } else {

        response.sendRedirect("updateProfile.jsp");

    }
}


}
