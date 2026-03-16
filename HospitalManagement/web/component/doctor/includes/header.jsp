<%-- 
    Document   : header
    Created on : Mar 15, 2026, 8:40:45 PM
    Author     : Dang Khoa
--%>

<%@page import="entity.User"%>
<%@page import="dao.DoctorDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) request.getAttribute("user");
    DoctorDTO doctor = (DoctorDTO) request.getAttribute("doctor");
    String avatarChar = (doctor != null && doctor.getName() != null && !doctor.getName().isEmpty()) 
                        ? doctor.getName().substring(0, 1).toUpperCase() : "D";
%>
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h4 class="fw-bold mb-0">Chào mừng, BS. <%= (doctor != null && doctor.getName() != null) ? doctor.getName() : user.getUserName() %>! 👋</h4>
        <p class="text-muted small">Chúc bạn một ngày làm việc hiệu quả tại MediCare.</p>
    </div>
    <div class="bg-white p-2 rounded-pill shadow-sm px-3 d-flex align-items-center">
        <i class="bi bi-bell text-muted me-3"></i>
        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 35px; height: 35px;">
            <%= avatarChar %>
        </div>
        <span class="fw-bold small">Bác sĩ</span>
    </div>
</div>
