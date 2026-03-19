/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yuikiri
 */
public class LoadGetAppointmentController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor");

        if (doctor != null) {
            try {
                // 1. Lấy ID Khoa của Bác sĩ này (Giả sử lấy khoa đầu tiên bác sĩ đang trực)
                DoctorDepartmentDAO ddDAO = new DoctorDepartmentDAO();
                List<DoctorDepartmentDTO> deptList = ddDAO.getDepartmentsByDoctor(doctor.getId());
                
                int departmentId = -1;
                if (!deptList.isEmpty()) {
                    departmentId = deptList.get(0).getDepartmentId();
                }

                // 2. Gọi AppointmentDAO lấy Lịch chờ (Tab 1) và Lịch đang khám (Tab 2)
                AppointmentDAO appDAO = new AppointmentDAO();
                List<AppointmentDTO> pendingList = appDAO.getPendingAppointmentsByDept(departmentId);
                List<AppointmentDTO> acceptedList = appDAO.getAcceptedAppointmentsByDoctor(doctor.getId());
                
                // 3. Lấy Danh sách Thuốc cho vào Modal kê đơn
                // MỞ ĐOẠN NÀY RA NẾU SẾP ĐÃ VIẾT MedicineDAO NHÉ
                /*
                MedicineDAO medDAO = new MedicineDAO();
                List<MedicineDTO> medicineList = medDAO.getAllActiveMedicines();
                request.setAttribute("medicineList", medicineList);
                */

                // 4. Gắn vào request để ném sang View
                request.setAttribute("pendingList", pendingList);
                request.setAttribute("acceptedList", acceptedList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 5. Đá sang JSP để nó đắp dữ liệu vào HTML (Fetch API sẽ nhận cục HTML này)
        request.getRequestDispatcher("/component/doctor/contents/getAppointment.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
