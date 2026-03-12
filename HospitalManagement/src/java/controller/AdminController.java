package controller;

import entity.User;
import service.UserService;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name="AdminController",urlPatterns={"/AdminController"})
public class AdminController extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        UserService userService = new UserService();

        try{

            if(action == null){
                request.getRequestDispatcher("component/admin/adminDashboard.jsp")
                        .forward(request,response);
                return;
            }

            switch(action){

                case "users":

                    String email = request.getParameter("email");
                    String isActive = request.getParameter("isActive");

                    List<User> list;

                    // nếu không search thì lấy tất cả user
                    if((email == null || email.isEmpty()) && 
                       (isActive == null || isActive.isEmpty())){

                        list = userService.getAllUsers();

                    }else{

                        list = userService.searchUsers(email, isActive);

                    }

                    request.setAttribute("userList", list);

                    request.getRequestDispatcher("component/admin/manageUsers.jsp")
                            .forward(request,response);

                    break;

                default:

                    request.getRequestDispatcher("component/admin/adminDashboard.jsp")
                            .forward(request,response);

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}