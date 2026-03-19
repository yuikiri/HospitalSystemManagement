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
    
    String[] shiftTimes = {"06:00 - 08:00", "08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00", 
                           "14:00 - 16:00", "16:00 - 18:00", "18:00 - 20:00", "20:00 - 22:00"};
    String[] daysOfWeek = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Ca Trực | Medicare</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #0052cc 100%); --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.04); --bg-main: #f4f7fe; --purple-main: #8b5cf6; }
        body { background: var(--bg-main); font-family: 'Plus Jakarta Sans', sans-serif; color: #4a5568; }
        .admin-container { max-width: 1400px; margin: 0 auto; }
        .card { border: none; border-radius: 20px; box-shadow: var(--soft-shadow); background: #ffffff; }
        .nav-tabs .nav-link { font-weight: 600; border: none; color: #a3aed0; padding: 12px 24px; border-radius: 12px !important; }
        .nav-tabs .nav-link.active { background: #fff; color: var(--purple-main); box-shadow: 0 4px 15px rgba(139, 92, 246, 0.15); }
        .shift-time-col { background: #f8fafc; font-weight: 700; text-align: center; border-radius: 14px; vertical-align: middle; border: 1px solid #f1f5f9; }
        .shift-cell { background: #ffffff; border: 1px dashed #e2e8f0; border-radius: 14px; height: 100px; text-align: center; vertical-align: middle; cursor: pointer; transition: 0.2s; }
        .shift-cell:hover { background: #f8fafc; border-color: var(--purple-main); transform: scale(1.02); }
        .shift-cell.has-shift { background: #f0fdf4; border: 1px solid #bbf7d0; border-left: 4px solid #10b981; }
        .btn-purple { background: linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%); color: white; border-radius: 12px; border: none; }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold">Quản lý Ca Trực</h3>
        <a href="AdminController?action=dashboard" class="btn btn-light"><i class="fa fa-arrow-left me-2"></i>Dashboard</a>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-4" style="border-radius: 12px;">
            <i class="fa fa-exclamation-triangle me-2"></i><strong>Lỗi!</strong> Không thể lưu ca trực. Vui lòng kiểm tra lại.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="mb-4">
        <ul class="nav nav-tabs" id="shiftTabs">
            <li class="nav-item"><button class="nav-link <%= "doctor".equals(activeType) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#doctor-tab">Lịch Bác Sĩ</button></li>
            <li class="nav-item"><button class="nav-link <%= "staff".equals(activeType) ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#staff-tab">Lịch Nhân Viên</button></li>
        </ul>
    </div>

    <div class="tab-content">
        <div class="tab-pane fade <%= "doctor".equals(activeType) ? "show active" : "" %>" id="doctor-tab">
            <div class="card p-4 mb-4">
                <form action="AdminController" method="get" class="row g-3">
                    <input type="hidden" name="action" value="shifts"><input type="hidden" name="type" value="doctor">
                    <div class="col-md-5"><input type="email" name="docEmail" class="form-control" placeholder="Email bác sĩ..." value="<%= docEmail != null ? docEmail : "" %>" required></div>
                    <div class="col-md-4">
                        <select name="weekOffset" class="form-select">
                            <option value="0" <%=currentWeekOffset==0?"selected":""%>>Tuần hiện tại</option>
                            <option value="1" <%=currentWeekOffset==1?"selected":""%>>Tuần sau</option>
                        </select>
                    </div>
                    <div class="col-md-3"><button type="submit" class="btn btn-purple w-100">Tải lịch</button></div>
                </form>
            </div>
            <div class="card p-4">
                <% if (docSchedule != null) { %>
                    <table class="table w-100" style="border-collapse: separate; border-spacing: 6px;">
                        <tbody>
                            <% for(int r=0; r<8; r++) { %>
                            <tr>
                                <td class="shift-time-col">Ca <%=r+1%><br><small><%=shiftTimes[r]%></small></td>
                                <% for(int c=0; c<7; c++) { 
                                    ShiftDTO s = docSchedule[r][c]; boolean has = (s!=null);
                                %>
                                    <td class="shift-cell <%=has?"has-shift":""%>" 
                                        onclick="openShiftModal('doctor', '<%=docEmail%>', <%=c+2%>, <%=r+1%>, <%=currentWeekOffset%>, '<%=has?s.getRoomId():""%>', '<%=has?s.getNote():""%>')">
                                        <% if(has) { %><strong><%=s.getDepartmentName()%></strong><br><small>P.<%=s.getRoomNumber()%></small><% } else { %><i class="fa fa-plus opacity-25"></i><% } %>
                                    </td>
                                <% } %>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %><p class="text-center p-5">Vui lòng nhập email bác sĩ</p><% } %>
            </div>
        </div>

        <div class="tab-pane fade <%= "staff".equals(activeType) ? "show active" : "" %>" id="staff-tab">
            <div class="card p-4 mb-4">
                <form action="AdminController" method="get" class="row g-3">
                    <input type="hidden" name="action" value="shifts"><input type="hidden" name="type" value="staff">
                    <div class="col-md-5"><input type="email" name="staffEmail" class="form-control" placeholder="Email nhân viên..." value="<%= staffEmail != null ? staffEmail : "" %>" required></div>
                    <div class="col-md-4"><select name="weekOffset" class="form-select"><option value="0" <%=currentWeekOffset==0?"selected":""%>>Tuần hiện tại</option><option value="1" <%=currentWeekOffset==1?"selected":""%>>Tuần sau</option></select></div>
                    <div class="col-md-3"><button type="submit" class="btn btn-primary w-100" style="background:#10b981; border:none; border-radius:12px;">Tải lịch</button></div>
                </form>
            </div>
            <div class="card p-4">
                <% if (staffSchedule != null) { %>
                    <table class="table w-100" style="border-collapse: separate; border-spacing: 6px;">
                        <tbody>
                            <% for(int r=0; r<8; r++) { %>
                            <tr>
                                <td class="shift-time-col">Ca <%=r+1%><br><small><%=shiftTimes[r]%></small></td>
                                <% for(int c=0; c<7; c++) { 
                                    ShiftDTO s = staffSchedule[r][c]; boolean has = (s!=null);
                                %>
                                    <td class="shift-cell <%=has?"has-shift":""%>" 
                                        onclick="openShiftModal('staff', '<%=staffEmail%>', <%=c+2%>, <%=r+1%>, <%=currentWeekOffset%>, '<%=has?s.getRoomId():""%>', '<%=has?s.getNote():""%>')">
                                        <% if(has) { %><strong><%=s.getDepartmentName()%></strong><br><small>P.<%=s.getRoomNumber()%></small><% } else { %><i class="fa fa-plus opacity-25"></i><% } %>
                                    </td>
                                <% } %>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %><p class="text-center p-5">Vui lòng nhập email nhân viên</p><% } %>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editShiftModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg">
            <form action="AdminController?action=updateShift" method="POST">
                <div class="modal-header border-0"><h5 class="modal-title fw-bold">📅 Phân bổ ca trực</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                <div class="modal-body px-4">
                    <input type="hidden" name="personType" id="modalPersonType">
                    <input type="hidden" name="targetEmail" id="modalEmail">
                    <input type="hidden" name="dayOfWeek" id="modalDay">
                    <input type="hidden" name="shiftNumber" id="modalShift">
                    <input type="hidden" name="weekOffset" id="modalWeekOffset">
                    
                    <div class="alert alert-info py-2 mb-3 rounded-4 small">Đang chọn: <strong id="modalInfoText">Thứ 2 - Ca 1</strong> cho <span id="modalTargetLabel">...</span></div>
                    
                    <div class="mb-3">
                        <label class="form-label text-primary fw-bold">Chọn Phòng Còn Trống <span class="text-danger">*</span></label>
                        <select name="roomId" id="modalRoomId" class="form-select" required>
                            <option value="">-- Đang kiểm tra phòng trống... --</option>
                        </select>
                        <small class="text-muted">Hệ thống chỉ hiện các phòng chưa có người trực trong khung giờ này.</small>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nhiệm vụ cụ thể</label>
                        <input type="text" name="roleNote" id="modalRoleNote" class="form-control" placeholder="VD: Trực chính, Vệ sinh..." required>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-purple px-4">Lưu Phân Công</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function openShiftModal(type, email, day, shift, week, roomId, note) {
        const dayNames = {2:"Thứ 2", 3:"Thứ 3", 4:"Thứ 4", 5:"Thứ 5", 6:"Thứ 6", 7:"Thứ 7", 8:"Chủ Nhật"};
        
        // 1. Điền thông tin vào các ô ẩn và label
        document.getElementById('modalPersonType').value = type;
        document.getElementById('modalEmail').value = email;
        document.getElementById('modalDay').value = day;
        document.getElementById('modalShift').value = shift;
        document.getElementById('modalWeekOffset').value = week;
        document.getElementById('modalRoleNote').value = note || "";
        
        document.getElementById('modalInfoText').innerText = dayNames[day] + " - Ca " + shift;
        document.getElementById('modalTargetLabel').innerText = email;

        // 2. Xử lý Dropdown phòng trống
        const roomSelect = document.getElementById('modalRoomId');
        roomSelect.innerHTML = "<option value=''>-- Đang kiểm tra phòng trống... --</option>";

        // CHÚ Ý: Đây là URL Cách 2 khớp với web.xml của bạn
        const url = '${pageContext.request.contextPath}/GetAvailableRoomsController?day=' + day + '&shift=' + shift + '&week=' + week;

        fetch(url)
            .then(response => {
                if (!response.ok) throw new Error('Mạng có vấn đề');
                return response.text();
            })
            .then(html => {
                roomSelect.innerHTML = html;
                // Nếu đang sửa ca cũ, chọn lại đúng phòng đó
                if(roomId) {
                    roomSelect.value = roomId;
                }
            })
            .catch(err => {
                console.error('Lỗi AJAX:', err);
                roomSelect.innerHTML = "<option value=''>Lỗi tải danh sách phòng</option>";
            });

        // 3. Mở Modal
        var myModal = new bootstrap.Modal(document.getElementById('editShiftModal'));
        myModal.show();
    }
</script>
</body>
</html>