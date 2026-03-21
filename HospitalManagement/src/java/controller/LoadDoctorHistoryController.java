package controller;

import dao.AppointmentDAO;
import dao.AppointmentDTO;
import dao.DoctorDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoadDoctorHistoryController", urlPatterns = {"/LoadDoctorHistoryController"})
public class LoadDoctorHistoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor");

        if (doctor != null) {
            AppointmentDAO appDAO = new AppointmentDAO();
            List<AppointmentDTO> historyList = appDAO.getCompletedAppointmentsByDoctor(doctor.getId());
            request.setAttribute("historyList", historyList);
        }

        request.getRequestDispatcher("/component/doctor/contents/history.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}