/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.MedicalHistoryDTO;
import dao.PrescriptionItemDTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
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
public class LoadMedicalHistoryController extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            int patientId = -1;
            String sqlFindPatient = "SELECT id FROM Patients WHERE userId = ?";
            try (Connection conn = new DbUtils().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlFindPatient)) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) patientId = rs.getInt("id");
            }
            if (patientId == -1) patientId = 42; // Mượn ID test nếu chưa có

            // GỌI SERVICE XỬ LÝ (Rất chuẩn MVC)
            AppointmentService service = new AppointmentService();
            List<MedicalHistoryDTO> waitingList = service.getProcessedMedicalHistory(patientId, "waiting");
            List<MedicalHistoryDTO> completedList = service.getProcessedMedicalHistory(patientId, "completed");

            // Ném dữ liệu sang JSP
            request.setAttribute("waitingList", waitingList);
            request.setAttribute("completedList", completedList);
            
            request.getRequestDispatcher("/component/patient/contents/medicalHistory.jsp").forward(request, response);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
