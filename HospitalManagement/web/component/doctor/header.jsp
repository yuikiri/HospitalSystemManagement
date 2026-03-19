<%@page import="entity.User"%>
<%@page import="dao.DoctorDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Lấy thông tin từ session
    User user = (User) session.getAttribute("user");
    DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor");
    
    // Tạo chữ cái đầu cho Avatar
    String doctorName = (doctor != null && doctor.getName() != null) ? doctor.getName() : user.getUserName();
    String avatarChar = doctorName.substring(0, 1).toUpperCase();
    
    // Lấy chức vụ nếu có
    String position = (doctor != null && doctor.getPosition() != null) ? doctor.getPosition() : "Bác sĩ chuyên khoa";
%>

<div class="d-flex justify-content-between align-items-center h-100 px-4">
    <div>
        <h4 class="fw-bold mb-1" style="color: var(--text-main);">Chào mừng, BS. <%= doctorName %>! 👋</h4>
        <p class="text-muted small mb-0">Hệ thống quản lý dành riêng cho Y Bác sĩ.</p>
    </div>
    
    <div class="d-flex align-items-center">
        <div class="position-relative me-4" style="cursor: pointer;">
            <i class="fas fa-bell text-muted fs-5"></i>
            <span class="position-absolute top-0 start-100 translate-middle p-1 bg-danger border border-light rounded-circle">
                <span class="visually-hidden">New alerts</span>
            </span>
        </div>
        
        <div class="bg-white p-1 pe-3 rounded-pill shadow-sm d-flex align-items-center border">
            <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2 fw-bold shadow-sm" 
                 style="width: 38px; height: 38px; font-size: 1.1rem;">
                <%= avatarChar %>
            </div>
            <div>
                <span class="fw-bold d-block text-dark" style="font-size: 0.9rem; line-height: 1.2;"><%= doctorName %></span>
                <span class="text-muted" style="font-size: 0.75rem;"><%= position %></span>
            </div>
        </div>
    </div>
</div>