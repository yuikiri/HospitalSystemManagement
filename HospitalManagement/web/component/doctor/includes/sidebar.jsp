<%-- 
    Document   : sidebar
    Created on : Mar 15, 2026, 8:39:25 PM
    Author     : Dang Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% String current = (String) request.getAttribute("currentPage"); %>
<nav class="sidebar d-flex flex-column">
    <div class="logo-area">
        <i class="bi bi-heart-pulse-fill"></i> MediCare
    </div>
    <div class="nav flex-column mt-4 px-2">
        <a href="doctorDashboard.jsp?page=profile" class="nav-link <%= "profile".equals(current) ? "active" : "" %>">
            <i class="bi bi-person-vcard me-2"></i> Thông tin cá nhân
        </a>
        <a href="doctorDashboard.jsp?page=shift" class="nav-link <%= "shift".equals(current) ? "active" : "" %>">
            <i class="bi bi-calendar-event me-2"></i> Ca trực
        </a>
        <a href="doctorDashboard.jsp?page=history" class="nav-link <%= "history".equals(current) ? "active" : "" %>">
            <i class="bi bi-clock-history me-2"></i> Lịch sử khám bệnh
        </a>
        <div style="margin-top: 100px;">
            <hr class="mx-3 text-muted">
            <a href="../../LogoutController" class="nav-link text-danger">
                <i class="bi bi-box-arrow-left me-2"></i> Đăng xuất
            </a>
        </div>
    </div>
</nav>