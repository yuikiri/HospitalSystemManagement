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
import javax.servlet.http.HttpSession;

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
    // KHOAI BÁO TRANG LỖI HOẶC TRANG CHỦ MẶC ĐỊNH
    // KHOAI BÁO TRANG LỖI HOẶC TRANG CHỦ MẶC ĐỊNH
    private static final String ERROR_PAGE = "index.jsp";

    // --- AUTH (Xác thực) ---
    private static final String LOGIN = "LoginController";
    private static final String REGISTER = "RegisterController";
    private static final String LOGOUT = "LogoutController";

    // --- PATIENT (Bệnh nhân) ---
    private static final String LOAD_PATIENT_DASHBOARD = "LoadPatientDashboardController";
    private static final String LOAD_BOOKING_PAGE = "LoadBookingPageController";
    private static final String LOAD_MEDICAL_HISTORY = "LoadMedicalHistoryController";
    private static final String SUBMIT_BOOKING = "SubmitBookingController";
    private static final String CANCEL_APPOINTMENT = "CancelAppointmentController";
    private static final String CONFIRM_PAYMENT = "ConfirmPaymentController";

    // --- DOCTOR (Bác sĩ) ---
    private static final String LOAD_DOCTOR_PROFILE = "LoadDoctorProfileController";
    private static final String LOAD_GET_APPOINTMENT = "LoadGetAppointmentController";
    private static final String ACCEPT_APPOINTMENT = "AcceptAppointmentController";
    private static final String COMPLETE_APPOINTMENT = "CompleteAppointmentController";
    private static final String LOAD_DOCTOR_HISTORY = "LoadDoctorHistoryController"; // <--- THÊM DÒNG NÀY

    // --- STAFF (Nhân viên) - Tôi tạo sẵn khung để sếp làm sau ---
    private static final String LOAD_STAFF_DASHBOARD = "LoadStaffDashboardController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. CẤM TRÌNH DUYỆT LƯU CACHE (CHỐNG NÚT BACK THẦN THÁNH)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String url = ERROR_PAGE;

        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "";
            }

            // ==============================================================
            // 2. TRẠM GÁC BẢO MẬT (KIỂM TRA ĐĂNG NHẬP)
            // ==============================================================
            HttpSession session = request.getSession(false);
            boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

            // Action cho phép khách vãng lai đi qua (Chưa đăng nhập vẫn vô được)
            boolean isPublicAction = action.equals("Login") || action.equals("Register") || action.equals("");

            // NẾU CHƯA ĐĂNG NHẬP MÀ ĐÒI VÀO TRANG TRONG -> ĐÁ VỀ INDEX TỨC KHẮC
            if (!isLoggedIn && !isPublicAction) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return; // Thoát ngang luồng ngay lập tức
            }
            // ==============================================================

            // 3. PHÂN LUỒNG ACTION VÀO TỪNG CONTROLLER CON
            if (action.equals("")) {
                url = ERROR_PAGE;
            } // [NHÓM XÁC THỰC]
            else if (action.equals("Login")) {
                url = LOGIN;
            } else if (action.equals("Register")) {
                url = REGISTER;
            } else if (action.equals("Logout")) {
                url = LOGOUT;
            } // [NHÓM BỆNH NHÂN]
            else if (action.equals("LoadPatientDashboard")) {
                url = LOAD_PATIENT_DASHBOARD;
            } else if (action.equals("LoadBookingPage")) {
                url = LOAD_BOOKING_PAGE;
            } else if (action.equals("SubmitBooking")) {
                url = SUBMIT_BOOKING;
            } else if (action.equals("LoadMedicalHistory")) {
                url = LOAD_MEDICAL_HISTORY;
            } else if (action.equals("CancelAppointment")) {
                url = CANCEL_APPOINTMENT;
            } else if (action.equals("ConfirmPayment")) {
                url = CONFIRM_PAYMENT;
            } // [NHÓM BÁC SĨ]
            else if (action.equals("LoadDoctorProfile")) {
                url = LOAD_DOCTOR_PROFILE;
            } else if (action.equals("LoadGetAppointment")) {
                url = LOAD_GET_APPOINTMENT;
            } else if (action.equals("AcceptAppointment")) {
                url = ACCEPT_APPOINTMENT;
            } else if (action.equals("CompleteAppointment")) {
                url = COMPLETE_APPOINTMENT;
            } // Shift và History chưa có logic DB phức tạp nên tạm trỏ thẳng vào JSP
            else if (action.equals("LoadDoctorShift")) {
                url = "/component/doctor/contents/shift.jsp";
            } else if (action.equals("LoadDoctorHistory")) {
                url = LOAD_DOCTOR_HISTORY;;
            } // [NHÓM NHÂN VIÊN] - Tạo sẵn
            else if (action.equals("LoadStaffDashboard")) {
                url = LOAD_STAFF_DASHBOARD;
            } // [NHÓM API TRẢ VỀ TEXT CHO JAVASCRIPT / WEBHOOK]
            else if (action.equals("CheckPaymentStatus")) {
                int appId = Integer.parseInt(request.getParameter("appointmentId"));
                String status = new dao.PaymentDAO().getPaymentStatusByAppointmentId(appId);

                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(status);
                return; // Trả về text xong là ngắt luôn, không chuyển trang (forward) nữa
            } else {
                request.setAttribute("errorMessage", "Action không được hỗ trợ hoặc bị sai tên!");
            }

        } catch (Exception e) {
            log("Lỗi tại MainController: " + e.toString());
            request.setAttribute("errorMessage", "Hệ thống đang bận. Vui lòng thử lại!");
        } finally {
            // 4. CHUYỂN HƯỚNG TỚI ĐÍCH ĐẾN CUỐI CÙNG
            if (!response.isCommitted()) {
                request.getRequestDispatcher(url).forward(request, response);
            }
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
