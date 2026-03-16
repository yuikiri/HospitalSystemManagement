package controller;

import entity.User;
import service.UserService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Để tránh lỗi 404 khi redirect, ta lấy ContextPath (ví dụ: /HospitalManagement)
        String cp = request.getContextPath();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            response.sendRedirect(cp + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        UserService userService = new UserService();

        // Nếu không có action, mặc định vào Dashboard
        if (action == null || action.isEmpty()) {
            request.getRequestDispatcher("/component/admin/adminDashboard.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
   case "users": {
    String query = request.getParameter("searchQuery");
    String role = request.getParameter("role");
    String statusParam = request.getParameter("statusFilter");
    
    if (role == null || role.isEmpty()) role = "doctor";
    
    // Mặc định là 1 (Hoạt động), nếu chọn "Tất cả" thì truyền -99
    int statusFilter = (statusParam == null || statusParam.isEmpty()) ? 1 : Integer.parseInt(statusParam);

    List<User> list = userService.searchUsers(query, statusFilter, role);

    request.setAttribute("userList", list);
    request.setAttribute("activeTab", role.toLowerCase());
    request.setAttribute("currentStatus", statusFilter); // Gửi lại để UI giữ giá trị select
    request.getRequestDispatcher("/component/admin/manageUsers.jsp").forward(request, response);
    break;

}
  case "updateStatus": {
    int userId = Integer.parseInt(request.getParameter("id"));
    int newStatus = Integer.parseInt(request.getParameter("newStatus"));
    String currentRole = request.getParameter("role");
    
    // Lấy thêm cái này để giữ đúng filter hiện tại
    String statusFilter = request.getParameter("statusFilter"); 
    if (statusFilter == null) statusFilter = "1"; 

    // Thực hiện cập nhật trong DB
    userService.updateUserStatus(userId, newStatus);
    
    // TRƯỚC ĐÂY: Bạn redirect thiếu statusFilter nên nó nhảy về mặc định (Hoạt động)
    // BÂY GIỜ: Redirect kèm theo statusFilter để nó ở lại đúng trang đó
    response.sendRedirect("AdminController?action=users&role=" + currentRole + "&statusFilter=" + statusFilter);
    break;

}
                case "deleteUser":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    String rDel = request.getParameter("role");
                    userService.deleteUser(idDel);
                    // Dùng CP để tránh 404 sau khi redirect
                    response.sendRedirect(cp + "/AdminController?action=users&role=" + rDel);
                    break;

                    case "trash": {

    List<User> list = userService.getDeletedUsers();

    request.setAttribute("userList", list);
    request.setAttribute("activeTab", "trash");

    request.getRequestDispatcher("/component/admin/manageUsers.jsp").forward(request, response);

    break;
}

case "restoreUser": {

    int id = Integer.parseInt(request.getParameter("id"));

    userService.restoreUser(id);

    response.sendRedirect("AdminController?action=trash");

    break;
}
                case "editUser":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    User u = userService.getUserById(idEdit); 
                    request.setAttribute("user", u);
                    request.getRequestDispatcher("/component/admin/editUser.jsp").forward(request, response);
                    break;

                default:
                    response.sendRedirect(cp + "/AdminController");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(cp + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String cp = request.getContextPath();
        String action = request.getParameter("action");
        UserService userService = new UserService();

        try {
            if ("addUser".equals(action)) {
                String name = request.getParameter("userName");
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                String role = request.getParameter("role");

                userService.addUserByAdmin(name, email, pass, role);
                response.sendRedirect(cp + "/AdminController?action=users&role=" + role);

            } else if ("updateUser".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("userName");
                String email = request.getParameter("email");
                String role = request.getParameter("role"); 

                userService.updateUserByAdmin(id, name, email, role);
                response.sendRedirect(cp + "/AdminController?action=users&role=" + role);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(cp + "/AdminController?action=users");
        }
    }
}