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

@WebServlet(name="AdminController", urlPatterns={"/AdminController"})
public class AdminController extends HttpServlet {

protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if(session == null || session.getAttribute("LOGIN_USER") == null){
        response.sendRedirect("index.jsp");
        return;
    }

    String action = request.getParameter("action");

    if(action == null){
        request.getRequestDispatcher("component/admin/adminDashboard.jsp")
               .forward(request, response);
        return;
    }
}
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    String action = request.getParameter("action");

    UserService userService = new UserService();

    try {

        if (action == null) {
            request.getRequestDispatcher("component/admin/adminDashboard.jsp")
                    .forward(request, response);
            return;
        }

        switch (action) {

            case "users":

                String email = request.getParameter("email");
                String isActive = request.getParameter("isActive");

                List<User> list;

                if ((email == null || email.isEmpty()) &&
                    (isActive == null || isActive.isEmpty())) {

                    list = userService.getAllUsers();

                } else {

                    list = userService.searchUsers(email, isActive);

                }

                request.setAttribute("userList", list);

                request.getRequestDispatcher("component/admin/manageUsers.jsp")
                        .forward(request, response);

                break;


            case "toggleUser":

                int id = Integer.parseInt(request.getParameter("id"));

                userService.toggleUserStatus(id);

                response.sendRedirect("AdminController?action=users");

                break;


            case "deleteUser":

                id = Integer.parseInt(request.getParameter("id"));

                userService.deleteUser(id);

                response.sendRedirect("AdminController?action=users");

                break;


            case "trashUsers":

                List<User> trashList = userService.getDeletedUsers();

                request.setAttribute("userList", trashList);

                request.getRequestDispatcher("component/admin/trashUsers.jsp")
                        .forward(request, response);

                break;


            case "restoreUser":

                id = Integer.parseInt(request.getParameter("id"));

                userService.restoreUser(id);

                response.sendRedirect("AdminController?action=trashUsers");

                break;


            default:

                request.getRequestDispatcher("component/admin/adminDashboard.jsp")
                        .forward(request, response);

        }
        

    } catch (Exception e) {
        e.printStackTrace();
    }
}


}
