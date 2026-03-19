<%@page import="dao.ShiftDTO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    ShiftDTO[][] schedule = (ShiftDTO[][]) request.getAttribute("userSchedule");
    String[] shiftTimes = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "12:00-14:00", 
                           "14:00-16:00", "16:00-18:00", "18:00-20:00", "20:00-22:00"};
%>

<style>
    :root {
        --primary: #059669; /* Xanh ngọc lục bảo sang trọng */
        --primary-light: #d1fae5;
        --surface: #ffffff;
        --background: #f8fafc;
        --text-main: #1e293b;
        --text-muted: #64748b;
        --border-color: #e2e8f0;
    }

    body {
        background-color: var(--background);
    }

    /* Container chính */
    .schedule-premium-wrapper {
        font-family: 'Inter', 'Segoe UI', Roboto, sans-serif;
        background: var(--surface);
        border-radius: 24px;
        box-shadow: 0 10px 40px -10px rgba(0, 0, 0, 0.08);
        padding: 35px;
        margin: 20px auto;
        max-width: 100%;
        border: 1px solid rgba(255, 255, 255, 0.5);
    }

    /* --- Khu vực Header & Nút Quay Lại --- */
    .top-actions {
        margin-bottom: 25px;
    }

    .btn-back {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        color: var(--text-muted);
        text-decoration: none;
        font-weight: 600;
        font-size: 0.95rem;
        padding: 8px 16px;
        border-radius: 10px;
        background: var(--background);
        transition: all 0.3s ease;
    }

    .btn-back:hover {
        background: #e2e8f0;
        color: var(--text-main);
        transform: translateX(-3px);
    }

    .schedule-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        margin-bottom: 30px;
        padding-bottom: 25px;
        border-bottom: 2px dashed var(--border-color);
    }

    .header-title {
        margin: 0;
        font-size: 1.5rem;
        font-weight: 800;
        color: var(--text-main);
        letter-spacing: -0.5px;
    }

    .header-subtitle {
        margin: 8px 0 0 0;
        font-size: 0.9rem;
        color: var(--text-muted);
        display: flex;
        align-items: center;
        gap: 5px;
    }

    /* --- Nút Điều Hướng Tuần --- */
    .week-navigation {
        display: inline-flex;
        background-color: var(--background);
        border-radius: 12px;
        padding: 6px;
        box-shadow: inset 0 2px 4px rgba(0,0,0,0.02);
    }

    .week-navigation a {
        text-decoration: none;
        color: var(--text-muted);
        font-weight: 600;
        font-size: 0.95rem;
        padding: 10px 24px;
        border-radius: 8px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .week-navigation a:hover:not(.active) {
        background-color: rgba(226, 232, 240, 0.5);
        color: var(--text-main);
    }

    .week-navigation a.active {
        background-color: var(--primary);
        color: white;
        box-shadow: 0 4px 12px rgba(5, 150, 105, 0.25);
    }

    /* --- Bảng Lịch Trực --- */
    .table-schedule {
        width: 100%;
        border-collapse: separate;
        border-spacing: 12px; 
        table-layout: fixed;
    }

    .table-schedule th {
        text-align: center;
        color: var(--text-muted);
        font-weight: 700;
        font-size: 0.85rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        padding-bottom: 5px;
        border: none;
    }

    .table-schedule th:first-child {
        width: 140px; 
    }

    /* Cột Thời Gian */
    .col-time {
        background: linear-gradient(145deg, #ffffff, #f8fafc);
        border: 1px solid var(--border-color);
        border-radius: 14px;
        text-align: center;
        vertical-align: middle;
        padding: 15px 10px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.01);
    }

    .shift-name {
        display: block;
        font-weight: 800;
        color: var(--primary);
        font-size: 1.05rem;
        margin-bottom: 4px;
    }

    .shift-time {
        font-size: 0.75rem;
        color: var(--text-muted);
        font-weight: 600;
        background: var(--background);
        padding: 3px 8px;
        border-radius: 6px;
    }

    /* Ô Trống */
    .cell-empty {
        border: 2px dashed #cbd5e1;
        border-radius: 14px;
        height: 85px;
        text-align: center;
        vertical-align: middle;
        background: transparent;
        transition: all 0.3s ease;
        cursor: pointer;
    }

    .cell-empty i {
        color: #cbd5e1;
        font-size: 1.5rem;
        transition: transform 0.3s ease;
    }

    .cell-empty:hover {
        background: #f1f5f9;
        border-color: #94a3b8;
    }

    .cell-empty:hover i {
        transform: scale(1.2) rotate(90deg);
        color: #94a3b8;
    }

    /* Ô Có Lịch Trực */
    .cell-active {
        background: #ffffff;
        border: 1px solid var(--border-color);
        border-left: 5px solid var(--primary); /* Điểm nhấn viền trái */
        border-radius: 14px;
        text-align: left;
        vertical-align: middle;
        padding: 12px 15px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.03);
        transition: all 0.3s ease;
    }

    .cell-active:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(5, 150, 105, 0.12);
        border-color: var(--primary-light);
    }

    .dept-name {
        font-weight: 700;
        color: var(--text-main);
        font-size: 0.9rem;
        margin-bottom: 8px;
        line-height: 1.3;
    }

    .room-label {
        font-size: 0.75rem;
        font-weight: 700;
        background-color: var(--primary-light);
        color: var(--primary);
        padding: 4px 10px;
        border-radius: 8px;
        display: inline-flex;
        align-items: center;
        gap: 4px;
    }

    .shift-note {
        font-size: 0.75rem;
        color: var(--text-muted);
        margin-top: 8px;
        font-style: italic;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
</style>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<div class="schedule-premium-wrapper">
    <div class="top-actions">
        <a href="staffDashboard.jsp" class="btn-back">
            <i class="fas fa-arrow-left"></i> Quay lại Dashboard
        </a>
    </div>

    <div class="schedule-header">
        <div>
            <h4 class="header-title">Lịch Trực Cá Nhân</h4>
            <p class="header-subtitle">
                <i class="fas fa-clock text-success"></i> Đồng bộ dữ liệu theo thời gian thực
            </p>
        </div>
        
        <div class="week-navigation">
            <a href="MySchedule?weekOffset=${currentWeekOffset - 1}" class="${currentWeekOffset < 0 ? 'active' : ''}">
                <i class="fas fa-chevron-left"></i> Tuần trước
            </a>
            <a href="MySchedule?weekOffset=0" class="${currentWeekOffset == 0 ? 'active' : ''}">
                <i class="fas fa-calendar-day"></i> Hiện tại
            </a>
            <a href="MySchedule?weekOffset=${currentWeekOffset + 1}" class="${currentWeekOffset > 0 ? 'active' : ''}">
                Tuần sau <i class="fas fa-chevron-right"></i>
            </a>
        </div>
    </div>

    <div style="overflow-x: auto; padding-bottom: 10px;">
        <table class="table-schedule">
            <thead>
                <tr>
                    <th>Ca Trực</th>
                    <th>Thứ 2</th>
                    <th>Thứ 3</th>
                    <th>Thứ 4</th>
                    <th>Thứ 5</th>
                    <th>Thứ 6</th>
                    <th>Thứ 7</th>
                    <th>Chủ Nhật</th>
                </tr>
            </thead>
            <tbody>
                <% for(int r=0; r<8; r++) { %>
                <tr>
                    <td class="col-time">
                        <span class="shift-name">Ca <%= r + 1 %></span>
                        <span class="shift-time"><i class="far fa-clock"></i> <%= shiftTimes[r] %></span>
                    </td>

                    <% for(int c=0; c<7; c++) { 
                        ShiftDTO s = (schedule != null) ? schedule[r][c] : null; 
                    %>
                        <% if(s != null) { %>
                            <td class="cell-active">
                                <div class="dept-name"><%= s.getDepartmentName() %></div>
                                <div>
                                    <span class="room-label">
                                        <i class="fas fa-door-open"></i> P.<%= s.getRoomNumber() %>
                                    </span>
                                </div>
                                <% if(s.getNote() != null && !s.getNote().isEmpty()) { %>
                                    <div class="shift-note" title="<%= s.getNote() %>"><%= s.getNote() %></div>
                                <% } %>
                            </td>
                        <% } else { %>
                            <td class="cell-empty">
                                <i class="fas fa-plus"></i>
                            </td>
                        <% } %>
                    <% } %>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>