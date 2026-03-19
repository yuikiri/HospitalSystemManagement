package controller;

import dao.RoomDTO;
import dao.ShiftDAO;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet xử lý AJAX lấy danh sách phòng còn trống theo khung giờ
 * @author Lenovo
 */
@WebServlet(name = "GetAvailableRoomsController", urlPatterns = {"/layphongtrong"})
public class GetAvailableRoomsController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // 1. Lấy thông tin thời gian từ Web gửi lên
            int day = Integer.parseInt(request.getParameter("day"));
            int shift = Integer.parseInt(request.getParameter("shift"));
            int week = Integer.parseInt(request.getParameter("week"));

            // 2. Tính toán Timestamp bắt đầu ca trực (Logic đồng bộ với ShiftService)
            LocalDate targetDate = LocalDate.now()
                    .with(DayOfWeek.MONDAY)
                    .plusWeeks(week)
                    .plusDays(day - 2);
            
            int startHour = 6 + (shift - 1) * 2;
            Timestamp startTime = Timestamp.valueOf(targetDate.atTime(startHour, 0));

            // 3. Gọi DAO lấy danh sách phòng chưa bị ai đặt vào giờ này
            ShiftDAO shiftDAO = new ShiftDAO();
            List<RoomDTO> rooms = shiftDAO.getAvailableRooms(startTime, startTime);
            
            // 4. Trả về đoạn mã HTML <option> để JavaScript nhét vào Dropdown
            StringBuilder html = new StringBuilder("<option value=''>-- Chọn phòng trống --</option>");
            
            if (rooms.isEmpty()) {
                html = new StringBuilder("<option value=''>❌ Không còn phòng trống</option>");
            } else {
                for (RoomDTO r : rooms) {
                    html.append("<option value='").append(r.getId()).append("'>")
                        .append("Phòng ").append(r.getRoomNumber())
                        .append(" - ").append(r.getDepartmentName() != null ? r.getDepartmentName() : "Chưa phân khoa")
                        .append("</option>");
                }
            }
            
            response.getWriter().write(html.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<option value=''>Lỗi tải dữ liệu</option>");
        }
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
        return "Controller phục vụ lấy phòng trống qua AJAX";
    }
}