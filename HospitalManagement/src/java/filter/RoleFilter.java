package filter;

import entity.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class RoleFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            chain.doFilter(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        
        // ĐÃ FIX LỖI Ở ĐÂY: Thêm .trim() để xóa khoảng trắng thừa từ Database
        String role = user.getRole().trim();

        // ADMIN
        if (url.contains("/component/admin/") && !role.equalsIgnoreCase("admin")) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // STAFF
        if (url.contains("/component/staff/") && !role.equalsIgnoreCase("staff")) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // DOCTOR
        if (url.contains("/component/doctor/") && !role.equalsIgnoreCase("doctor")) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // PATIENT
        if (url.contains("/component/patient/") && !role.equalsIgnoreCase("patient")) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

}