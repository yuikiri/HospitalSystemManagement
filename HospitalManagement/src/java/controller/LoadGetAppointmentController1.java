/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.DoctorDTO;
import dao.DoctorDepartmentDAO;
import dao.DoctorDepartmentDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.MedicineService;

/**
 *
 * @author Yuikiri
 */
public class LoadGetAppointmentController1 extends HttpServlet {

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
        DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor");

        if (doctor != null) {
            try {
                // 1. Lấy Khoa của Bác sĩ
                DoctorDepartmentDAO ddDAO = new DoctorDepartmentDAO();
                List<DoctorDepartmentDTO> deptList = ddDAO.getDepartmentsByDoctor(doctor.getId());
                int departmentId = deptList.isEmpty() ? -1 : deptList.get(0).getDepartmentId();

                // 2. Lấy danh sách bệnh nhân
                AppointmentDAO appDAO = new AppointmentDAO();
                request.setAttribute("pendingList", appDAO.getPendingAppointmentsByDept(departmentId));
                request.setAttribute("acceptedList", appDAO.getAcceptedAppointmentsByDoctor(doctor.getId()));

                // 3. Lấy danh sách THUỐC
                MedicineService medService = new MedicineService();
                request.setAttribute("medicineList", medService.getActiveList());

            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        }
        request.getRequestDispatcher("/component/doctor/contents/getAppointment.jsp").forward(request, response);
    }
}
