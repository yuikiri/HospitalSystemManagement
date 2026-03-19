<%@page import="dao.ShiftDTO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    ShiftDTO[][] schedule = (ShiftDTO[][]) request.getAttribute("userSchedule");
    String[] shiftTimes = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "12:00-14:00", 
                           "14:00-16:00", "16:00-18:00", "18:00-20:00", "20:00-22:00"};
%>

<style>
    @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&display=swap');

    :root {
        --primary: #10b981; /* Emerald Green */
        --primary-dark: #059669;
        --primary-soft: rgba(16, 185, 129, 0.1);
        --surface: #ffffff;
        --background: #f1f5f9;
        --text-main: #0f172a;
        --text-muted: #64748b;
        --border-color: #e2e8f0;
    }

    body {
        background-color: var(--background);
        font-family: 'Outfit', sans-serif;
        color: var(--text-main);
    }

    /* Animation vào trang */
    @keyframes fadeInUp {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .schedule-premium-wrapper {
        background: var(--surface);
        border-radius: 30px;
        box-shadow: 0 20px 50px rgba(0, 0, 0, 0.05);
        padding: 40px;
        margin: 30px auto;
        max-width: 95%;
        border: 1px solid rgba(255, 255, 255, 0.8);
        animation: fadeInUp 0.6s ease-out;
    }

    /* --- Back Button Decor --- */
    .top-actions { margin-bottom: 30px; }
    .btn-back {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        color: var(--text-muted);
        text-decoration: none;
        font-weight: 600;
        font-size: 0.9rem;
        padding: 10px 20px;
        border-radius: 15px;
        background: #fff;
        border: 1px solid var(--border-color);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .btn-back:hover {
        background: var(--text-main);
        color: #fff;
        transform: translateX(-5px);
        box-shadow: 0 10px 20px rgba(0,0,0,0.1);
    }

    /* --- Header & Nav Decor --- */
    .schedule-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 40px;
        flex-wrap: wrap;
        gap: 20px;
    }

    .header-title { font-size: 1.8rem; font-weight: 800; color: var(--text-main); margin: 0; }
    
    .week-navigation {
        display: flex;
        background: #f8fafc;
        padding: 8px;
        border-radius: 20px;
        border: 1px solid var(--border-color);
    }

    .week-navigation a {
        text-decoration: none;
        color: var(--text-muted);
        font-weight: 600;
        padding: 10px 25px;
        border-radius: 14px;
        transition: 0.3s;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .week-navigation a.active {
        background: var(--primary);
        color: white;
        box-shadow: 0 10px 20px rgba(16, 185, 129, 0.2);
    }

    /* --- Table Decor --- */
    .table-schedule {
        width: 100%;
        border-collapse: separate;
        border-spacing: 12px;
    }

    .table-schedule th {
        text-align: center;
        color: var(--text-muted);
        font-weight: 700;
        font-size: 0.8rem;
        text-transform: uppercase;
        letter-spacing: 1.5px;
        padding-bottom: 10px;
    }

    /* Cột Thời Gian dọc */
    .col-time {
        background: #fff;
        border: 1px solid var(--border-color);
        border-radius: 20px;
        padding: 20px 10px;
        width: 130px;
        transition: 0.3s;
    }
    
    .col-time:hover { border-color: var(--primary); background: var(--primary-soft); }

    .shift-name { display: block; font-weight: 800; color: var(--primary-dark); font-size: 1rem; }
    .shift-time { font-size: 0.75rem; color: var(--text-muted); font-weight: 600; }

    /* Ô Lịch Trực (Premium Look) */
    .cell-active {
        background: #fff;
        border: 1px solid var(--border-color);
        border-top: 6px solid var(--primary); /* Đổi sang viền trên cho hiện đại */
        border-radius: 20px;
        padding: 18px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.02);
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }

    .cell-active:hover {
        transform: scale(1.05) translateY(-5px);
        box-shadow: 0 20px 30px rgba(0,0,0,0.08);
        border-top-width: 10px;
    }

    .dept-name { font-weight: 800; font-size: 0.95rem; color: var(--text-main); margin-bottom: 10px; }
    
    .room-badge {
        background: var(--primary-soft);
        color: var(--primary-dark);
        font-size: 0.75rem;
        font-weight: 700;
        padding: 6px 12px;
        border-radius: 10px;
        display: inline-block;
    }

    /* Ô Trống (Minimalist) */
    .cell-empty {
        border: 2px dashed #e2e8f0;
        border-radius: 20px;
        height: 100px;
        transition: 0.3s;
        opacity: 0.5;
    }
    .cell-empty:hover { background: #fff; border-style: solid; opacity: 1; border-color: var(--primary-light); }
</style>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<div class="schedule-premium-wrapper">
    <div class="top-actions">
        <a href="${pageContext.request.contextPath}/component/staff/staffDashboard.jsp" class="btn-back">
            <i class="fas fa-chevron-left"></i> Quay lại Dashboard
        </a>
    </div>

    <div class="schedule-header">
        <div>
            <h4 class="header-title">Lịch Trực Tuần Hiện Tại</h4>
            <p class="text-muted mb-0"><i class="fas fa-check-circle text-success me-1"></i> Đã đồng bộ với hệ thống bệnh viện</p>
        </div>
        
        <div class="week-navigation">
            <a href="MySchedule?weekOffset=${currentWeekOffset - 1}" class="${currentWeekOffset < 0 ? 'active' : ''}">
                <i class="fas fa-arrow-left"></i>
            </a>
            <a href="MySchedule?weekOffset=0" class="${currentWeekOffset == 0 ? 'active' : ''}">
                Tuần này
            </a>
            <a href="MySchedule?weekOffset=${currentWeekOffset + 1}" class="${currentWeekOffset > 0 ? 'active' : ''}">
                <i class="fas fa-arrow-right"></i>
            </a>
        </div>
    </div>

    <div class="table-responsive" style="border-radius: 20px;">
        <table class="table-schedule">
            <thead>
                <tr>
                    <th></th> <th>Thứ 2</th><th>Thứ 3</th><th>Thứ 4</th><th>Thứ 5</th><th>Thứ 6</th><th>Thứ 7</th><th>CN</th>
                </tr>
            </thead>
            <tbody>
                <% for(int r=0; r<8; r++) { %>
                <tr>
                    <td class="col-time">
                        <span class="shift-name">Ca <%= r + 1 %></span>
                        <span class="shift-time"><%= shiftTimes[r] %></span>
                    </td>

                    <% for(int c=0; c<7; c++) { 
                        ShiftDTO s = (schedule != null) ? schedule[r][c] : null; 
                    %>
                        <% if(s != null) { %>
                            <td class="cell-active">
                                <div class="dept-name text-uppercase"><%= s.getDepartmentName() %></div>
                                <div class="room-badge">
                                    <i class="fas fa-map-marker-alt me-1"></i> Phòng <%= s.getRoomNumber() %>
                                </div>
                                <% if(s.getNote() != null && !s.getNote().isEmpty()) { %>
                                    <div class="text-muted mt-2" style="font-size: 0.7rem; font-style: italic;">
                                        <i class="fas fa-info-circle"></i> <%= s.getNote() %>
                                    </div>
                                <% } %>
                            </td>
                        <% } else { %>
                            <td class="cell-empty d-flex align-items-center justify-content-center">
                                <i class="fas fa-plus-circle text-muted opacity-25"></i>
                            </td>
                        <% } %>
                    <% } %>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>