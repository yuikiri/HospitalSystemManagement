/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DoctorDAO;
import dao.DoctorDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yuikiri
 */
public class GetDoctorsAPI extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            int departmentId = Integer.parseInt(request.getParameter("departmentId"));
            String date = request.getParameter("date"); // YYYY-MM-DD
            String time = request.getParameter("time"); // HH:mm:ss
            
            // Xây dựng Timestamp bắt đầu
            String startStr = date + " " + time;
            Timestamp bookingStart = Timestamp.valueOf(startStr);
            
            // Tính Timestamp kết thúc (Mặc định mỗi lượt khám là 2 tiếng như trong HTML của sếp)
            long twoHoursInMillis = 2 * 60 * 60 * 1000;
            Timestamp bookingEnd = new Timestamp(bookingStart.getTime() + twoHoursInMillis);
            
            // Gọi DAO tìm Bác sĩ
            DoctorDAO doctorDAO = new DoctorDAO();
            List<DoctorDTO> doctors = doctorDAO.getAvailableDoctors(departmentId, bookingStart, bookingEnd);
            
            // Chuyển List thành JSON (Tự build để khỏi cài thư viện ngoài)
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < doctors.size(); i++) {
                DoctorDTO d = doctors.get(i);
                json.append("{\"id\":").append(d.getId())
                    .append(", \"name\":\"").append(d.getName()).append("\"}");
                if (i < doctors.size() - 1) json.append(",");
            }
            json.append("]");
            
            out.print(json.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]"); // Lỗi thì trả về mảng rỗng
        }
    }
}
