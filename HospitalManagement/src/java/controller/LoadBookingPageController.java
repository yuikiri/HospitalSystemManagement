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
            // 1. GỌI DỮ LIỆU TỪ SERVICE (Giống hệt cách sếp làm ở Test01)
            DepartmentService deptService = new DepartmentService();
            RoomService roomService = new RoomService();

            // Lấy tất cả Khoa (hoặc sếp dùng getClinicalDepartments() tùy ý sếp)
            List<DepartmentDTO> departmentList = deptService.getClinicalDepartments();
            List<RoomDTO> roomList = roomService.getActiveList();

            // 2. NÉM DỮ LIỆU SANG JSP
            request.setAttribute("departmentList", departmentList);
            request.setAttribute("roomList", roomList);

            // 3. CHUYỂN HƯỚNG VỀ FILE GIAO DIỆN
            // 🚨 LƯU Ý QUAN TRỌNG: Nếu sếp bị lỗi 404 (Not Found), hãy chắc chắn 
            // đường dẫn này khớp với cây thư mục thực tế của sếp.
            // VD: "/component/patient/contents/bookAppointment.jsp" hoặc "/patient/contents/bookAppointment.jsp"
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
