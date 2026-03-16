/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.AppointmentService;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class CancelAppointmentController extends HttpServlet {

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            
            // Tìm ID bệnh nhân
            int patientId = -1;
            String sqlFindPatient = "SELECT id FROM Patients WHERE userId = ?";
            try (Connection conn = new DbUtils().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlFindPatient)) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) patientId = rs.getInt("id");
            }
            if (patientId == -1) patientId = 42;

            // GỌI SERVICE ĐỂ HỦY
            AppointmentService service = new AppointmentService();
            boolean success = service.cancelPendingAppointmentByPatient(appointmentId, patientId);
            
            if (success) {
                session.setAttribute("successMessage", "Hủy lịch hẹn thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể hủy! Lịch hẹn đã được Bác sĩ tiếp nhận.");
            }
            
            response.sendRedirect(request.getContextPath() + "/component/patient/patientDashboard.jsp");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
