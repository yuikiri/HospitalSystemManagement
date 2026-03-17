<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thùng rác Người dùng | Medicare Pro</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --danger-gradient: linear-gradient(135deg, #ff5f6d 0%, #ffc371 100%);
            --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            --bg-main: #f4f7fe;
            --text-main: #2b3674;
        }

        body { 
            background: var(--bg-main); 
            font-family: 'Plus Jakarta Sans', sans-serif; 
            color: #4a5568;
        }

        .admin-container {
            max-width: 1200px;
            margin: 0 auto;
        }

        /* Card Styling */
        .card { 
            border: none; 
            border-radius: 24px; 
            box-shadow: var(--soft-shadow); 
            background: #ffffff;
            overflow: hidden;
        }

        /* Header Trash */
        .trash-header {
            background: #fff5f5;
            padding: 2rem;
            border-bottom: 1px solid #ffe3e3;
        }

        .trash-title {
            color: #e53e3e;
            font-weight: 800;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
        }

        /* Table Styling */
        .table { margin-bottom: 0; }
        .table thead th {
            background: #fdf2f2;
            color: #c53030;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            font-weight: 700;
            padding: 1.25rem 1rem;
            border: none;
        }
        .table tbody td {
            padding: 1.25rem 1rem;
            vertical-align: middle;
            border-bottom: 1px solid #f1f5f9;
        }

        /* Avatar */
        .avatar-trash {
            width: 40px; height: 40px;
            border-radius: 12px;
            background: #f1f5f9;
            display: flex; align-items: center; justify-content: center;
            color: #94a3b8; font-weight: 700;
            margin-right: 12px;
        }

        /* Buttons */
        .btn-restore {
            background: #10b981;
            color: white;
            border: none;
            border-radius: 12px;
            padding: 8px 20px;
            font-weight: 600;
            transition: 0.3s;
            box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
        }
        .btn-restore:hover {
            background: #059669;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(16, 185, 129, 0.3);
            color: white;
        }

        .btn-back {
            background: white;
            color: var(--text-main);
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            padding: 10px 20px;
            font-weight: 600;
            transition: 0.3s;
        }
        .btn-back:hover {
            background: #f8fafc;
            border-color: #cbd5e1;
            transform: translateX(-5px);
        }

        .empty-state {
            padding: 5rem 2rem;
            text-align: center;
        }
        .empty-state i {
            font-size: 4rem;
            color: #e2e8f0;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>

<div class="container admin-container py-5">
    
    <div class="mb-4">
        <a href="AdminController?action=users" class="btn btn-back">
            <i class="fa fa-arrow-left me-2"></i> Quay lại quản lý người dùng
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="trash-header">
            <h3 class="trash-title">
                <i class="fa fa-trash-can-pulse me-3"></i> Thùng rác Người dùng
            </h3>
            <p class="text-muted mb-0">Danh sách các tài khoản đã bị xóa tạm thời. Bạn có thể khôi phục chúng tại đây.</p>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead>
                    <tr>
                        <th class="ps-4">Mã ID</th>
                        <th>Người dùng</th>
                        <th>Email</th>
                        <th>Vai trò</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty userList}">
                            <c:forEach var="u" items="${userList}">
                                <tr>
                                    <td class="ps-4 text-muted fw-bold">#${u.id}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar-trash">
                                                <i class="fa fa-user-slash"></i>
                                            </div>
                                            <span class="fw-bold text-dark">${u.userName}</span>
                                        </div>
                                    </td>
                                    <td class="text-muted">${u.email}</td>
                                    <td>
                                        <span class="badge bg-light text-dark border px-3 py-2" style="border-radius: 8px;">
                                            ${u.role.toUpperCase()}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <a href="AdminController?action=restoreUser&id=${u.id}" 
                                           class="btn btn-restore btn-sm"
                                           onclick="return confirm('Khôi phục người dùng này?')">
                                            <i class="fa fa-undo me-2"></i> Khôi phục
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5">
                                    <div class="empty-state">
                                        <i class="fa fa-folder-open"></i>
                                        <h5 class="fw-bold text-muted">Thùng rác trống</h5>
                                        <p class="text-muted small">Không có người dùng nào bị xóa gần đây.</p>
                                    </div>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-4 text-center">
        <p class="text-muted small italic">Lưu ý: Dữ liệu trong thùng rác vẫn chiếm tài nguyên hệ thống cho đến khi được xóa vĩnh viễn.</p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>