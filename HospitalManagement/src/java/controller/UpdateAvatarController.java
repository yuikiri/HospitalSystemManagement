/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
@WebServlet(name = "UpdateAvatarController", urlPatterns = {"/UpdateAvatarController"})
// Phải có dòng này thì mới nhận được file upload!
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
             maxFileSize = 1024 * 1024 * 5,      // Tối đa 5MB
             maxRequestSize = 1024 * 1024 * 10)  // Tối đa 10MB/Request
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
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // 1. Lấy file ảnh từ form gửi lên (name="avatarFile")
            Part filePart = request.getPart("avatarFile");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Nếu người dùng có chọn file
            if (fileName != null && !fileName.isEmpty()) {
                
                // Đổi tên file để không bị trùng (Ví dụ: avatar_BN_45_1638218.jpg)
                String uniqueFileName = "avatar_user_" + currentUser.getId() + "_" + System.currentTimeMillis() + ".jpg";
                
                // 2. Định nghĩa thư mục lưu ảnh (Nằm trong thư mục /uploads/ của webapp)
                // Lưu ý: Sếp phải TẠO THỦ CÔNG một thư mục tên là "uploads" nằm ngang hàng với thư mục "component" nhé.
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir(); // Nếu chưa có thư mục thì tự động tạo
                }
                
                // 3. Lưu file vào ổ cứng Server
                String savePath = uploadPath + File.separator + uniqueFileName;
                filePart.write(savePath);
                
                // 4. Đường dẫn sẽ lưu vào DB (Đường dẫn tương đối: "uploads/ten-file.jpg")
                String dbAvatarUrl = "uploads/" + uniqueFileName;
                
                // 5. Cập nhật vào DB
                UserService userService = new UserService();
                userService.updateAvatar(currentUser.getId(), dbAvatarUrl);
                
                // 6. Cập nhật lại Session để UI đổi ảnh ngay lập tức
                currentUser.setAvatarUrl(dbAvatarUrl);
                session.setAttribute("user", currentUser);
            }
            
            response.sendRedirect("component/patient/patientDashboard.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("component/patient/patientDashboard.jsp");
        }
    }

}
