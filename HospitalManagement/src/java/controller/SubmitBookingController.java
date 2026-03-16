/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.RoomDAO;
import dao.RoomDTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.AppointmentService;
import service.RoomService;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class SubmitBookingController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        // 1. Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/patientDashboard.jsp"); 
            return;
        }

        try {
            // ========================================================
            // 0. TỰ ĐỘNG DỌN RÁC (Hủy lịch quá 7 ngày chưa khám)
            // ========================================================
            AppointmentDAO appDAO = new AppointmentDAO();
            appDAO.autoCancelExpiredAppointments();

            // 2. Tìm ID bệnh nhân (patientId) từ bảng Patients
            int patientId = -1;
            String sqlFindPatient = "SELECT id FROM Patients WHERE userId = ?";
            try (Connection conn = new DbUtils().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlFindPatient)) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    patientId = rs.getInt("id");
                }
            }
            
            // Mượn tạm ID Bệnh nhân nếu đang test bằng tài khoản Admin/Chưa có patientId
            if (patientId == -1) {
                patientId = 42; 
            }

            // 3. Lấy dữ liệu từ Form gửi lên
            String bookingDate = request.getParameter("bookingDate"); 
            String bookingTime = request.getParameter("bookingTime"); 
            String deptIdStr = request.getParameter("departmentId"); 
            String roomIdStr = request.getParameter("roomId");       

            int finalRoomId = -1;

            // ========================================================
            // 4. LOGIC XỬ LÝ "KHÔNG CHỌN KHOA / PHÒNG" 
            // ========================================================
            if (roomIdStr != null && !roomIdStr.trim().isEmpty()) {
                finalRoomId = Integer.parseInt(roomIdStr);
            } else {
                RoomDAO roomDAO = new RoomDAO();
                List<RoomDTO> allRooms = roomDAO.getAllActiveRoomsAvailable();

                if (deptIdStr != null && !deptIdStr.trim().isEmpty()) {
                    int deptId = Integer.parseInt(deptIdStr);
                    for (RoomDTO r : allRooms) {
                        if (r.getDepartmentId() == deptId && r.getPrice() == 0.0) {
                            finalRoomId = r.getId();
                            break;
                        }
                    }
                } else {
                    for (RoomDTO r : allRooms) {
                        if (r.getPrice() == 0.0) {
                            finalRoomId = r.getId();
                            break;
                        }
                    }
                }
            }

            if (finalRoomId == -1) {
                session.setAttribute("errorMessage", "Hệ thống chưa có phòng Khám Mặc Định. Vui lòng liên hệ Admin!");
                // ĐÃ FIX ĐƯỜNG DẪN TẠI ĐÂY
                response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
                return;
            }

            // 5. Nối ngày và giờ thành Timestamp
            Timestamp startTime = Timestamp.valueOf(bookingDate + " " + bookingTime);
            Timestamp endTime = new Timestamp(startTime.getTime() + (2 * 60 * 60 * 1000));

            // =========================================================================
            // 6. LUỒNG ĐỒNG BỘ Y KHOA: LƯU LỊCH HẸN -> LẤY ID -> TẠO BỆNH ÁN -> TẠO ĐƠN THUỐC
            // =========================================================================
            int newAppId = appDAO.insertAppointmentAndGetId(patientId, finalRoomId, startTime, endTime);

            if (newAppId > 0) {
                int newMrId = appDAO.createEmptyMedicalRecordAndGetId(newAppId);
                if (newMrId > 0) {
                    appDAO.createEmptyPrescription(newMrId);
                }
                session.setAttribute("successMessage", "Đặt lịch thành công! Hệ thống đã tạo sẵn Hồ sơ bệnh án chờ Bác sĩ.");
            } else {
                session.setAttribute("errorMessage", "Lưu Đặt lịch thất bại. Vui lòng kiểm tra lại dữ liệu!");
            }
            
            // ĐÃ FIX ĐƯỜNG DẪN TẠI ĐÂY
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Hệ thống đang bận hoặc dữ liệu không hợp lệ. Vui lòng thử lại!");
            // ĐÃ FIX ĐƯỜNG DẪN TẠI ĐÂY
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
        }
    }
}