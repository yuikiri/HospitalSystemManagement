/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import service.UserService;

/**
 *
 * @author Yuikiri
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // Giới hạn ảnh 5MB
@WebServlet(name = "UpdateAvatarController", urlPatterns = {"/UpdateAvatarController"})
// Phải có dòng này thì mới nhận được file upload!

public class UpdateAvatarController extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            // 1. Lấy file ảnh từ Form
            Part filePart = request.getPart("avatarFile");
            
            if (filePart != null && filePart.getSize() > 0) {
                // 2. Đọc file ảnh thành mảng Byte (byte[])
                InputStream fileContent = filePart.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = fileContent.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] fileBytes = buffer.toByteArray();

                // 3. Mã hóa mảng Byte thành chuỗi Base64
                String base64String = Base64.getEncoder().encodeToString(fileBytes);
                
                // 4. Nối thêm tiền tố để Trình duyệt hiểu đây là ảnh
                String mimeType = filePart.getContentType(); // VD: image/jpeg
                String base64ImageURI = "data:" + mimeType + ";base64," + base64String;

                // 5. Lưu chuỗi dài này vào Database
                UserDAO userDAO = new UserDAO();
                boolean isUpdated = userDAO.updateAvatar(currentUser.getId(), base64ImageURI);

                if (isUpdated) {
                    // Cập nhật lại Session để giao diện đổi ảnh ngay lập tức
                    currentUser.setAvatarUrl(base64ImageURI);
                    session.setAttribute("user", currentUser);
                    session.setAttribute("successMessage", "Cập nhật ảnh đại diện thành công!");
                } else {
                    session.setAttribute("errorMessage", "Lỗi khi lưu ảnh vào cơ sở dữ liệu.");
                }
            } else {
                session.setAttribute("errorMessage", "Bạn chưa chọn ảnh nào!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "File quá lớn hoặc không đúng định dạng!");
        }

        // ĐÁ VỀ TRANG CHỦ CỦA USER ĐÓ (Dùng JavaScript History để load lại)
        String role = currentUser.getRole().toLowerCase();
        if (role.equals("doctor")) {
            response.sendRedirect(request.getContextPath() + "/component/doctor/doctorDashboard.jsp");
        } else if (role.equals("patient")) {
            response.sendRedirect(request.getContextPath() + "/LoadPatientDashboardController");
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    
    }

}
