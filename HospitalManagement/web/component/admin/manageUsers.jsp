<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Users | Medicare Pro</title>
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
        .card:hover {
            box-shadow: var(--hover-shadow);
        }

        /* Tabs Styling */
        .nav-tabs {
            gap: 10px;
        }
        .nav-tabs .nav-link { 
            font-weight: 600; 
            border: none; 
            color: var(--text-muted); 
            padding: 12px 24px; 
            border-radius: 12px !important; 
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); 
            cursor: pointer;
        }
        .nav-tabs .nav-link:hover {
            background: #f8fafc;
            color: #0d6efd;
        }
        .nav-tabs .nav-link.active { 
            background: #fff; 
            color: #0d6efd; 
            box-shadow: 0 4px 15px rgba(13, 110, 253, 0.12); 
            transform: translateY(-2px);
        }

        /* Table Enhancements */
        .table {
            margin-bottom: 0;
        }
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
        tbody tr {
            transition: all 0.2s ease-in-out;
        }
        tbody tr:hover {
            background-color: #fafbfc !important;
            transform: scale(1.002);
            box-shadow: inset 4px 0 0 0 #0d6efd; /* Line xanh highlight bên trái khi hover */
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
        .btn-action.delete:hover { background: #ef4444; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(239,68,68,0.2); }
        .btn-action.restore:hover { background: #10b981; color: white !important; transform: translateY(-2px); box-shadow: 0 5px 10px rgba(16,185,129,0.2); }

        /* Modern Modal Styling */
        .modal-content {
            border: none;
            border-radius: 24px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
            overflow: hidden;
        }
        .modal-header {
            border-bottom: 1px solid #f1f5f9;
            padding: 1.5rem 2rem;
            background: #fff;
        }
        .modal-title { font-weight: 700; color: var(--text-main); letter-spacing: -0.5px; }
        .modal-body { padding: 2rem; background: #fafbfc; }
        .modal-footer {
            border-top: 1px solid #f1f5f9;
            padding: 1.25rem 2rem;
            background: #fff;
        }
        .form-label {
            font-weight: 600;
            color: var(--text-main);
            margin-bottom: 8px;
            font-size: 0.9rem;
        }
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
        .badge-role { font-weight: 600; letter-spacing: 0.5px; border-radius: 8px; font-size: 0.75rem; }
        .badge-doctor { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }
        .badge-staff { background: #fff7ed; color: #c2410c; border: 1px solid #fed7aa; }
        .badge-patient { background: #f0fdf4; color: #15803d; border: 1px solid #bbf7d0; }
        .badge-status { border-radius: 8px; font-weight: 600; font-size: 0.75rem; }
        
        .avatar { 
            width: 42px; 
            height: 42px; 
            border-radius: 12px; 
            border: 2px solid #fff;
            box-shadow: 0 3px 6px rgba(0,0,0,0.08);
            object-fit: cover;
        }
        
        /* Custom scrollbar cho table nếu màn hình nhỏ */
        ::-webkit-scrollbar { width: 8px; height: 8px; }
        ::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 10px; }
        ::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 10px; }
        ::-webkit-scrollbar-thumb:hover { background: #94a3b8; }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">

    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
            <h3 class="fw-bold mb-1" style="color: var(--text-main);">Quản lý người dùng</h3>
            <p class="text-muted mb-0"><i class="fa fa-shield-halved me-2"></i>Hệ thống quản trị nhân sự Medicare</p>
        </div>

        <div class="d-flex gap-2">
            <a href="AdminController?action=dashboard" class="btn btn-light shadow-sm">
                <i class="fa fa-arrow-left me-2"></i>Dashboard
            </a>

            <button class="btn btn-primary shadow-sm" data-bs-toggle="modal" data-bs-target="#addUserModal">
                <i class="fa fa-plus-circle me-2"></i>Thêm người dùng mới
            </button>
        </div>
    </div>

    <div class="card p-4 mb-4">
        <form action="AdminController" method="GET" class="row g-3 align-items-center">
            <input type="hidden" name="action" value="users">
            <input type="hidden" name="role" value="${activeTab != null ? activeTab : 'doctor'}">
            
            <div class="col-md-5">
                <div class="input-group search-box">
                    <span class="input-group-text border-0 pe-2"><i class="fa fa-search text-muted"></i></span>
                    <input type="text" class="form-control border-0 shadow-none p-0" name="searchQuery" placeholder="Tìm kiếm theo tên hoặc email..." value="${param.searchQuery}">
                </div>
            </div>
            
            <div class="col-md-3">
                <select class="form-select filter-select shadow-none fw-semibold" name="statusFilter" onchange="this.form.submit()">
                    <option value="1" ${currentStatus == 1 ? 'selected' : ''}>🟢 Trạng thái: Hoạt động</option>
                    <option value="0" ${currentStatus == 0 ? 'selected' : ''}>🔴 Trạng thái: Bị khóa</option>
                    <option value="-99" ${currentStatus == -99 ? 'selected' : ''}>⚪ Tất cả trạng thái</option>
                </select>
            </div>
            
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100" style="padding: 12px 24px;">Lọc kết quả</button>
            </div>
        </form>
    </div>

    <div class="card shadow-sm">
        <div class="p-3 border-bottom" style="background-color: rgba(255,255,255,0.8); backdrop-filter: blur(10px);">
            <ul class="nav nav-tabs border-0">
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'doctor' || activeTab == null ? 'active' : ''}" onclick="changeRole('doctor')">👨‍⚕️ Bác sĩ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'staff' ? 'active' : ''}" onclick="changeRole('staff')">🧑‍💼 Nhân viên</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'patient' ? 'active' : ''}" onclick="changeRole('patient')">🤒 Bệnh nhân</a>
                </li>
                <li class="nav-item ms-md-auto">
                    <a class="nav-link text-danger ${activeTab == 'trash' ? 'active bg-danger text-white shadow-none' : ''}" onclick="changeRole('trash')" style="background: #fef2f2;">
                        <i class="fa fa-trash-alt me-2"></i>Thùng rác
                    </a>
                </li>
            </ul>
        </div>
        
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead style="background: #f8fafc;">
                    <tr>
                        <th class="ps-4">Mã ID</th>
                        <th>Thông tin người dùng</th>
                        <th>Email liên hệ</th>
                        <th>Vai trò</th>
                        <th>Trạng thái</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="u">
                        <tr id="user-${u.id}">
                            <td class="ps-4 text-muted fw-semibold">#${u.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <img src="https://ui-avatars.com/api/?name=${u.userName}&background=random&bold=true" class="avatar me-3">
                                    <div>
                                        <div class="fw-bold" style="color: var(--text-main);">${u.userName}</div>
                                    </div>
                                </div>
                            </td>
                            <td class="text-muted">${u.email}</td>
                            <td>
                                <c:set var="r" value="${u.role.toLowerCase()}"/>
                                <span class="badge badge-role ${r=='doctor'?'badge-doctor':(r=='staff'?'badge-staff':'badge-patient')} px-3 py-2">
                                    ${u.role.toUpperCase()}
                                </span>
                            </td>
                            <td class="status-badge">
                                <c:choose>
                                    <c:when test="${u.isActive == 1}"><span class="badge badge-status bg-success bg-opacity-10 text-success px-3 py-2"><i class="fa fa-circle me-1" style="font-size: 8px;"></i> Active</span></c:when>
                                    <c:otherwise><span class="badge badge-status bg-danger bg-opacity-10 text-danger px-3 py-2"><i class="fa fa-circle-xmark me-1"></i> Banned</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <div class="d-flex justify-content-end gap-2">
                                    <c:choose>
                                        <c:when test="${activeTab == 'trash'}">
                                            <button class="btn-action restore text-success" title="Khôi phục" onclick="restoreUser(${u.id})"><i class="fa fa-undo"></i></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn-action edit text-primary" title="Chỉnh sửa" onclick="openEdit('${u.id}','${u.userName}','${u.email}','${u.role}')">
                                                <i class="fa fa-pen-nib"></i>
                                            </button>
                                            <button class="btn-action lock text-warning" title="${u.isActive==1?'Khóa tài khoản':'Mở khóa'}" onclick="updateStatus(${u.id}, ${u.isActive})">
                                                <i class="fa ${u.isActive==1?'fa-lock':'fa-lock-open'}"></i>
                                            </button>
                                            <button class="btn-action delete text-danger" title="Xóa vào thùng rác" onclick="deleteUser(${u.id})">
                                                <i class="fa fa-trash"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="addUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="AdminController" method="POST">
                <input type="hidden" name="action" value="addUser">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fa fa-user-plus me-2 text-primary"></i>Thêm tài khoản mới</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-12">
                            <label class="form-label">Tên người dùng</label>
                            <input type="text" name="userName" class="form-control" placeholder="Nhập họ và tên..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Địa chỉ Email</label>
                            <input type="email" name="email" class="form-control" placeholder="example@medicare.com" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Mật khẩu ban đầu</label>
                            <input type="password" name="password" class="form-control" placeholder="********" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Phân quyền</label>
                            <select name="role" class="form-select">
                                <option value="doctor">Bác sĩ (Doctor)</option>
                                <option value="staff">Nhân viên (Staff)</option>
                                <option value="patient" selected>Bệnh nhân (Patient)</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="submit" class="btn btn-primary px-4">Tạo tài khoản</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="AdminController" method="POST">
                <input type="hidden" name="action" value="updateUser">
                <input type="hidden" name="id" id="editId">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fa fa-user-edit me-2 text-primary"></i>Cập nhật thông tin</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-12">
                            <label class="form-label">Họ và tên</label>
                            <input type="text" name="userName" id="editName" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Email liên hệ</label>
                            <input type="email" name="email" id="editEmail" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Thay đổi vai trò</label>
                            <select name="role" id="editRole" class="form-select">
                                <option value="doctor">Doctor</option>
                                <option value="staff">Staff</option>
                                <option value="patient">Patient</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary px-4">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // 1. Chuyển Tab (Fix lại action trash)
    function changeRole(role) {
        if (role === "trash") { 
            window.location.href = "AdminController?action=userTrash"; 
            return; 
        }
        let status = document.querySelector("[name=statusFilter]").value;
        window.location.href = "AdminController?action=users&role=" + role + "&statusFilter=" + status;
    }

    // 2. Xóa mềm người dùng (Ném vào thùng rác)
    function deleteUser(id) {
        if (confirm("Xác nhận chuyển người dùng này vào thùng rác?")) {
            window.location.href = "AdminController?action=deleteUser&id=" + id;
        }
    }

    // 3. Khôi phục người dùng từ thùng rác
    function restoreUser(id) {
        if (confirm("Bạn muốn khôi phục người dùng này?")) {
            window.location.href = "AdminController?action=restoreUser&id=" + id;
        }
    }

    // 4. Bật/Tắt trạng thái hoạt động (Khóa tài khoản) - ĐÃ CẬP NHẬT AJAX/FETCH
    function updateStatus(id, currentStatus) {
        let actionText = currentStatus === 1 ? "Khóa tài khoản này?" : "Mở khóa tài khoản này?";
        
        if (confirm(actionText)) {
            let row = document.getElementById("user-" + id);
            if(!row) return; 
            
            fetch("AdminController?action=toggleUser&id=" + id, {
                method: 'GET' 
            })
            .then(response => {
                if (!response.ok) throw new Error('Lỗi mạng hoặc server');
                return response.text(); 
            })
            .then(newStatusText => {
                // Ép kiểu text trả về thành số (1 hoặc 0)
                let newStatus = parseInt(newStatusText.trim());
                
                if (newStatus === 1 || newStatus === 0) {
                    // Cập nhật Badge Trạng thái
                    let statusTd = row.querySelector(".status-badge");
                    if (newStatus === 1) {
                        statusTd.innerHTML = '<span class="badge badge-status bg-success bg-opacity-10 text-success px-3 py-2"><i class="fa fa-circle me-1" style="font-size: 8px;"></i> Active</span>';
                    } else {
                        statusTd.innerHTML = '<span class="badge badge-status bg-danger bg-opacity-10 text-danger px-3 py-2"><i class="fa fa-circle-xmark me-1"></i> Banned</span>';
                    }
                    
                    // Cập nhật Nút bấm ổ khóa
                    let lockBtn = row.querySelector(".btn-action.lock");
                    lockBtn.setAttribute('onclick', 'updateStatus(' + id + ', ' + newStatus + ')');
                    
                    let icon = lockBtn.querySelector('i');
                    if (newStatus === 1) {
                        lockBtn.title = "Khóa tài khoản";
                        icon.className = "fa fa-lock";
                    } else {
                        lockBtn.title = "Mở khóa";
                        icon.className = "fa fa-lock-open";
                    }
                } else {
                    alert("Có lỗi xảy ra khi xử lý kết quả.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Lỗi kết nối. Vui lòng thử lại!");
            });
        }
    }

    // 5. Mở form Edit
    function openEdit(id, name, email, role) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").value = name;
        document.getElementById("editEmail").value = email;
        document.getElementById("editRole").value = role.toLowerCase();
        
        var editModal = new bootstrap.Modal(document.getElementById('editModal'));
        editModal.show();
    }
</script>
</body>
</html>