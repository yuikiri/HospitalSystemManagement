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

        if (session.getAttribute("user") == null) {
            response.getWriter().write("error_login");
            return;
        }

        try {
            // 1. Lấy ID Bệnh án
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            
            // 2. Ép cứng phương thức thanh toán là 'banking' (khỏi cần truyền từ JSP)
            String paymentMethod = "banking"; 

            // 3. Gọi DAO cập nhật
            PaymentDAO payDAO = new PaymentDAO();
            boolean success = payDAO.markAsPaidByAppointmentId(appointmentId, paymentMethod);

            // 4. Trả về kết quả ngầm cho Javascript
            response.setContentType("text/plain");
            response.getWriter().write(success ? "success" : "error");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error");
        }
    }
}
