package controller;

import dao.ShiftDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.ShiftService;

@WebServlet(name = "UserScheduleController", urlPatterns = {"/MySchedule"})
public class UserScheduleController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        // 1. Kiểm tra xem đã đăng nhập chưa
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Giả sử đối tượng User trong Session của bạn có Email và Role (doctor/staff)
        // Bạn hãy đổi tên getAttribute cho đúng với code Login của bạn nhé
        entity.User user = (entity.User) session.getAttribute("LOGIN_USER");
        String email = user.getEmail();
        String role = user.getRole().toLowerCase(); // "doctor" hoặc "staff"

        // 2. Lấy tuần hiện tại (weekOffset)
        String weekStr = request.getParameter("weekOffset");
        int weekOffset = (weekStr == null) ? 0 : Integer.parseInt(weekStr);

        // 3. Gọi Service lấy Ma trận lịch
        ShiftService shiftService = new ShiftService();
        ShiftDTO[][] schedule = shiftService.getScheduleMatrix(email, role, weekOffset);

        // 4. Đẩy dữ liệu sang JSP
        request.setAttribute("userSchedule", schedule);
request.setAttribute("currentWeekOffset", weekOffset);

// Tự động rẽ nhánh dựa trên role
String path = "";
if ("doctor".equalsIgnoreCase(role)) {
    path = "/component/doctor/DoctorShifts.jsp"; // Đường dẫn đến file của Bác sĩ
} else {
    path = "/component/staff/StaffShifts.jsp";  // Đường dẫn đến file của Nhân viên
}

request.getRequestDispatcher(path).forward(request, response);
    }
}