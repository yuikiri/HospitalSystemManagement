<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Khoa | Medicare Pro</title>

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
        .nav-tabs { gap: 10px; border: none; }
        .nav-tabs .nav-link { 
            font-weight: 600; 
            border: none; 
            color: var(--text-muted); 
            padding: 12px 24px; 
            border-radius: 12px !important; 
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); 
        }
        .nav-tabs .nav-link:hover { background: #f8fafc; color: #0d6efd; }
        .nav-tabs .nav-link.active { 
            background: #fff; 
            color: #0d6efd; 
            box-shadow: 0 4px 15px rgba(13, 110, 253, 0.12); 
            transform: translateY(-2px);
        }

        /* Table Enhancements */
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
            background: transparent !important;
        }
        tbody tr { transition: all 0.2s ease-in-out; }
        tbody tr:hover {
            background-color: #fafbfc !important;
            transform: scale(1.002);
            box-shadow: inset 4px 0 0 0 #0d6efd;
        }

        /* Inputs */
        .search-box, .filter-select {
            border-radius: 14px;
            border: 1px solid #e2e8f0;
            background-color: #fff !important;
            padding: 12px 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.02);
        }
        
        /* Action Buttons */
        .btn-action {
            border-radius: 10px;
            width: 36px;
            height: 36px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            transition: all 0.2s ease;
            border: none;
            background: #f8fafc;
        }
        .btn-action.edit:hover { background: #0d6efd; color: white !important; transform: translateY(-2px); }
        .btn-action.lock:hover { background: #f59e0b; color: white !important; transform: translateY(-2px); }
        .btn-action.unlock:hover { background: #10b981; color: white !important; transform: translateY(-2px); }
        .btn-action.delete:hover { background: #ef4444; color: white !important; transform: translateY(-2px); }

        /* Badges */
        .badge-status { border-radius: 8px; font-weight: 600; font-size: 0.75rem; }

        .avatar-circle {
            width: 40px; height: 40px; 
            background: var(--primary-gradient); 
            color: white;
            border-radius: 12px; 
            display: flex; align-items: center; justify-content: center;
            font-weight: 700; box-shadow: 0 4px 8px rgba(13, 110, 253, 0.2);
        }

        .btn-primary {
            background: var(--primary-gradient);
            border: none; border-radius: 12px;
            padding: 12px 24px; font-weight: 600;
        }
        
        /* Modal Modernization */
        .modal-content { border: none; border-radius: 24px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.2); }
        .modal-header { padding: 1.5rem 2rem; border-bottom: 1px solid #f1f5f9; }
        .modal-body { padding: 2rem; background: #fafbfc; }
        .form-control { border-radius: 12px; padding: 12px 16px; border: 1px solid #e0e5f2; }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">

    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
            <h3 class="fw-bold mb-1" style="color: var(--text-main);">Quản lý Khoa</h3>
            <p class="text-muted mb-0"><i class="fa fa-sitemap me-2"></i>Cơ cấu tổ chức Medicare</p>
        </div>

        <div class="d-flex gap-2">
            <a href="AdminController?action=dashboard" class="btn btn-light shadow-sm">
                <i class="fa fa-arrow-left me-2"></i>Dashboard
            </a>
            <button class="btn btn-primary shadow-sm" data-bs-toggle="modal" data-bs-target="#addModal">
                <i class="fa fa-plus-circle me-2"></i>Thêm khoa mới
            </button>
        </div>
    </div>

    <div class="card p-4 mb-4">
        <form action="AdminController" method="get" class="row g-3 align-items-center">
            <input type="hidden" name="action" value="department">
            <input type="hidden" name="type" value="${activeTab}"> 

            <div class="col-md-7">
                <div class="input-group search-box">
                    <span class="input-group-text border-0 pe-2 bg-transparent"><i class="fa fa-search text-muted"></i></span>
                    <input type="text" name="search" class="form-control border-0 shadow-none p-0" 
                           placeholder="Tìm kiếm theo tên khoa..." value="${currentSearch}">
                </div>
            </div>
            <div class="col-md-3">
                <select name="statusFilter" class="form-select filter-select shadow-none fw-semibold">
                    <option value="-99">⚪ Tất cả trạng thái</option>
                    <option value="1" ${currentStatus == 1 ? 'selected' : ''}>🟢 Đang hoạt động</option>
                    <option value="0" ${currentStatus == 0 ? 'selected' : ''}>🔴 Tạm ngưng</option>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Lọc kết quả</button>
            </div>
        </form>
    </div>

    <div class="card shadow-sm">
        <div class="p-3 border-bottom" style="background: rgba(255,255,255,0.8); backdrop-filter: blur(10px);">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link ${(activeTab == 'all' || empty activeTab) ? 'active' : ''}" 
                       href="AdminController?action=department&type=all">Tất cả</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'khoa' ? 'active' : ''}" 
                       href="AdminController?action=department&type=khoa">Khoa lâm sàng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'phong' ? 'active' : ''}" 
                       href="AdminController?action=department&type=phong">Phòng ban</a>
                </li>
                <li class="nav-item ms-md-auto">
                    <a class="nav-link text-danger" href="AdminController?action=departmentTrash" style="background: #fef2f2;">
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
                        <th>Tên khoa / Phòng ban</th>
                        <th>Mô tả chi tiết</th>
                        <th class="text-center">Trạng thái</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${departmentList}">
                        <tr>
                            <td class="ps-4 text-muted fw-semibold">#${d.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-circle me-3">${d.name.substring(0, 1)}</div>
                                    <span class="fw-bold" style="color: var(--text-main);">${d.name}</span>
                                </div>
                            </td>
                            <td><span class="text-muted small fw-500">${d.description}</span></td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${d.isActive == 1}">
                                        <span class="badge badge-status bg-success bg-opacity-10 text-success px-3 py-2">
                                            <i class="fa fa-check-circle me-1"></i> Active
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-status bg-danger bg-opacity-10 text-danger px-3 py-2">
                                            <i class="fa fa-times-circle me-1"></i> Inactive
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <div class="d-flex justify-content-end gap-2">
                                    <button type="button" class="btn-action edit text-primary" title="Sửa" 
                                            data-bs-toggle="modal" data-bs-target="#editModal"
                                            data-id="${d.id}" data-name="${d.name}" data-description="${d.description}">
                                        <i class="fa fa-pen-nib"></i>
                                    </button>
                                    
                                    <a href="AdminController?action=toggleDepartment&id=${d.id}&status=${d.isActive}" 
                                       class="btn-action ${d.isActive == 1 ? 'lock text-warning' : 'unlock text-success'}" 
                                       title="${d.isActive == 1 ? 'Tạm ngưng' : 'Kích hoạt'}">
                                        <i class="fa ${d.isActive == 1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                                    </a>
                                    
                                    <a href="AdminController?action=deleteDepartment&id=${d.id}" class="btn-action delete text-danger" 
                                       onclick="return confirm('Xác nhận xóa khoa này vào thùng rác?')" title="Xóa">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="AdminController" method="post">
                <input type="hidden" name="action" value="addDepartment">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold text-dark"><i class="fa fa-plus-circle me-2 text-primary"></i>Thêm khoa mới</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-4">
                        <label class="form-label">Tên khoa / Phòng ban</label>
                        <input type="text" name="name" class="form-control" placeholder="Nhập tên đơn vị..." required>
                    </div>
                    <div class="mb-0">
                        <label class="form-label">Mô tả chức năng</label>
                        <textarea name="description" class="form-control" rows="4" placeholder="Mô tả ngắn gọn về khoa..."></textarea>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4">Xác nhận tạo</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="AdminController" method="post">
                <input type="hidden" name="action" value="updateDepartment">
                <input type="hidden" name="id" id="edit-id">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold text-dark"><i class="fa fa-edit me-2 text-primary"></i>Chỉnh sửa thông tin</h5>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-4">
                        <label class="form-label">Tên khoa / Phòng ban</label>
                        <input type="text" name="name" id="edit-name" class="form-control" required>
                    </div>
                    <div class="mb-0">
                        <label class="form-label">Mô tả chi tiết</label>
                        <textarea name="description" id="edit-description" class="form-control" rows="4"></textarea>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const editModal = document.getElementById('editModal');
    if (editModal) {
        editModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            editModal.querySelector('#edit-id').value = button.getAttribute('data-id');
            editModal.querySelector('#edit-name').value = button.getAttribute('data-name');
            editModal.querySelector('#edit-description').value = button.getAttribute('data-description');
        });
    }
</script>

</body>
</html>