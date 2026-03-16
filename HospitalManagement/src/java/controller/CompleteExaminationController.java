package controller;

import dao.*;
import entity.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import service.AppointmentService;
import util.DbUtils;
import util.ErrorMessages;

@WebServlet("/CompleteExaminationController")
public class CompleteExaminationController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        // 1. Khai báo các biến tổng quát bên ngoài để tránh lỗi Scope
        double totalMedicineCost = 0; 
        double totalAmount = 0;

        try {
            // Lấy dữ liệu từ Form gửi lên
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String diagnosis = request.getParameter("diagnosis");
            String notes = request.getParameter("notes");
            
            // Giả định giá phòng cơ bản từ loại phòng, nếu không có hãy để mặc định 0 hoặc lấy từ DB
            double roomPriceBase = Double.parseDouble(request.getParameter("roomPriceBase") != null ? request.getParameter("roomPriceBase") : "0");
            int roomMultiplier = Integer.parseInt(request.getParameter("roomMultiplier") != null ? request.getParameter("roomMultiplier") : "1");
            
            // 2. Tạo Bệnh án (Medical Record)
            MedicalRecordDAO mrDAO = new MedicalRecordDAO();
            boolean isRecordCreated = mrDAO.insertMedicalRecord(appointmentId, diagnosis, notes);
            
            if (!isRecordCreated) {
                throw new ErrorMessages.AppException(ErrorMessages.SYSTEM_ERROR);
            }
            
            // 3. Xử lý trừ kho và tính tiền thuốc
            MedicineDAO medDAO = new MedicineDAO();
            String[] selectedMedicines = request.getParameterValues("medicineIds");
            String[] quantities = request.getParameterValues("quantities");

            if (selectedMedicines != null) {
                // Sửa lỗi .size() thành .length cho mảng
                for (int i = 0; i < selectedMedicines.length; i++) {
                    int medId = Integer.parseInt(selectedMedicines[i]);
                    int qty = Integer.parseInt(quantities[i]);
                    
                    MedicineDTO med = medDAO.getMedicineById(medId);
                    if (med != null) {
                        totalMedicineCost += (med.getPrice() * qty);
                        // Trừ kho thuốc trong DB
                        medDAO.updateStock(medId, -qty);
                    }
                }
            }

            // 4. Tính toán tổng hóa đơn
            totalAmount = (roomPriceBase * roomMultiplier) + totalMedicineCost;
            
            // 5. Lưu vào bảng Payments (Sử dụng JDBC trực tiếp vì chưa có PaymentDAO)
            MedicalRecordDTO newRecord = mrDAO.getByAppointmentId(appointmentId);
            if (newRecord != null) {
                String sqlPay = "INSERT INTO Payments (medicalRecordId, totalAmount, paymentMethod, status, isActive) VALUES (?, ?, 'cash', 'unpaid', 1)";
                try (Connection conn = DbUtils.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sqlPay)) {
                    ps.setInt(1, newRecord.getId());
                    ps.setDouble(2, totalAmount);
                    ps.executeUpdate();
                }
            }
            
            int roomId = Integer.parseInt(request.getParameter("roomId"));

            // HỨNG TIỀN CÔNG BÁC SĨ NHẬP
            double doctorFee = Double.parseDouble(request.getParameter("doctorFee"));

            // Hứng list thuốc (Logic mảng getParameterValues...)
             List<PrescriptionItemDTO> meds = null;///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                AppointmentService service = new AppointmentService();
            boolean success = service.completeExaminationWithFee(appointmentId, roomId, diagnosis, notes, doctorFee, meds);

            if(success) {
                // Chuyển hướng báo thành công
            }

            // 6. Cập nhật trạng thái lịch hẹn
            AppointmentDAO appDAO = new AppointmentDAO();
            appDAO.updateAppointmentStatus(appointmentId, "completed");

            session.setAttribute("successMessage", "Đã hoàn thành khám! Tổng hóa đơn: " + String.format("%,.0f", totalAmount) + " VNĐ");
            response.sendRedirect("doctorDashboard.jsp");

        } catch (ErrorMessages.AppException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("doctorDashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Hệ thống gặp sự cố: " + e.getMessage());
            response.sendRedirect("doctorDashboard.jsp");
        }
    }
}