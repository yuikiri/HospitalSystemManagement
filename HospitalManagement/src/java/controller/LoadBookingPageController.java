/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AppointmentDAO;
import dao.DepartmentDAO;
import dao.DepartmentDTO;
import dao.RoomDAO;
import dao.RoomDTO;
import entity.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.DepartmentService;
import service.RoomService;
import util.DbUtils;

/**
 *
 * @author Yuikiri
 */
public class LoadBookingPageController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            // 1. Gọi dữ liệu từ Service
            DepartmentService deptService = new DepartmentService();
            RoomService roomService = new RoomService();

            List<DepartmentDTO> departmentList = deptService.getClinicalDepartments();
            List<RoomDTO> roomList = roomService.getActiveList();

            // 2. Ném dữ liệu sang JSP
            request.setAttribute("departmentList", departmentList);
            request.setAttribute("roomList", roomList);

            // 3. Chuyển hướng về file Giao diện
            request.getRequestDispatcher("/component/patient/contents/bookAppointment.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi Server: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
