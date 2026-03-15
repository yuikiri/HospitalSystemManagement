package controller;

import dao.PatientDTO;
import dao.StaffDAO;
import dao.StaffDTO;
import entity.User;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import service.PatientService;
@WebServlet("/UpdateProfileController1")
public class UpdateProfileController1 extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String role = currentUser.getRole().toLowerCase().trim();

        // ==========================================
        // RẼ NHÁNH 1: LUỒNG CỦA STAFF (Giữ nguyên 100% code gốc của người làm Staff)
        // ==========================================
        if (role.equals("staff")) {
            String userIdRaw = request.getParameter("userId");

            if (userIdRaw == null) {
                response.sendRedirect("index.jsp");
                return;
            }

            int userId = Integer.parseInt(userIdRaw);
            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String position = request.getParameter("position");
            int gender = Integer.parseInt(request.getParameter("gender"));

            StaffDAO dao = new StaffDAO();
            boolean check = dao.updateProfile(userId, email, name, phone, position, gender);

            if (check) {
                StaffDTO staff = dao.getStaffByUserId(userId);
                session.setAttribute("staff", staff);
                
                currentUser.setEmail(email);
                session.setAttribute("user", currentUser);

                response.sendRedirect("component/staff/staffDashboard.jsp");
            } else {
                response.sendRedirect("updateProfile.jsp");
            }
            return; // Dừng luôn luồng Staff tại đây
        }

        // ==========================================
        // ///////////////////Hoàng patient
        // ==========================================
        else if (role.equals("patient")) {
            String redirectUrl = "component/patient/patientDashboard.jsp";
            try {
                int patientId = Integer.parseInt(request.getParameter("patientId"));
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                Date dob = Date.valueOf(request.getParameter("dob"));
                int gender = Integer.parseInt(request.getParameter("gender"));
                String address = request.getParameter("address");

                // Gọi Service xử lý
                PatientService patientService = new PatientService();
                patientService.updatePatient(patientId, name, dob, gender, phone, address);

                // Cập nhật lại session mới nhất cho Patient
                PatientDTO updatedPatient = patientService.getPatientById(patientId);
                session.setAttribute("patient", updatedPatient);

                session.setAttribute("successMessage", "Cập nhật hồ sơ cá nhân thành công!");
                response.sendRedirect(redirectUrl);

            } catch (Exception e) {
                e.printStackTrace();
                // Bắt lỗi trùng số điện thoại và báo lên UI
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(redirectUrl);
            }
            return; // Dừng luồng Patient tại đây
        }

        // ==========================================
        // RẼ NHÁNH 3: LUỒNG CỦA DOCTOR 
        // ==========================================
        else if (role.equals("doctor")) {
            // Nhường đất cho bạn code phần Doctor
            // response.sendRedirect("component/doctor/doctorDashboard.jsp");
            return;
        }
        
        // Nếu không thuộc Role nào thì đá về trang chủ
        response.sendRedirect("index.jsp");
    }


}
