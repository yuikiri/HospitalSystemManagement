<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thùng rác Khoa | Medicare Pro</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        :root {
            --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            --bg-main: #f4f7fe;
            --text-main: #2b3674;
            --text-muted: #a3aed0;
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
        }

        /* Table Styling */
        .table { margin-bottom: 0; }
        thead th {
            background: #fdf2f2 !important;
            color: #c53030 !important;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            font-weight: 700;
            padding: 1.25rem 1rem !important;
            border: none !important;
        }
        tbody td {
            padding: 1.25rem 1rem;
            vertical-align: middle;
            border-bottom: 1px solid #f1f5f9;
        }

        /* Icon Department */
        .avatar-trash {
            width: 42px; height: 42px;
            border-radius: 12px;
            background: #fff;
            display: flex; align-items: center; justify-content: center;
            color: #e53e3e; 
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            margin-right: 15px;
            font-weight: 700;
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
    
    <div class="mb-4 d-flex justify-content-between align-items-center">
        <a href="AdminController?action=department" class="btn btn-back">
            <i class="fa fa-arrow-left me-2"></i> Quay lại Danh sách Khoa
        </a>
    </div>

    <div class="card">
        <div class="trash-header">
            <div>
                <h3 class="trash-title mb-1">
                    <i class="fa fa-layer-group me-2"></i> Khoa & Phòng ban đã xóa
                </h3>
                <p class="text-muted mb-0 small">Các đơn vị này hiện đang tạm ngưng và có thể được khôi phục bất cứ lúc nào.</p>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead>
                    <tr>
                        <th class="ps-4" width="100">Mã ID</th>
                        <th>Tên Khoa / Phòng ban</th>
                        <th>Mô tả chi tiết</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${departmentList}">
                        <tr>
                            <td class="ps-4 text-muted fw-bold">#${d.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-trash">
                                        ${d.name.substring(0, 1)}
                                    </div>
                                    <span class="fw-bold" style="color: var(--text-main);">${d.name}</span>
                                </div>
                            </td>
                            <td>
                                <span class="text-muted small fw-500">
                                    ${not empty d.description ? d.description : 'Không có mô tả chi tiết cho khoa này.'}
                                </span>
                            </td>
                            <td class="text-end pe-4">
                                <a href="AdminController?action=restoreDepartment&id=${d.id}" 
                                   class="btn btn-restore btn-sm px-3"
                                   onclick="return confirm('Bạn có chắc chắn muốn khôi phục khoa này?')">
                                    <i class="fa fa-undo me-2"></i> Khôi phục
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty departmentList}">
                        <tr>
                            <td colspan="4">
                                <div class="empty-state">
                                    <i class="fa fa-folder-open"></i>
                                    <h5 class="fw-bold text-muted">Thùng rác đang trống</h5>
                                    <p class="text-muted small">Hiện tại không có khoa nào bị xóa tạm thời.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>