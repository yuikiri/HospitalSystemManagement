<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 1. Tạo danh sách ca trực (static list) cho giờ --%>
<c:set var="shiftNames" value="${['Ca 1', 'Ca 2', 'Ca 3', 'Ca 4', 'Ca 5', 'Ca 6', 'Ca 7', 'Ca 8']}" />
<c:set var="shiftTimes" value="${['06:00-08:00', '08:00-10:00', '10:00-12:00', '12:00-14:00', '14:00-16:00', '16:00-18:00', '18:00-20:00', '20:00-22:00']}" />

<div class="content-section fade-in">
    <%-- 2. HEADER: Quay lại, Điều hướng & In lịch --%>
    <div class="d-flex justify-content-between align-items-center mb-2">
        <a href="${pageContext.request.contextPath}/component/doctor/doctorDashboard.jsp" class="btn btn-sm btn-light rounded-pill px-3 shadow-sm text-secondary">
            <i class="bi bi-chevron-left me-1"></i> Quay lại Dashboard
        </a>
        
        <div class="d-flex align-items-center bg-white p-2 rounded-pill shadow-sm border">
            <button class="btn btn-outline-secondary btn-sm rounded-circle d-flex align-items-center justify-content-center" style="width: 30px; height: 30px;"
                    onclick="loadContent('${pageContext.request.contextPath}/MySchedule?weekOffset=${currentWeekOffset - 1}', document.getElementById(sessionStorage.getItem('savedDoctorTabId')))">
                <i class="fas fa-chevron-left fs-7"></i>
            </button>
            
            <span class="fw-bold px-3 text-secondary" style="min-width: 120px; text-align: center; font-size: 0.9rem;">
                <c:choose>
                    <c:when test="${currentWeekOffset == 0}">Tuần hiện tại</c:when>
                    <c:when test="${currentWeekOffset == -1}">Tuần trước</c:when>
                    <c:when test="${currentWeekOffset == 1}">Tuần sau</c:when>
                    <c:when test="${currentWeekOffset < -1}">${Math.abs(currentWeekOffset)} tuần trước</c:when>
                    <c:otherwise>${currentWeekOffset} tuần tới</c:otherwise>
                </c:choose>
            </span>
            
            <button class="btn btn-outline-secondary btn-sm rounded-circle d-flex align-items-center justify-content-center" style="width: 30px; height: 30px;"
                    onclick="loadContent('${pageContext.request.contextPath}/MySchedule?weekOffset=${currentWeekOffset + 1}', document.getElementById(sessionStorage.getItem('savedDoctorTabId')))">
                <i class="fas fa-chevron-right fs-7"></i>
            </button>
        </div>
        
        <button class="btn btn-primary rounded-pill px-4 shadow-sm fw-semibold">
            <i class="bi bi-printer me-2"></i> In lịch
        </button>
    </div>

    <div class="mb-4">
        <h3 class="fw-bold text-dark mb-0">Lịch Trực Tuần Hiện Tại</h3>
        <small class="text-success fw-medium"><i class="bi bi-patch-check-fill me-1"></i> Đã đồng bộ với hệ thống bệnh viện</small>
    </div>

    <%-- 3. BẢNG CHI TIẾT LỊCH TRỰC TEAL (Table structure) --%>
    <div class="table-responsive rounded-4 shadow-sm border-0 bg-white p-3">
        <table class="table table-borderless text-center align-middle mb-0" style="table-layout: fixed; min-width: 1000px; border-collapse: separate; border-spacing: 0;">
            <thead class="bg-light">
                <tr>
                    <th style="width: 9%; background-color: transparent;"></th>
                    <c:set var="dayNames" value="${['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'CN']}" />
                    <c:forEach var="dayName" items="${dayNames}">
                        <th class="fw-bold text-secondary py-3" style="font-size: 0.85rem; background-color: transparent;">${dayName}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <%-- Vòng lặp qua 8 Ca trực (Ca 1-Ca 8, begin=0 end=7) --%>
                <c:forEach var="row" begin="0" end="7">
                    <tr>
                        <%-- Cột đầu tiên: Hộp Info cho Ca (Dùng màu xanh info nhạt) --%>
                        <td class="p-1" style="background-color: transparent; border: none;">
                            <div class="ca-box p-3 rounded-4 shadow-sm text-center border-0 bg-info-subtle text-info h-100 d-flex flex-column justify-content-center align-items-center">
                                <h6 class="fw-bold mb-1" style="font-size: 0.95rem;">${shiftNames[row]}</h6>
                                <small class="opacity-75" style="font-size: 0.75rem;">${shiftTimes[row]}</small>
                            </div>
                        </td>
                        
                        <%-- Vòng lặp qua 7 ngày trong tuần --%>
                        <c:forEach var="col" begin="0" end="6">
                            <td class="p-2 position-relative schedule-cell" style="height: 120px; vertical-align: top; border-bottom: 1px dashed #e9ecef;">
                                <%-- Điểm neo hình tròn nhỏ (Anchor points, giống mẫu) --%>
                                <div class="grid-anchor"></div>
                                
                                <%-- ⚠️ Back-end phải trả về schedule với kích thước [8][7] --%>
                                <c:set var="shift" value="${userSchedule[row][col]}" />
                                
                                <c:choose>
                                    <c:when test="${not empty shift}">
                                        <%-- Ô CÓ LỊCH: Hộp lịch dài Teal (Màu teal như ảnh mẫu) --%>
                                        <div class="shift-box-teal p-3 rounded-4 shadow-sm h-100 border-teal-top bg-teal-subtle text-teal">
                                            <h6 class="fw-bold text-dark mb-1" style="font-size: 0.9rem;">${shift.departmentName}</h6>
                                            
                                            <div class="text-teal-dark mt-1" style="font-size: 0.75rem;">
                                                <i class="bi bi-geo-alt-fill me-1"></i> Phòng ${shift.roomNumber}
                                            </div>
                                            <div class="text-teal-dark mt-1" style="font-size: 0.75rem;">
                                                <i class="bi bi-card-text me-1"></i> ${shift.note != null ? shift.note : 'Chưa có ghi chú'}
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- Ô TRỐNG: Đơn giản để lưới hiện ra --%>
                                        <div class="w-100 h-100"></div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%-- 4. CSS: Thiết kế lại tông màu teal và điểm neo --%>
<style>
    :root {
        --teal-color: #00897b; /* Teal */
        --teal-subtle: #e0f2f1; /* Teal nhạt */
        --teal-dark: #00695c; /* Teal đậm */
        --info-subtle: #e0f7fa; /* Info nhạt cho Ca */
        --info-color: #00bcd4; /* Info color cho Ca */
    }
    .content-section { font-family: 'Inter', sans-serif; }
    .schedule-cell { border-right: 1px dashed #e9ecef; }
    .schedule-cell:last-child { border-right: none; }
    
    /* Hộp Ca (Dùng màu xanh info nhạt) */
    .ca-box { background-color: var(--info-subtle) !important; color: var(--info-color) !important; width: 100%; border-radius: 1rem; }
    .ca-box h6 { font-weight: 700; }
    
    /* Hộp lịch dài Teal (Màu teal nhạt, viền teal đậm) */
    .shift-box-teal { background-color: var(--teal-subtle) !important; border-top: 3px solid var(--teal-color) !important; border-radius: 1rem; position: relative; z-index: 2; margin-top: 5px; margin-bottom: 5px;}
    .text-teal { color: var(--teal-color) !important; }
    .text-teal-dark { color: var(--teal-dark) !important; }

    /* Điểm neo (grid anchor points - hình tròn nhỏ với dấu cộng) */
    .schedule-cell .grid-anchor { position: absolute; top: -5px; left: -5px; width: 10px; height: 10px; border-radius: 50%; border: 1px dashed #ced4da; background-color: white; z-index: 1; }
    .table-borderless tbody tr:first-child .schedule-cell .grid-anchor { top: -5px; } /* First row correction */
    .table-borderless tbody tr:last-child .schedule-cell { border-bottom: none; } /* Last row no bottom border */
    .table-borderless tbody tr td:nth-child(2) .grid-anchor { left: -5px; } /* First day column correction */
    
    /* Fade in animation (giữ nguyên cũ) */
    .fade-in { animation: fadeIn 0.4s ease-in-out; }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
    .fs-7 { font-size: 0.85rem; }
</style>