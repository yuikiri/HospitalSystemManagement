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
        // 1. Lấy thông tin từ Web
        int day = Integer.parseInt(request.getParameter("day"));
        int shift = Integer.parseInt(request.getParameter("shift"));
        int week = Integer.parseInt(request.getParameter("week"));

        // 2. SỬ DỤNG ShiftService ĐỂ TÍNH THỜI GIAN (Đảm bảo đồng bộ 100%)
        service.ShiftService shiftService = new service.ShiftService();
        Timestamp[] range = shiftService.calculateShiftRange(day, shift, week);
        Timestamp startTime = range[0];
        Timestamp endTime = range[1];

        // 3. Gọi DAO với đầy đủ Start và End
        dao.ShiftDAO shiftDAO = new dao.ShiftDAO();
        // Truyền cả startTime và endTime vào đây
        List<dao.RoomDTO> rooms = shiftDAO.getAvailableRooms(startTime, endTime); 
        
        // 4. Trả về HTML (Giữ nguyên đoạn StringBuilder bên dưới của bạn)
        StringBuilder html = new StringBuilder("<option value=''>-- Chọn phòng trống --</option>");
        if (rooms.isEmpty()) {
            html = new StringBuilder("<option value=''>❌ Không còn phòng trống</option>");
        } else {
            for (dao.RoomDTO r : rooms) {
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