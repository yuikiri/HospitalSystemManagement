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
        
        // 1. KIỂM TRA ĐĂNG NHẬP (ĐÃ FIX: Dùng biến "user" thay vì "LOGIN_USER")
        if (session == null || session.getAttribute("user") == null) {
            // ĐÃ FIX: Điều hướng về đúng trang index.jsp có kèm đường dẫn gốc
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Lấy tài khoản từ Session (ĐÃ FIX TÊN BIẾN)
        entity.User user = (entity.User) session.getAttribute("user");
        String email = user.getEmail();
        String role = user.getRole().toLowerCase().trim(); // Thêm .trim() cho an toàn giống RoleFilter

        // 2. Lấy tuần hiện tại (weekOffset)
        String weekStr = request.getParameter("weekOffset");
        int weekOffset = 0;
        try {
            if (weekStr != null && !weekStr.isEmpty()) {
                weekOffset = Integer.parseInt(weekStr);
            }
        } catch (NumberFormatException e) {
            weekOffset = 0; // Tránh lỗi nếu ai đó gõ chữ vào URL
        }

        // 3. Gọi Service lấy Ma trận lịch
        ShiftService shiftService = new ShiftService();
        ShiftDTO[][] schedule = shiftService.getScheduleMatrix(email, role, weekOffset);

        // 4. Đẩy dữ liệu sang JSP
        request.setAttribute("userSchedule", schedule);
        request.setAttribute("currentWeekOffset", weekOffset);

        // 5. Tự động rẽ nhánh dựa trên role
        String path = "";
        if ("doctor".equals(role)) {
            path = "/component/doctor/contents/doctorShift.jsp"; 
        } else if ("staff".equals(role)) {
            path = "/component/staff/StaffShifts.jsp";  
        } else {
            // Phòng hờ nếu Admin hoặc Patient bấm nhầm vào URL này
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.getRequestDispatcher(path).forward(request, response);
    }
}