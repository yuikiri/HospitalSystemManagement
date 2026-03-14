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
        } // ==========================================
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

                response.sendRedirect(redirectUrl);

            } catch (Exception e) {
                e.printStackTrace();
                // Bắt lỗi trùng số điện thoại và báo lên UI
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(redirectUrl);
            }
            return; // Dừng luồng Patient tại đây
        } // ==========================================
        // RẼ NHÁNH 3: LUỒNG CỦA DOCTOR 
        // ==========================================
        else if (role.equals("doctor")) {
            // Dashboard gộp nên chuyển hướng về chính nó
            String redirectUrl = "doctordashboard.jsp";

            try {
                // 1. Lấy dữ liệu từ Request (Khớp với các name trong thẻ input của Dashboard)
                // Lưu ý: DoctorDTO của bạn dùng doctorId, ta lấy từ đối tượng DoctorDTO đang lưu trong session
                dao.DoctorDTO currentDoctor = (dao.DoctorDTO) session.getAttribute("user");
                int doctorId = currentDoctor.getId();

                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String position = request.getParameter("position");
                String licenseNumber = request.getParameter("licenseNumber");

                // Xử lý gender (mặc định 1 nếu form không gửi hoặc lỗi)
                int gender = 1;
                try {
                    gender = Integer.parseInt(request.getParameter("gender"));
                } catch (Exception e) {
                    /* giữ mặc định */ }

                // 2. Gọi Service xử lý (Đúng theo cấu trúc DoctorService của bạn)
                service.DoctorService doctorService = new service.DoctorService();
                doctorService.updateProfile(doctorId, name, gender, position, phone, licenseNumber);

                // 3. Cập nhật lại session mới nhất sau khi lưu thành công
                dao.DoctorDTO updatedDoctor = doctorService.getProfileByUserId(currentDoctor.getUserId());
                session.setAttribute("user", updatedDoctor);

                // Thông báo thành công (tùy chọn)
                session.setAttribute("successMessage", "Cập nhật hồ sơ thành công!");
                response.sendRedirect(redirectUrl);

            } catch (util.ErrorMessages.AppException e) {
                // Bắt lỗi nghiệp vụ (trùng SĐT, trống dữ liệu...) từ DoctorService
                e.printStackTrace();
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(redirectUrl);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", "Hệ thống gặp sự cố: " + e.getMessage());
                response.sendRedirect(redirectUrl);
            }
            return;
        }

        // Nếu không thuộc Role nào thì đá về trang chủ
        response.sendRedirect("index.jsp");
    }

}
