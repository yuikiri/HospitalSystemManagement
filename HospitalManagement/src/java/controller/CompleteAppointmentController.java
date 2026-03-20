/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.MedicineDTO;
import dao.PaymentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.MedicineService;
import service.PrescriptionItemService;

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
            // 1. Nhận thông tin từ form Modal
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String diagnosis = request.getParameter("diagnosis");
            String notes = request.getParameter("notes");
            
            // NHẬN TIỀN CÔNG KHÁM VÀ SỐ NGÀY LƯU BỆNH
            double doctorFee = Double.parseDouble(request.getParameter("doctorFee"));
            int roomDays = Integer.parseInt(request.getParameter("roomDays"));
            double roomPricePerDay = 200000; // Tiền giường mặc định 200k/ngày (Sếp có thể thay đổi)

            AppointmentDAO appDAO = new AppointmentDAO();
            MedicineService medService = new MedicineService();
            PrescriptionItemService itemService = new PrescriptionItemService();
            PaymentDAO payDAO = new PaymentDAO();

            // 2. Cập nhật Bệnh Án
            int medicalRecordId = appDAO.updateAndGetMedicalRecordId(appointmentId, diagnosis, notes);

            if (medicalRecordId != -1) {
                // 3. Cập nhật trạng thái Đơn thuốc
                int prescriptionId = appDAO.updateAndGetPrescriptionId(medicalRecordId, "active");

                // 4. Kê đơn & Tính tiền thuốc
                double totalMedicinePrice = 0;
                String[] selectedMedIds = request.getParameterValues("medIds");

                if (selectedMedIds != null && prescriptionId != -1) {
                    for (String medIdStr : selectedMedIds) {
                        int medId = Integer.parseInt(medIdStr);
                        int qty = Integer.parseInt(request.getParameter("medQty_" + medId));
                        String dosage = request.getParameter("medDosage_" + medId);
                        String freq = request.getParameter("medFreq_" + medId);
                        String duration = request.getParameter("medDuration_" + medId);

                        itemService.addMedicineToPrescription(prescriptionId, medId, qty, dosage, freq, duration);
                        
                        MedicineDTO med = medService.getById(medId);
                        if (med != null) {
                            totalMedicinePrice += (med.getPrice() * qty);
                            // NẾU SẾP MUỐN TRỪ TỒN KHO THÌ MỞ DÒNG DƯỚI RA:
                            // medService.dispenseMedicine(medId, qty);
                        }
                    }
                }

                // 5. Tính TỔNG TIỀN & Xuất hóa đơn
                // Tổng = Tiền Khám + (Tiền Giường * Số Ngày) + Tiền Thuốc
                double finalTotalAmount = doctorFee + (roomDays * roomPricePerDay) + totalMedicinePrice;
                payDAO.insertPayment(medicalRecordId, finalTotalAmount); 

                appDAO.updateAppointmentStatus(appointmentId, "completed");
                session.setAttribute("successMessage", "Khám & Kê đơn hoàn tất! Đã xuất hóa đơn: " + String.format("%,.0f", finalTotalAmount) + " VNĐ.");
            } else {
                session.setAttribute("errorMessage", "Lỗi: Không thể lưu Bệnh án.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Hệ thống gặp sự cố trong lúc kê đơn!");
        }

        response.sendRedirect(request.getContextPath() + "/component/doctor/doctorDashboard.jsp");
    }
}
