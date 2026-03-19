/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
public class ConfirmPaymentController extends HttpServlet {

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

        // Kiểm tra bảo mật cơ bản (nếu chưa đăng nhập thì văng ra)
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/MainController");
            return;
        }

        try {
            // 1. Lấy dữ liệu từ form JSP (Thông qua MainController đẩy sang)
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String paymentMethod = request.getParameter("paymentMethod"); // Giá trị là 'banking'

            // 2. Gọi DAO để update trạng thái
            PaymentDAO payDAO = new PaymentDAO();

            // ⚠️ LƯU Ý: Sử dụng hàm markAsPaidByAppointmentId 
            // (Hàm này tôi đã gửi cho sếp ở phần code PaymentDAO lúc nãy để update xuyên qua Bệnh án)
            boolean success = payDAO.markAsPaidByAppointmentId(appointmentId, paymentMethod);

            if (success) {
                session.setAttribute("successMessage", "Thanh toán thành công! Hệ thống đã ghi nhận viện phí.");
            } else {
                session.setAttribute("errorMessage", "Lỗi xác nhận thanh toán! Vui lòng báo cho Quầy Thu Ngân.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Hệ thống đang bận hoặc dữ liệu lỗi. Vui lòng thử lại sau.");
        }

        // 3. CHUẨN MỚI: ĐÁ VỀ MAIN CONTROLLER ĐỂ LOAD LẠI DASHBOARD (Tuyệt đối không bị rớt CSS)
        response.sendRedirect(request.getContextPath() + "/MainController?action=LoadPatientDashboard");
    }

    // Đề phòng trường hợp gọi nhầm GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/MainController?action=LoadPatientDashboard");
    }
}
