package controller;

import dao.MedicineDTO;
import service.MedicineService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Test01Controller", urlPatterns = {"/Test01Controller"})
public class test01 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            // 1. Gọi Service lấy thuốc
            MedicineService medService = new MedicineService();
            List<MedicineDTO> list = medService.getActiveList();
            
            // 2. In thẳng ra màn hình Console của NetBeans để theo dõi
            System.out.println("=====================================");
            System.out.println("TEST 01 - SỐ LƯỢNG THUỐC: " + list.size());
            System.out.println("=====================================");

            // 3. Ném sang trang test
            request.setAttribute("testList", list);
            request.getRequestDispatcher("test01.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h2>LỖI CRASH RỒI SẾP ƠI: " + e.getMessage() + "</h2>");
        }
    }
}