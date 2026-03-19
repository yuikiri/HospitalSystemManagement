/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PaymentDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yuikiri
 */
public class SePayWebhookController extends HttpServlet {

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
        try {
            // 1. Đọc cục dữ liệu JSON do SePay bắn về
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String payload = sb.toString();

            // 2. LẤY NỘI DUNG CHUYỂN KHOẢN (Mã Lịch hẹn)
            // Fix linh hoạt: Không phân biệt hoa thường, lỡ khách ghi "THANHTOAN_41" vẫn nhận được
            Pattern patternContent = Pattern.compile("ThanhToan_(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcherContent = patternContent.matcher(payload);

            // 3. LẤY SỐ TIỀN THỰC TẾ KHÁCH CHUYỂN (Chìa khóa nằm ở chữ "transferAmount" của SePay)
            Pattern patternAmount = Pattern.compile("\"transferAmount\"\\s*:\\s*(\\d+)");
            Matcher matcherAmount = patternAmount.matcher(payload);

            // 4. KIỂM TRA VÀ ĐỐI CHIẾU
            if (matcherContent.find() && matcherAmount.find()) {
                int appointmentId = Integer.parseInt(matcherContent.group(1)); // Lấy ID Lịch hẹn
                double transferAmount = Double.parseDouble(matcherAmount.group(1)); // Lấy Tiền khách chuyển

                PaymentDAO payDAO = new PaymentDAO();
                
                // Lấy số tiền GỐC cần thu từ Database
                double requiredAmount = payDAO.getRequiredAmountByAppointmentId(appointmentId);

                if (requiredAmount == -1) {
                    System.out.println("❌ SePay Webhook: Không tìm thấy hóa đơn hợp lệ cho Lịch hẹn #" + appointmentId);
                } 
                else if (transferAmount >= requiredAmount) {
                    // 🎉 ĐỦ TIỀN HOẶC DƯ TIỀN -> GẠCH NỢ THÀNH CÔNG
                    boolean isUpdated = payDAO.markAsPaidByAppointmentId(appointmentId, "banking");
                    if (isUpdated) {
                        System.out.println("✅ TING TING! Gạch nợ thành công Lịch hẹn #" + appointmentId + ". Số tiền nhận: " + transferAmount + " VNĐ");
                    }
                } 
                else {
                    // ⚠️ THIẾU TIỀN -> CẢNH BÁO, KHÔNG GẠCH NỢ
                    System.out.println("CẢNH BÁO GIAN LẬN: Lịch hẹn #" + appointmentId + 
                                       " cần thu " + requiredAmount + " VNĐ, nhưng khách chỉ chuyển " + transferAmount + " VNĐ. Đã TỪ CHỐI gạch nợ!");
                }
            } else {
                System.out.println("SePay Webhook: Giao dịch không chứa mã ThanhToan hoặc không xác định được số tiền.");
            }

            // 5. Trả về 200 OK cho SePay
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
