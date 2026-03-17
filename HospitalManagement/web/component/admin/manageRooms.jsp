<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Phòng Khám & Lưu Bệnh | Medicare</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #0052cc 100%);
            --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            --hover-shadow: 0 15px 35px rgba(0, 0, 0, 0.08);
            --text-main: #2b3674;
            --text-muted: #a3aed0;
            --bg-main: #f4f7fe;
        }

        body { 
            background: var(--bg-main); 
            font-family: 'Plus Jakarta Sans', sans-serif; 
            color: #4a5568;
        }
        
        /* Main Layout Wrapper */
        .admin-container {
            max-width: 1400px;
            margin: 0 auto;
        }

        /* Card Styling */
        .card { 
            border: none; 
            border-radius: 20px; 
            box-shadow: var(--soft-shadow); 
            background: #ffffff;
            transition: all 0.3s ease;
        }
        .card:hover { box-shadow: var(--hover-shadow); }

        /* Tabs Styling */
        .nav-tabs { gap: 10px; }
        .nav-tabs .nav-link { 
            font-weight: 600; 
            border: none; 
            color: var(--text-muted); 
            padding: 12px 24px; 
            border-radius: 12px !important; 
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); 
            cursor: pointer;
        }
        .nav-tabs .nav-link:hover { background: #f8fafc; color: #0d6efd; }
        .nav-tabs .nav-link.active { 
            background: #fff; 
            color: #0d6efd; 
            box-shadow: 0 4px 15px rgba(13, 110, 253, 0.12); 
            transform: translateY(-2px);
        }

        /* Table Enhancements */
        .table { margin-bottom: 0; }
        .table > :not(caption) > * > * {
            padding: 1.25rem 1rem;
            border-bottom-color: #f1f5f9;
        }
        thead th {
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            font-weight: 700;
            color: var(--text-muted);
            border-bottom: none !important;
            background: transparent !important;
        }
        tbody tr { transition: all 0.2s ease-in-out; }
        tbody tr:hover {
            background-color: #fafbfc !important;
            transform: scale(1.002);
            box-shadow: inset 4px 0 0 0 #0d6efd;
        }

        /* Search & Filter Inputs */
        .search-box, .filter-select {
            border-radius: 14px;
            border: 1px solid #e2e8f0;
            background-color: #fff !important;
            padding: 12px 20px;
            font-size: 0.95rem;
            box-shadow: 0 2px 5px rgba(0,0,0,0.02);
            transition: all 0.3s ease;
        }
        .search-box:focus-within, .filter-select:focus {
            border-color: #0d6efd;
            box-shadow: 0 0 0 4px rgba(13, 110, 253, 0.1);
        }
        .input-group-text { background: transparent; }
        
        /* Action Buttons */
        .btn-action {
            border-radius: 10px;
            width: 36px;
            height: 36px;
            padding: 0;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            transition: all 0.2s ease;
            border: none;
            background: #f8fafc;
        }
        .btn-action.edit:hover { background: #0d6efd; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(13,110,253,0.2); }
        .btn-action.lock:hover { background: #f59e0b; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(245,158,11,0.2); }
        .btn-action.unlock:hover { background: #10b981; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(16,185,129,0.2); }
        .btn-action.delete:hover { background: #ef4444; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(239,68,68,0.2); }

        /* Modern Modal Styling */
        .modal-content {
            border: none;
            border-radius: 24px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
            overflow: hidden;
        }
        .modal-header { border-bottom: 1px solid #f1f5f9; padding: 1.5rem 2rem; background: #fff; }
        .modal-title { font-weight: 700; color: var(--text-main); letter-spacing: -0.5px; }
        .modal-body { padding: 2rem; background: #fafbfc; }
        .modal-footer { border-top: 1px solid #f1f5f9; padding: 1.25rem 2rem; background: #fff; }
        .form-label { font-weight: 600; color: var(--text-main); margin-bottom: 8px; font-size: 0.9rem; }
        .form-control, .form-select {
            border-radius: 12px;
            padding: 12px 16px;
            border: 1px solid #e0e5f2;
            background-color: #fff;
            transition: all 0.2s;
            font-size: 0.95rem;
        }
        .form-control:focus, .form-select:focus {
            background-color: #fff;
            border-color: #0d6efd;
            box-shadow: 0 0 0 4px rgba(13, 110, 253, 0.1);
        }

        /* Buttons Core */
        .btn-primary {
            background: var(--primary-gradient);
            border: none;
            border-radius: 12px;
            padding: 12px 24px;
            font-weight: 600;
            transition: 0.3s;
            letter-spacing: 0.3px;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 15px rgba(13, 110, 253, 0.25);
        }
        .btn-light {
            border-radius: 12px;
            background: #fff;
            color: var(--text-main);
            font-weight: 600;
            border: 1px solid #e2e8f0;
            padding: 12px 24px;
            transition: all 0.3s;
        }
        .btn-light:hover {
            background: #f8fafc;
            transform: translateY(-2px);
            box-shadow: 0 5px 10px rgba(0,0,0,0.05);
            border-color: #cbd5e1;
        }

        /* Badges & Avatars */
        .badge-status { border-radius: 8px; font-weight: 600; font-size: 0.75rem; }
        
        .avatar-icon { 
            width: 42px; 
            height: 42px; 
            border-radius: 12px; 
            background: #eff6ff;
            color: #0d6efd;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.1rem;
            border: 2px solid #fff;
            box-shadow: 0 3px 6px rgba(0,0,0,0.08);
        }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">

    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
            <h3 class="fw-bold mb-1" style="color: var(--text-main);">Quản lý Phòng bệnh</h3>
            <p class="text-muted mb-0"><i class="fa fa-hospital-user me-2"></i>Hệ thống quản trị nhân sự Medicare</p>
        </div>

        <div class="d-flex gap-2">
            <a href="AdminController?action=dashboard" class="btn btn-light shadow-sm">
                <i class="fa fa-arrow-left me-2"></i>Dashboard
            </a>
            <button class="btn btn-primary shadow-sm" data-bs-toggle="modal" data-bs-target="#addModal">
                <i class="fa fa-plus-circle me-2"></i>Thêm phòng mới
            </button>
        </div>
    </div>

    <div id="alertContainer">
        <c:if test="${param.error == 'addFailed'}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm" style="border-radius: 12px; border: none;" role="alert">
                <i class="fa fa-exclamation-triangle me-2"></i><strong>Thất bại!</strong> Số phòng này đã tồn tại trong khoa.
                <button type="button" class="btn-close shadow-none" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.error == 'updateFailed'}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm" style="border-radius: 12px; border: none;" role="alert">
                <i class="fa fa-exclamation-triangle me-2"></i><strong>Cập nhật thất bại!</strong> Số phòng mới bị trùng lặp trong khoa.
                <button type="button" class="btn-close shadow-none" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
    </div>

    <div class="card p-4 mb-4">
        <form action="AdminController" method="get" class="row g-3 align-items-center">
            <input type="hidden" name="action" value="rooms">
            
            <div class="col-md-3">
                <div class="input-group search-box">
                    <span class="input-group-text border-0 pe-2"><i class="fa fa-search text-muted"></i></span>
                    <input type="text" name="search" class="form-control border-0 shadow-none p-0" placeholder="Nhập số phòng..." value="${currentSearch}">
                </div>
            </div>
            
            <div class="col-md-3">
                <select name="departmentId" class="form-select filter-select shadow-none fw-semibold">
                    <option value="0">🏥 Tất cả Khoa / Phòng ban</option>
                    <c:forEach var="dept" items="${deptList}">
                        <option value="${dept.id}" ${currentDept == dept.id ? 'selected' : ''}>${dept.name}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="col-md-2">
                <select name="status" class="form-select filter-select shadow-none fw-semibold">
                    <option value="all" ${currentStatus == 'all' ? 'selected' : ''}>⚪ Mọi tình trạng</option>
                    <option value="available" ${currentStatus == 'available' ? 'selected' : ''}>🟢 Trống</option>
                    <option value="occupied" ${currentStatus == 'occupied' ? 'selected' : ''}>🟠 Đang dùng</option>
                    <option value="maintenance" ${currentStatus == 'maintenance' ? 'selected' : ''}>🔧 Bảo trì</option>
                </select>
            </div>
            
            <div class="col-md-2">
                <select name="isActive" class="form-select filter-select shadow-none fw-semibold">
                    <option value="-99" ${currentIsActive == -99 ? 'selected' : ''}>Tất cả trạng thái</option>
                    <option value="1" ${currentIsActive == 1 ? 'selected' : ''}>Đang mở cửa</option>
                    <option value="0" ${currentIsActive == 0 ? 'selected' : ''}>Đã đóng cửa</option>
                </select>
            </div>
            
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100" style="padding: 12px 24px;">Lọc dữ liệu</button>
            </div>
        </form>
    </div>

    <div class="card shadow-sm" id="roomTableContainer">
        <div class="p-3 border-bottom" style="background-color: rgba(255,255,255,0.8); backdrop-filter: blur(10px);">
            <ul class="nav nav-tabs border-0">
                <li class="nav-item">
                    <a class="nav-link active" href="AdminController?action=rooms">📋 Danh sách phòng (${totalRooms})</a>
                </li>
                <li class="nav-item ms-md-auto">
                    <a class="nav-link text-danger" href="AdminController?action=roomTrash" style="background: #fef2f2;">
                        <i class="fa fa-trash-alt me-2"></i>Thùng rác
                    </a>
                </li>
            </ul>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead style="background: #f8fafc;">
                    <tr>
                        <th style="width: 80px;" class="ps-4">ID</th>
                        <th>Thông tin Phòng</th>
                        <th>Trực thuộc Khoa</th>
                        <th>Loại Phòng</th>
                        <th>Giá tiền / Ngày</th>
                        <th class="text-center">Tình trạng</th>
                        <th class="text-center">Trạng thái</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="room" items="${roomList}">
                        <tr>
                            <td class="text-muted fw-semibold ps-4">#${room.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-icon me-3">
                                        <i class="fa fa-door-open"></i>
                                    </div>
                                    <span class="fw-bold" style="color: var(--text-main); font-size: 1.05rem;">Phòng ${room.roomNumber}</span>
                                </div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty room.departmentName}">
                                        <span class="fw-semibold text-dark">${room.departmentName}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-status bg-danger bg-opacity-10 text-danger px-2 py-1"><i class="fa fa-exclamation-circle me-1"></i> Chưa phân khoa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="text-muted fw-semibold">${room.roomTypeName}</span></td>
                            <td class="text-success fw-bold">
                                <fmt:formatNumber value="${room.price}" pattern="#,### VNĐ"/>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${room.status == 'available'}">
                                        <span class="badge badge-status bg-success bg-opacity-10 text-success px-3 py-2">Trống</span>
                                    </c:when>
                                    <c:when test="${room.status == 'occupied'}">
                                        <span class="badge badge-status bg-warning bg-opacity-10 text-warning px-3 py-2">Đang dùng</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-status bg-secondary bg-opacity-10 text-secondary px-3 py-2">Bảo trì</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${room.isActive == 1}"><span class="badge badge-status bg-success bg-opacity-10 text-success px-3 py-2"><i class="fa fa-circle me-1" style="font-size: 8px;"></i> Active</span></c:when>
                                    <c:otherwise><span class="badge badge-status bg-danger bg-opacity-10 text-danger px-3 py-2"><i class="fa fa-circle-xmark me-1"></i> Inactive</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <div class="d-flex justify-content-end gap-2">
                                    <button type="button" class="btn-action edit text-primary" title="Sửa thông tin" 
                                            data-bs-toggle="modal" data-bs-target="#editModal"
                                            data-id="${room.id}" 
                                            data-dept-id="${empty room.departmentId ? '0' : room.departmentId}" 
                                            data-room-num="${room.roomNumber}" 
                                            data-type-id="${room.roomType}" 
                                            data-status="${room.status}">
                                        <i class="fa fa-pen-nib"></i>
                                    </button>
                                    
                                    <form action="${pageContext.request.contextPath}/AdminController" method="POST" class="m-0 p-0">
                                        <input type="hidden" name="action" value="toggleRoom">
                                        <input type="hidden" name="id" value="${room.id}">
                                        <input type="hidden" name="status" value="${room.isActive}">
                                        <button type="submit" class="btn-action ${room.isActive == 1 ? 'lock text-warning' : 'unlock text-success'}" title="${room.isActive == 1 ? 'Khóa phòng' : 'Mở phòng'}">
                                            <i class="fa ${room.isActive == 1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                                        </button>
                                    </form>
                                    
                                    <button class="btn-action delete text-danger" title="Xóa phòng" onclick="deleteRoom(${room.id})">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty roomList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-5">
                                <i class="fa fa-box-open text-secondary opacity-50 mb-3 d-block" style="font-size: 3rem;"></i>
                                <span class="fw-semibold">Không tìm thấy phòng nào phù hợp!</span>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form id="addRoomForm" action="AdminController" method="post">
                <input type="hidden" name="action" value="addRoom">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fa fa-plus-circle me-2 text-primary"></i>Thêm phòng mới</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-12">
                            <label class="form-label">Trực thuộc Khoa <span class="text-danger">*</span></label>
                            <select class="form-select" name="departmentId" required>
                                <option value="0">-- Chưa phân khoa --</option>
                                <c:forEach var="dept" items="${deptList}">
                                    <option value="${dept.id}">${dept.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-12">
                            <label class="form-label">Loại Phòng <span class="text-danger">*</span></label>
                            <select class="form-select" name="roomType" required>
                                <c:forEach var="type" items="${typeList}">
                                    <option value="${type.id}">${type.name} - <fmt:formatNumber value="${type.price}" pattern="#,### VNĐ"/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12">
                            <label class="form-label">Số Phòng <span class="text-danger">*</span></label>
                            <input type="number" name="roomNumber" class="form-control" required min="1" placeholder="Ví dụ: 101, 102">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4">Xác nhận thêm</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form id="editRoomForm" action="AdminController" method="post">
                <input type="hidden" name="action" value="updateRoom">
                <input type="hidden" name="id" id="edit_id">
                <input type="hidden" name="oldDepartmentId" id="edit_oldDepartmentId">
                <input type="hidden" name="oldRoomNumber" id="edit_oldRoomNumber">

                <div class="modal-header">
                    <h5 class="modal-title"><i class="fa fa-pen-to-square me-2 text-primary"></i>Cập nhật thông tin</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-12">
                            <label class="form-label">Trực thuộc Khoa <span class="text-danger">*</span></label>
                            <select class="form-select" name="departmentId" id="edit_departmentId" required>
                                <option value="0">-- Chưa phân khoa --</option>
                                <c:forEach var="dept" items="${deptList}">
                                    <option value="${dept.id}">${dept.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-12">
                            <label class="form-label">Loại Phòng <span class="text-danger">*</span></label>
                            <select class="form-select" name="roomType" id="edit_roomType" required>
                                <c:forEach var="type" items="${typeList}">
                                    <option value="${type.id}">${type.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-sm-6">
                            <label class="form-label">Số Phòng <span class="text-danger">*</span></label>
                            <input type="number" name="roomNumber" id="edit_roomNumber" class="form-control" required min="1">
                        </div>
                        <div class="col-sm-6">
                            <label class="form-label">Tình trạng thực tế</label>
                            <select class="form-select" name="status" id="edit_status" required>
                                <option value="available">Trống</option>
                                <option value="occupied">Đang dùng</option>
                                <option value="maintenance">Bảo trì</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // 1. Nạp dữ liệu vào form Sửa
    const editModal = document.getElementById('editModal');
    if (editModal) {
        editModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            
            let oldDeptId = button.getAttribute('data-dept-id');
            let oldRoomNum = button.getAttribute('data-room-num');
            
            oldDeptId = (oldDeptId && oldDeptId !== "null" && oldDeptId.trim() !== "") ? oldDeptId : "0";
            
            document.getElementById('edit_id').value = button.getAttribute('data-id') || "0";
            document.getElementById('edit_oldDepartmentId').value = oldDeptId;
            document.getElementById('edit_oldRoomNumber').value = oldRoomNum || "0";
            
            document.getElementById('edit_departmentId').value = oldDeptId;
            document.getElementById('edit_roomNumber').value = oldRoomNum;
            document.getElementById('edit_roomType').value = button.getAttribute('data-type-id');
            document.getElementById('edit_status').value = button.getAttribute('data-status');
        });
    }

    // 2. Xóa mềm
    function deleteRoom(id) {
        if(confirm('Xác nhận chuyển phòng này vào thùng rác?')) {
            window.location.href = 'AdminController?action=deleteRoom&id=' + id;
        }
    }

    // 3. Tự động ẩn thông báo lỗi (Toast) sau 3 giây
    document.addEventListener("DOMContentLoaded", function() {
        const alerts = document.querySelectorAll('.alert-dismissible');
        alerts.forEach(function(alert) {
            setTimeout(function() {
                alert.classList.remove('show');
                setTimeout(() => alert.remove(), 200);
            }, 3000); 
        });
    });
</script>
</body>
</html>