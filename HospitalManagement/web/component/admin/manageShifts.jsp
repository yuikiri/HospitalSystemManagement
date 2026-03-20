<%@page import="dao.ShiftDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    String activeType = (String) request.getAttribute("activeType");
    if (activeType == null) activeType = "doctor"; 
    
    Integer currentWeekOffset = (Integer) request.getAttribute("currentWeekOffset");
    if (currentWeekOffset == null) currentWeekOffset = 0;
    
    String docEmail = (String) request.getAttribute("docEmail");
    String staffEmail = (String) request.getAttribute("staffEmail");
    
    ShiftDTO[][] docSchedule = (ShiftDTO[][]) request.getAttribute("docSchedule");
    ShiftDTO[][] staffSchedule = (ShiftDTO[][]) request.getAttribute("staffSchedule");
    
    String[] shiftNames = {"Ca 1", "Ca 2", "Ca 3", "Ca 4", "Ca 5", "Ca 6", "Ca 7", "Ca 8"};
    String[] shiftTimes = {"06:00-08:00", "08:00-10:00", "10:00-12:00", "12:00-14:00", 
                           "14:00-16:00", "16:00-18:00", "18:00-20:00", "20:00-22:00"};
    String[] daysOfWeek = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Ca Trực | Medicare Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root { 
            --teal-color: #00897b; 
            --teal-subtle: #e0f2f1; 
            --teal-dark: #00695c;
            --info-subtle: #e0f7fa; 
            --info-color: #00bcd4;
            --bg-main: #f8f9fa;
        }
        body { background: var(--bg-main); font-family: 'Inter', sans-serif; color: #343a40; }
        .admin-container { max-width: 1400px; margin: 0 auto; }
        
        /* Nav Tabs */
        .nav-tabs .nav-link { font-weight: 600; border: none; color: #adb5bd; padding: 12px 24px; border-radius: 12px !important; transition: 0.3s; }
        .nav-tabs .nav-link:hover { color: var(--teal-color); background: #f8f9fa; }
        .nav-tabs .nav-link.active { background: var(--teal-color); color: white; box-shadow: 0 4px 15px rgba(0, 137, 123, 0.2); }
        
        /* Bảng Timetable (Đồng bộ Teal) */
        .schedule-cell { border-right: 1px dashed #e9ecef; border-bottom: 1px dashed #e9ecef; position: relative; cursor: pointer; transition: 0.2s; }
        .schedule-cell:hover { background-color: #f8f9fa; }
        .schedule-cell:last-child { border-right: none; }
        .table-borderless tbody tr:last-child .schedule-cell { border-bottom: none; }
        
        /* Dấu + mờ khi hover ô trống */
        .empty-cell-icon { opacity: 0; transition: 0.2s; color: var(--teal-color); font-size: 1.5rem; }
        .schedule-cell:hover .empty-cell-icon { opacity: 0.3; }

        /* Hộp Ca (Dùng màu xanh info nhạt) */
        .ca-box { background-color: var(--info-subtle); color: var(--info-color); width: 100%; border-radius: 1rem; }
        
        /* Hộp lịch dài Teal */
        .shift-box-teal { background-color: var(--teal-subtle); border-top: 3px solid var(--teal-color); border-radius: 1rem; position: relative; z-index: 2; margin-top: 5px; margin-bottom: 5px; transition: 0.2s;}
        .shift-box-teal:hover { transform: translateY(-3px); box-shadow: 0 .5rem 1rem rgba(0, 137, 123, 0.15); border-color: var(--teal-dark); }
        .text-teal { color: var(--teal-color); }
        .text-teal-dark { color: var(--teal-dark); }

        /* Điểm neo (grid anchor points) */
        .schedule-cell .grid-anchor { position: absolute; top: -5px; left: -5px; width: 10px; height: 10px; border-radius: 50%; border: 1px dashed #ced4da; background-color: white; z-index: 1; }
        .table-borderless tbody tr:first-child .schedule-cell .grid-anchor { top: -5px; } 
        .table-borderless tbody tr td:nth-child(2) .grid-anchor { left: -5px; } 

        .btn-teal { background-color: var(--teal-color); color: white; border-radius: 12px; border: none; }
        .btn-teal:hover { background-color: var(--teal-dark); color: white; }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">
    <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
        <h3 class="fw-bold text-dark mb-0"><i class="bi bi-calendar-range text-teal me-2"></i> Phân Bổ Ca Trực</h3>
        <a href="AdminController?action=dashboard" class="btn btn-light shadow-sm rounded-pill px-4 fw-semibold text-secondary">
            <i class="bi bi-arrow-left me-2"></i>Về Dashboard
        </a>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-4 rounded-4 border-0 border-start border-4 border-danger">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><strong>Lỗi!</strong> Không thể lưu ca trực. Vui lòng kiểm tra lại sự cố đụng giờ hoặc lỗi hệ thống.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="mb-4 bg-white p-2 rounded-4 shadow-sm d-inline-block">
        <ul class="nav nav-tabs border-0" id="shiftTabs">
            <li class="nav-item"><button class="nav-link <%= "doctor".equals(activeType) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#doctor-tab"><i class="bi bi-heart-pulse me-2"></i>Bác Sĩ</button></li>
            <li class="nav-item"><button class="nav-link <%= "staff".equals(activeType) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#staff-tab"><i class="bi bi-person-badge me-2"></i>Nhân Viên</button></li>
        </ul>
    </div>

    <div class="tab-content">
        <div class="tab-pane fade <%= "doctor".equals(activeType) ? "show active" : "" %>" id="doctor-tab">
            <div class="bg-white p-4 rounded-4 shadow-sm mb-4 border-0">
                <form action="AdminController" method="get" class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="shifts"><input type="hidden" name="type" value="doctor">
                    <div class="col-md-5">
                        <label class="form-label text-muted fw-semibold small">Email Bác sĩ</label>
                        <input type="email" name="docEmail" class="form-control form-control-lg bg-light border-0" placeholder="Nhập email bác sĩ..." value="<%= docEmail != null ? docEmail : "" %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label text-muted fw-semibold small">Tuần làm việc</label>
                        <select name="weekOffset" class="form-select form-select-lg bg-light border-0">
                            <option value="0" <%=currentWeekOffset==0?"selected":""%>>Tuần hiện tại</option>
                            <option value="1" <%=currentWeekOffset==1?"selected":""%>>Tuần tới (+1)</option>
                            <option value="2" <%=currentWeekOffset==2?"selected":""%>>Tuần tới nữa (+2)</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-teal btn-lg w-100 fw-bold"><i class="bi bi-search me-2"></i>Tải lịch</button>
                    </div>
                </form>
            </div>
            
            <div class="bg-white p-4 rounded-4 shadow-sm border-0 table-responsive">
                <% if (docSchedule != null) { %>
                    <table class="table table-borderless text-center align-middle mb-0" style="table-layout: fixed; min-width: 1000px; border-collapse: separate; border-spacing: 0;">
                        <thead>
                            <tr>
                                <th style="width: 9%;"></th>
                                <% for(String day : daysOfWeek) { %>
                                    <th class="fw-bold text-secondary py-3" style="font-size: 0.85rem;"><%=day%></th>
                                <% } %>
                            </tr>
                        </thead>
                        <tbody>
                            <% for(int r=0; r<8; r++) { %>
                            <tr>
                                <td class="p-1 border-0">
                                    <div class="ca-box p-3 shadow-sm text-center h-100 d-flex flex-column justify-content-center align-items-center">
                                        <h6 class="fw-bold mb-1" style="font-size: 0.95rem;"><%=shiftNames[r]%></h6>
                                        <small class="opacity-75" style="font-size: 0.75rem;"><%=shiftTimes[r]%></small>
                                    </div>
                                </td>
                                <% for(int c=0; c<7; c++) { 
                                    ShiftDTO s = docSchedule[r][c]; boolean has = (s!=null);
                                %>
                                    <td class="p-2 schedule-cell" onclick="openShiftModal('doctor', '<%=docEmail%>', <%=c+2%>, <%=r+1%>, <%=currentWeekOffset%>, '<%=has?s.getRoomId():""%>', '<%=has?s.getNote():""%>')">
                                        <div class="grid-anchor"></div>
                                        <% if(has) { %>
                                            <div class="shift-box-teal p-3 h-100 text-start">
                                                <h6 class="fw-bold text-dark mb-1" style="font-size: 0.85rem;"><%=s.getDepartmentName()%></h6>
                                                <div class="text-teal-dark mt-1" style="font-size: 0.75rem;"><i class="bi bi-geo-alt-fill me-1"></i> P.<%=s.getRoomNumber()%></div>
                                            </div>
                                        <% } else { %>
                                            <div class="w-100 h-100 d-flex align-items-center justify-content-center">
                                                <i class="bi bi-plus-circle empty-cell-icon"></i>
                                            </div>
                                        <% } %>
                                    </td>
                                <% } %>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="text-center p-5 text-muted">
                        <i class="bi bi-inbox fs-1 d-block mb-3 opacity-50"></i>
                        <h5>Chưa có dữ liệu</h5>
                        <p>Vui lòng nhập Email Bác sĩ và nhấn Tải lịch</p>
                    </div>
                <% } %>
            </div>
        </div>

        <div class="tab-pane fade <%= "staff".equals(activeType) ? "show active" : "" %>" id="staff-tab">
            <div class="bg-white p-4 rounded-4 shadow-sm mb-4 border-0">
                <form action="AdminController" method="get" class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="shifts"><input type="hidden" name="type" value="staff">
                    <div class="col-md-5">
                        <label class="form-label text-muted fw-semibold small">Email Nhân viên</label>
                        <input type="email" name="staffEmail" class="form-control form-control-lg bg-light border-0" placeholder="Nhập email nhân viên..." value="<%= staffEmail != null ? staffEmail : "" %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label text-muted fw-semibold small">Tuần làm việc</label>
                        <select name="weekOffset" class="form-select form-select-lg bg-light border-0">
                            <option value="0" <%=currentWeekOffset==0?"selected":""%>>Tuần hiện tại</option>
                            <option value="1" <%=currentWeekOffset==1?"selected":""%>>Tuần tới (+1)</option>
                            <option value="2" <%=currentWeekOffset==2?"selected":""%>>Tuần tới nữa (+2)</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold border-0" style="background-color: #3b82f6;"><i class="bi bi-search me-2"></i>Tải lịch</button>
                    </div>
                </form>
            </div>
            
            <div class="bg-white p-4 rounded-4 shadow-sm border-0 table-responsive">
                <% if (staffSchedule != null) { %>
                    <table class="table table-borderless text-center align-middle mb-0" style="table-layout: fixed; min-width: 1000px; border-collapse: separate; border-spacing: 0;">
                        <thead>
                            <tr>
                                <th style="width: 9%;"></th>
                                <% for(String day : daysOfWeek) { %>
                                    <th class="fw-bold text-secondary py-3" style="font-size: 0.85rem;"><%=day%></th>
                                <% } %>
                            </tr>
                        </thead>
                        <tbody>
                            <% for(int r=0; r<8; r++) { %>
                            <tr>
                                <td class="p-1 border-0">
                                    <div class="ca-box p-3 shadow-sm text-center h-100 d-flex flex-column justify-content-center align-items-center" style="background-color: #eff6ff; color: #3b82f6;">
                                        <h6 class="fw-bold mb-1" style="font-size: 0.95rem;"><%=shiftNames[r]%></h6>
                                        <small class="opacity-75" style="font-size: 0.75rem;"><%=shiftTimes[r]%></small>
                                    </div>
                                </td>
                                <% for(int c=0; c<7; c++) { 
                                    ShiftDTO s = staffSchedule[r][c]; boolean has = (s!=null);
                                %>
                                    <td class="p-2 schedule-cell" onclick="openShiftModal('staff', '<%=staffEmail%>', <%=c+2%>, <%=r+1%>, <%=currentWeekOffset%>, '<%=has?s.getRoomId():""%>', '<%=has?s.getNote():""%>')">
                                        <div class="grid-anchor"></div>
                                        <% if(has) { %>
                                            <div class="p-3 h-100 text-start rounded-4 shadow-sm position-relative" style="background-color: #eff6ff; border-top: 3px solid #3b82f6; z-index: 2; margin-top: 5px; margin-bottom: 5px; transition: 0.2s;">
                                                <h6 class="fw-bold text-dark mb-1" style="font-size: 0.85rem;"><%=s.getDepartmentName()%></h6>
                                                <div class="mt-1" style="font-size: 0.75rem; color: #1e40af;"><i class="bi bi-geo-alt-fill me-1"></i> P.<%=s.getRoomNumber()%></div>
                                            </div>
                                        <% } else { %>
                                            <div class="w-100 h-100 d-flex align-items-center justify-content-center">
                                                <i class="bi bi-plus-circle empty-cell-icon" style="color: #3b82f6;"></i>
                                            </div>
                                        <% } %>
                                    </td>
                                <% } %>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="text-center p-5 text-muted">
                        <i class="bi bi-inbox fs-1 d-block mb-3 opacity-50"></i>
                        <h5>Chưa có dữ liệu</h5>
                        <p>Vui lòng nhập Email Nhân viên và nhấn Tải lịch</p>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editShiftModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0 rounded-4">
            <form action="AdminController?action=updateShift" method="POST">
                <div class="modal-header border-bottom bg-light">
                    <h5 class="modal-title fw-bold text-dark"><i class="bi bi-calendar-plus text-teal me-2"></i> Phân bổ ca trực</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <input type="hidden" name="personType" id="modalPersonType">
                    <input type="hidden" name="targetEmail" id="modalEmail">
                    <input type="hidden" name="dayOfWeek" id="modalDay">
                    <input type="hidden" name="shiftNumber" id="modalShift">
                    <input type="hidden" name="weekOffset" id="modalWeekOffset">
                    
                    <div class="alert bg-teal-subtle text-teal-dark border-0 py-2 mb-4 rounded-3 text-center">
                        <i class="bi bi-info-circle-fill me-2"></i>Đang phân công: <strong id="modalInfoText" class="fs-6">Thứ 2 - Ca 1</strong><br>
                        <small>Nhân sự: <span id="modalTargetLabel" class="fw-bold">...</span></small>
                    </div>
                    
                    <div class="mb-4">
                        <label class="form-label fw-bold text-dark">Chọn Phòng Còn Trống <span class="text-danger">*</span></label>
                        <select name="roomId" id="modalRoomId" class="form-select form-select-lg bg-light" required>
                            <option value="">-- Đang kiểm tra phòng trống... --</option>
                        </select>
                        <small class="text-muted mt-2 d-block"><i class="bi bi-shield-check me-1 text-success"></i>Hệ thống tự động lọc các phòng bị trùng giờ.</small>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold text-dark">Nhiệm vụ cụ thể</label>
                        <input type="text" name="roleNote" id="modalRoleNote" class="form-control form-control-lg bg-light" placeholder="VD: Khám tổng quát, Trực chính..." required>
                    </div>
                </div>
                <div class="modal-footer border-top bg-light">
                    <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-teal fw-bold rounded-pill px-4">Lưu Phân Công</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function openShiftModal(type, email, day, shift, week, roomId, note) {
        const dayNames = {2:"Thứ 2", 3:"Thứ 3", 4:"Thứ 4", 5:"Thứ 5", 6:"Thứ 6", 7:"Thứ 7", 8:"Chủ Nhật"};
        
        // 1. Điền thông tin vào form
        document.getElementById('modalPersonType').value = type;
        document.getElementById('modalEmail').value = email;
        document.getElementById('modalDay').value = day;
        document.getElementById('modalShift').value = shift;
        document.getElementById('modalWeekOffset').value = week;
        document.getElementById('modalRoleNote').value = note || "";
        
        document.getElementById('modalInfoText').innerText = dayNames[day] + " - Ca " + shift;
        document.getElementById('modalTargetLabel').innerText = email;

        // 2. Fetch danh sách phòng trống qua AJAX
        const roomSelect = document.getElementById('modalRoomId');
        roomSelect.innerHTML = "<option value=''>-- Đang tải dữ liệu... --</option>";
        roomSelect.disabled = true;

        const url = '${pageContext.request.contextPath}/GetAvailableRoomsController?day=' + day + '&shift=' + shift + '&week=' + week;

        fetch(url)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text();
            })
            .then(html => {
                roomSelect.innerHTML = html;
                roomSelect.disabled = false;
                if(roomId) roomSelect.value = roomId; // Tự chọn lại phòng nếu là sửa đổi
            })
            .catch(err => {
                console.error('Lỗi tải danh sách phòng:', err);
                roomSelect.innerHTML = "<option value=''>❌ Lỗi kết nối máy chủ</option>";
            });

        // 3. Hiển thị Modal
        var myModal = new bootstrap.Modal(document.getElementById('editShiftModal'));
        myModal.show();
    }
</script>
</body>
</html>