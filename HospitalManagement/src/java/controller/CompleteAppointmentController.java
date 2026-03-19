/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.PaymentDAO;
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
public class CompleteAppointmentController extends HttpServlet {

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
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String diagnosis = request.getParameter("diagnosis");
            String notes = request.getParameter("notes");
            int roomDays = Integer.parseInt(request.getParameter("roomDays"));
            double doctorFee = Double.parseDouble(request.getParameter("doctorFee"));

            AppointmentDAO appDAO = new AppointmentDAO();
            PaymentDAO payDAO = new PaymentDAO();

            // 1. Cập nhật Bệnh Án
            int medicalRecordId = appDAO.updateAndGetMedicalRecordId(appointmentId, diagnosis, notes);

            if (medicalRecordId != -1) {
                // 2. Kích hoạt Đơn thuốc
                int prescriptionId = appDAO.updateAndGetPrescriptionId(medicalRecordId, "active");

                // 3. Xử lý Thuốc (Nếu sếp có check box medIds)
                String[] selectedMedIds = request.getParameterValues("medIds");
                double totalMedicinePrice = 0;
                if (selectedMedIds != null && prescriptionId != -1) {
                    for (String medIdStr : selectedMedIds) {
                        int medId = Integer.parseInt(medIdStr);
                        int qty = Integer.parseInt(request.getParameter("medQty_" + medId));
                        String dosage = request.getParameter("medDosage_" + medId);
                        // TODO: INSERT vào bảng PrescriptionItems và cộng tiền thuốc
                    }
                }

                // 4. Tính tiền & Cập nhật Payment (Phòng + Thuốc + Công khám)
                double finalTotalAmount = (roomDays * 200000) + doctorFee + totalMedicinePrice;
                payDAO.insertPayment(medicalRecordId, finalTotalAmount); 

                // 5. Hoàn thành Lịch hẹn
                appDAO.updateAppointmentStatus(appointmentId, "completed");
                session.setAttribute("successMessage", "Hoàn thành ca khám và xuất Hóa đơn thành công!");
            } else {
                session.setAttribute("errorMessage", "Không tìm thấy hồ sơ Bệnh án!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lưu Bệnh án thất bại!");
        }
        response.sendRedirect(request.getContextPath() + "/component/doctor/doctorDashboard.jsp");
    }
}
