<%@page import="dao.ShiftDTO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    ShiftDTO[][] schedule = (ShiftDTO[][]) request.getAttribute("userSchedule");
    String[] shiftNames = {"Ca 1", "Ca 2", "Ca 3", "Ca 4", "Ca 5", "Ca 6", "Ca 7", "Ca 8"};
    String[] shiftTimes = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "12:00-14:00", 
                           "14:00-16:00", "16:00-18:00", "18:00-20:00", "20:00-22:00"};
    String[] daysOfWeek = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN"};
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&display=swap');

        :root {
            --emerald-main: #10b981; 
            --emerald-subtle: #d1fae5; 
            --emerald-dark: #059669;
            --info-subtle: #f8fafc; 
            --info-color: #475569;
            --surface: #ffffff;
            --background: #f1f5f9;
        }

        body {
            background-color: var(--background);
            font-family: 'Outfit', sans-serif;
        }

        /* Animation vào trang */
        .fade-in { animation: fadeInUp 0.5s ease-out; }
        @keyframes fadeInUp { from { opacity: 0; transform: translateY(15px); } to { opacity: 1; transform: translateY(0); } }

        .schedule-premium-wrapper {
            background: var(--surface);
            border-radius: 24px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.04);
            padding: 30px;
            margin: 30px auto;
            max-width: 96%;
        }

        /* --- Header & Nav --- */
        .btn-back {
            display: inline-flex; align-items: center; gap: 8px; color: #64748b;
            text-decoration: none; font-weight: 600; font-size: 0.9rem; padding: 8px 16px;
            border-radius: 12px; background: #fff; border: 1px solid #e2e8f0; transition: 0.3s;
        }
        .btn-back:hover { background: #f8fafc; color: #0f172a; transform: translateX(-4px); }

        .week-navigation { display: flex; align-items: center; gap: 5px; background: #fff; padding: 6px; border-radius: 16px; border: 1px solid #e2e8f0; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
        .week-navigation a {
            text-decoration: none; color: #64748b; font-weight: 600; padding: 8px 16px;
            border-radius: 12px; transition: 0.3s; display: flex; align-items: center;
        }
        .week-navigation a:hover { background: #f1f5f9; color: var(--emerald-dark); }
        .week-navigation a.active { background: var(--emerald-main); color: white; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.25); }

        /* --- Lưới Ma Trận (Đồng bộ với Bác sĩ & Admin) --- */
        .schedule-cell { border-right: 1px dashed #e2e8f0; border-bottom: 1px dashed #e2e8f0; position: relative; }
        .schedule-cell:last-child { border-right: none; }
        .table-borderless tbody tr:last-child .schedule-cell { border-bottom: none; }
        
        /* Hộp Ca (Cột trái) */
        .ca-box { background-color: var(--info-subtle); color: var(--info-color); width: 100%; border-radius: 16px; border: 1px solid #e2e8f0; }
        
        /* Ô Có Lịch (Emerald Style) */
        .shift-box-emerald {
            background-color: var(--emerald-subtle);
            border-top: 4px solid var(--emerald-main);
            border-radius: 16px;
            position: relative;
            z-index: 2;
            margin-top: 4px;
            margin-bottom: 4px;
            transition: 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }
        .shift-box-emerald:hover { transform: translateY(-4px); box-shadow: 0 8px 20px rgba(16, 185, 129, 0.15); border-color: var(--emerald-dark); }

        /* Điểm neo (Grid anchors) */
        .schedule-cell .grid-anchor { position: absolute; top: -5px; left: -5px; width: 10px; height: 10px; border-radius: 50%; border: 1px dashed #cbd5e1; background-color: white; z-index: 1; }
        .table-borderless tbody tr:first-child .schedule-cell .grid-anchor { top: -5px; } 
        .table-borderless tbody tr td:nth-child(2) .grid-anchor { left: -5px; } 
    </style>
</head>
<body>

<div class="schedule-premium-wrapper fade-in">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/component/staff/staffDashboard.jsp" class="btn-back">
            <i class="fas fa-chevron-left"></i> Về Dashboard
        </a>
    </div>

    <div class="d-flex justify-content-between align-items-end mb-4 flex-wrap gap-3 border-bottom pb-4">
        <div>
            <h3 class="fw-bold text-dark mb-1" style="font-size: 1.8rem;">Lịch Trực Cá Nhân</h3>
            <span class="text-success fw-semibold" style="font-size: 0.9rem;">
                <i class="bi bi-patch-check-fill me-1"></i>Đã đồng bộ hệ thống
            </span>
        </div>
        
        <div class="week-navigation">
            <a href="MySchedule?weekOffset=${currentWeekOffset - 1}">
                <i class="fas fa-chevron-left"></i>
            </a>
            <a href="MySchedule?weekOffset=0" class="${currentWeekOffset == 0 ? 'active' : ''}">Tuần này</a>
            <a href="MySchedule?weekOffset=1" class="${currentWeekOffset == 1 ? 'active' : ''}">Tuần tới</a>
            <a href="MySchedule?weekOffset=${currentWeekOffset + 1}">
                <i class="fas fa-chevron-right"></i>
            </a>
        </div>
    </div>

    <div class="table-responsive rounded-4 shadow-sm border-0 bg-white p-3">
        <table class="table table-borderless text-center align-middle mb-0" style="table-layout: fixed; min-width: 1000px; border-collapse: separate; border-spacing: 0;">
            <thead>
                <tr>
                    <th style="width: 10%;"></th>
                    <% for(String day : daysOfWeek) { %>
                        <th class="fw-bold text-secondary py-3 text-uppercase" style="font-size: 0.8rem; letter-spacing: 1px;"><%=day%></th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <% for(int r=0; r<8; r++) { %>
                <tr>
                    <td class="p-1 border-0">
                        <div class="ca-box p-3 shadow-sm text-center h-100 d-flex flex-column justify-content-center align-items-center">
                            <h6 class="fw-bold mb-1" style="font-size: 0.95rem; color: #334155;"><%=shiftNames[r]%></h6>
                            <small class="opacity-75" style="font-size: 0.75rem;"><%=shiftTimes[r]%></small>
                        </div>
                    </td>

                    <% for(int c=0; c<7; c++) { 
                        ShiftDTO s = (schedule != null) ? schedule[r][c] : null; 
                    %>
                        <td class="p-2 schedule-cell" style="height: 120px; vertical-align: top;">
                            <div class="grid-anchor"></div>
                            
                            <% if(s != null) { %>
                                <div class="shift-box-emerald p-3 h-100 text-start">
                                    <h6 class="fw-bold text-dark mb-1 text-truncate" style="font-size: 0.9rem;" title="<%= s.getDepartmentName() %>">
                                        <%= s.getDepartmentName() %>
                                    </h6>
                                    
                                    <div class="mt-2" style="font-size: 0.8rem; color: var(--emerald-dark); font-weight: 600;">
                                        <i class="bi bi-geo-alt-fill me-1"></i> P.<%= s.getRoomNumber() %>
                                    </div>
                                    
                                    <% if(s.getNote() != null && !s.getNote().isEmpty()) { %>
                                        <div class="mt-1 text-truncate" style="font-size: 0.75rem; color: #475569;">
                                            <i class="bi bi-card-text me-1 opacity-75"></i> <%= s.getNote() %>
                                        </div>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <div class="w-100 h-100"></div>
                            <% } %>
                        </td>
                    <% } %>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>