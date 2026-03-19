package controller;

import dao.DoctorDTO;
import dao.PatientDTO;
import dao.StaffDAO;
import dao.StaffDTO;
import entity.User;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import service.DoctorService;
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
        // 1. LUỒNG CỦA STAFF
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
                
                session.setAttribute("successMessage", "Cập nhật hồ sơ thành công!");
                // QUAY VỀ TRANG DASHBOARD CỦA STAFF
                response.sendRedirect("component/staff/staffDashboard.jsp");
            } else {
                session.setAttribute("errorMessage", "Cập nhật thất bại!");
                response.sendRedirect("component/staff/staffDashboard.jsp");
            }
            return;
        } 
        
        // ==========================================
        // 2. LUỒNG CỦA PATIENT
        // ==========================================
        else if (role.equals("patient")) {
            // QUAY VỀ TRANG DASHBOARD CỦA BỆNH NHÂN
            String redirectUrl = request.getContextPath() + "/LoadPatientDashboardController";
            try {
                int patientId = Integer.parseInt(request.getParameter("patientId"));
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                Date dob = Date.valueOf(request.getParameter("dob"));
                int gender = Integer.parseInt(request.getParameter("gender"));
                String address = request.getParameter("address");

                PatientService patientService = new PatientService();
                patientService.updatePatient(patientId, name, dob, gender, phone, address);

                PatientDTO updatedPatient = patientService.getPatientById(patientId);
                session.setAttribute("patient", updatedPatient);
                
                session.setAttribute("successMessage", "Cập nhật hồ sơ cá nhân thành công!");
                response.sendRedirect(redirectUrl);

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(redirectUrl);
            }
            return;
        } 
        
        // ==========================================
        // 3. LUỒNG CỦA DOCTOR
        // ==========================================
        else if (role.equals("doctor")) {
            // ĐÃ SỬA: QUAY VỀ TRANG DASHBOARD CỦA BÁC SĨ THAY VÌ MAIN CONTROLLER
            String redirectUrl = request.getContextPath() + "/component/doctor/doctorDashboard.jsp";
            
            try {
                int doctorId = Integer.parseInt(request.getParameter("doctorId"));
                int userId = currentUser.getId(); 

                String name = request.getParameter("name");
                int gender = Integer.parseInt(request.getParameter("gender"));
                String position = request.getParameter("position");
                String phone = request.getParameter("phone");
                String licenseNumber = request.getParameter("licenseNumber");

                DoctorService doctorService = new DoctorService();
                doctorService.updateDoctor(doctorId, name, gender, position, phone, licenseNumber);

                // Cập nhật lại Session để có tên Bác sĩ mới nhất
                DoctorDTO updatedDoctor = doctorService.getProfileByUserId(userId);
                session.setAttribute("doctor", updatedDoctor);

                session.setAttribute("successMessage", "Cập nhật hồ sơ thành công!");
                response.sendRedirect(redirectUrl); // Nhảy về Dashboard, JavaScript sẽ tự hiện cái bảng Profile lên

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(redirectUrl);
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

}