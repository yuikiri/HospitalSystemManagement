/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.DoctorDTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yuikiri
 */
public class AcceptAppointmentController extends HttpServlet {

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
        DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor");

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            boolean success = new AppointmentDAO().acceptAppointment(appointmentId, doctor.getId());

            if (success) {
                session.setAttribute("successMessage", "Đã nhận ca thành công!");
            } else {
                session.setAttribute("errorMessage", "Nhận ca thất bại (Lịch đã bị hủy hoặc có người nhận rồi)!");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống!");
        }
        
        // Trở về Dashboard (Vì JS sẽ tự giữ nguyên tab Get Appointment)
        response.sendRedirect(request.getContextPath() + "/component/doctor/doctorDashboard.jsp");
    }
}
