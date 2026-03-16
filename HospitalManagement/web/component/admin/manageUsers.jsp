<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Users | Medicare Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #0052cc 100%);
            --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
        }

        body { background: #f4f7fe; font-family: 'Plus Jakarta Sans', sans-serif; }
        
        /* Table Card Styling */
        .card { border: none; border-radius: 20px; box-shadow: var(--soft-shadow); overflow: hidden; }
        .nav-tabs .nav-link { font-weight: 600; border: none; color: #a3aed0; padding: 12px 25px; border-radius: 12px !important; transition: 0.3s; }
        .nav-tabs .nav-link.active { background: #fff; color: #0d6efd; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }

        /* --- MODERN MODAL STYLING --- */
        .modal-content {
            border: none;
            border-radius: 24px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.15);
        }

        .modal-header {
            border-bottom: 1px solid #f1f1f1;
            padding: 1.5rem 2rem;
            background: #fff;
            border-radius: 24px 24px 0 0;
        }

        .modal-title { font-weight: 700; color: #2b3674; letter-spacing: -0.5px; }

        .modal-body { padding: 2rem; }

        .form-label {
            font-weight: 600;
            color: #2b3674;
            margin-bottom: 8px;
            font-size: 0.9rem;
        }

        .form-control, .form-select {
            border-radius: 12px;
            padding: 12px 16px;
            border: 1px solid #e0e5f2;
            background-color: #f8fafb;
            transition: all 0.2s;
        }

        .form-control:focus, .form-select:focus {
            background-color: #fff;
            border-color: #0d6efd;
            box-shadow: 0 0 0 4px rgba(13, 110, 253, 0.1);
        }

        .modal-footer {
            border-top: 1px solid #f1f1f1;
            padding: 1.25rem 2rem;
        }

        /* Buttons */
        .btn-primary {
            background: var(--primary-gradient);
            border: none;
            border-radius: 12px;
            padding: 10px 24px;
            font-weight: 600;
            transition: 0.3s;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 14px rgba(13, 110, 253, 0.2);
        }

        .btn-light {
            border-radius: 12px;
            background: #f4f7fe;
            color: #2b3674;
            font-weight: 600;
            border: none;
        }

        /* Badge Styling */
        .badge-doctor { background: #e8f2ff; color: #0d6efd; }
        .badge-staff { background: #fff4e6; color: #ff8800; }
        .badge-patient { background: #e6fffa; color: #00b5ad; }
        .avatar { width: 40px; height: 40px; border-radius: 12px; }
    </style>
</head>
<body>

<div class="container-fluid p-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-1" style="color: #2b3674;">Quản lý người dùng</h3>
            <p class="text-muted mb-0">Hệ thống quản trị nhân sự Medicare</p>
        </div>
        <button class="btn btn-primary shadow-sm" data-bs-toggle="modal" data-bs-target="#addUserModal">
            <i class="fa fa-plus-circle me-2"></i>Thêm người dùng mới
        </button>
    </div>

    <div class="card p-3 mb-4">
        <form action="AdminController" method="GET" class="row g-3 align-items-center">
            <input type="hidden" name="action" value="users">
            <input type="hidden" name="role" value="${activeTab != null ? activeTab : 'doctor'}">
            <div class="col-md-5">
                <div class="input-group">
                    <span class="input-group-text bg-light border-0"><i class="fa fa-search text-muted"></i></span>
                    <input type="text" class="form-control border-0 bg-light" name="searchQuery" placeholder="Tìm kiếm theo tên hoặc email..." value="${param.searchQuery}">
                </div>
            </div>
            <div class="col-md-3">
                <select class="form-select border-0 bg-light font-weight-bold" name="statusFilter" onchange="this.form.submit()">
                    <option value="1" ${currentStatus == 1 ? 'selected' : ''}>Hoạt động</option>
                    <option value="0" ${currentStatus == 0 ? 'selected' : ''}>Bị khóa</option>
                    <option value="-99" ${currentStatus == -99 ? 'selected' : ''}>Tất cả trạng thái</option>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Lọc kết quả</button>
            </div>
        </form>
    </div>

    <div class="card">
        <div class="p-3 bg-white border-bottom">
            <ul class="nav nav-tabs border-0">
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'doctor' || activeTab == null ? 'active' : ''}" onclick="changeRole('doctor')">Bác sĩ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'staff' ? 'active' : ''}" onclick="changeRole('staff')">Nhân viên</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'patient' ? 'active' : ''}" onclick="changeRole('patient')">Bệnh nhân</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'trash' ? 'active' : ''}" onclick="changeRole('trash')">
                        <i class="fa fa-trash-alt me-2"></i>Thùng rác
                    </a>
                </li>
            </ul>
        </div>
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-light">
                    <tr>
                        <th class="ps-4">ID</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>Vai trò</th>
                        <th>Trạng thái</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="u">
                        <tr id="user-${u.id}">
                            <td class="ps-4 text-muted">#${u.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <img src="https://ui-avatars.com/api/?name=${u.userName}&background=random&bold=true" class="avatar me-3">
                                    <span class="fw-bold text-dark">${u.userName}</span>
                                </div>
                            </td>
                            <td>${u.email}</td>
                            <td>
                                <c:set var="r" value="${u.role.toLowerCase()}"/>
                                <span class="badge ${r=='doctor'?'badge-doctor':(r=='staff'?'badge-staff':'badge-patient')} px-3 py-2">
                                    ${u.role.toUpperCase()}
                                </span>
                            </td>
                            <td class="status-badge">
                                <c:choose>
                                    <c:when test="${u.isActive == 1}"><span class="badge bg-success bg-opacity-10 text-success px-3 py-2">Active</span></c:when>
                                    <c:otherwise><span class="badge bg-danger bg-opacity-10 text-danger px-3 py-2">Banned</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <c:choose>
                                    <c:when test="${activeTab == 'trash'}">
                                        <button class="btn btn-sm btn-light text-success" onclick="restoreUser(${u.id})"><i class="fa fa-undo"></i></button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-light text-primary me-1" onclick="openEdit('${u.id}','${u.userName}','${u.email}','${u.role}')">
                                            <i class="fa fa-pen-nib"></i>
                                        </button>
                                        <button class="btn btn-sm btn-light text-warning me-1" onclick="updateStatus(${u.id}, ${u.isActive==1?0:1})">
                                            <i class="fa ${u.isActive==1?'fa-lock':'fa-lock-open'}"></i>
                                        </button>
                                        <button class="btn btn-sm btn-light text-danger" onclick="deleteUser(${u.id})">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
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
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
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
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
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
    function changeRole(role) {
        if (role === "trash") { window.location = "AdminController?action=trash"; return; }
        let status = document.querySelector("[name=statusFilter]").value;
        window.location = "AdminController?action=users&role=" + role + "&statusFilter=" + status;
    }

    function deleteUser(id) {
        if (confirm("Xác nhận chuyển người dùng này vào thùng rác?")) {
            fetch("AdminController?action=deleteUser&id=" + id).then(() => location.reload());
        }
    }

    function restoreUser(id) {
        fetch("AdminController?action=restoreUser&id=" + id).then(() => location.reload());
    }

    function updateStatus(id, status) {
        fetch("AdminController?action=updateStatus&id=" + id + "&newStatus=" + status).then(() => location.reload());
    }

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