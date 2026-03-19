/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yuikiri
 */
public class MainController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

        // KHA BÁO ĐỊA CHỈ CỦA CÁC CONTROLLER CON
    private static final String ERROR_PAGE = "index.jsp";
    
    private static final String LOGIN = "LoginController";
    private static final String REGISTER = "RegisterController";
    private static final String LOGOUT = "LogoutController";
    
    private static final String LOAD_PATIENT_DASHBOARD = "LoadPatientDashboardController";
    private static final String LOAD_BOOKING_PAGE = "LoadBookingPageController";
    private static final String LOAD_MEDICAL_HISTORY = "LoadMedicalHistoryController";
    
    private static final String SUBMIT_BOOKING = "SubmitBookingController";
    private static final String CANCEL_APPOINTMENT = "CancelAppointmentController";
    private static final String CONFIRM_PAYMENT = "ConfirmPaymentController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");

            if (action == null) {
                url = ERROR_PAGE;
            } 
            else if (action.equals("Login")) url = LOGIN;
            else if (action.equals("Register")) url = REGISTER;
            else if (action.equals("Logout")) url = LOGOUT;
            else if (action.equals("LoadPatientDashboard")) url = LOAD_PATIENT_DASHBOARD;
            else if (action.equals("LoadBookingPage")) url = LOAD_BOOKING_PAGE;
            else if (action.equals("SubmitBooking")) url = SUBMIT_BOOKING;
            else if (action.equals("LoadMedicalHistory")) url = LOAD_MEDICAL_HISTORY;
            else if (action.equals("CancelAppointment")) url = CANCEL_APPOINTMENT;
            else if (action.equals("ConfirmPayment")) url = CONFIRM_PAYMENT;
            
            // API TRẢ VỀ TRẠNG THÁI CHO JAVASCRIPT AUTO-RELOAD
            else if (action.equals("CheckPaymentStatus")) {
                int appId = Integer.parseInt(request.getParameter("appointmentId"));
                String status = new dao.PaymentDAO().getPaymentStatusByAppointmentId(appId);
                
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(status);
                return; // Thoát ngang, không forward
            }
            else {
                request.setAttribute("errorMessage", "Action không được hỗ trợ!");
            }
        } catch (Exception e) {
            log("Lỗi tại MainController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        return "Main Controller - Front Controller Pattern";
    }// </editor-fold>

}
