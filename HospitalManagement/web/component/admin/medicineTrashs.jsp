<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thùng rác Dược phẩm | Medicare Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body { background: #f4f7fe; font-family: 'Plus Jakarta Sans', sans-serif; }
        .card { border: none; border-radius: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.04); overflow: hidden; }
        .trash-header { background: #fff5f5; padding: 1.5rem 2rem; border-bottom: 1px solid #ffe3e3; }
        .table thead th { background: #fdf2f2; color: #c53030; font-size: 0.75rem; text-transform: uppercase; padding: 1.25rem 1rem; border: none; }
        .btn-restore { background: #10b981; color: white; border: none; border-radius: 12px; padding: 8px 18px; font-weight: 600; }
        .btn-restore:hover { background: #059669; color: white; transform: translateY(-2px); }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="mb-4">
        <a href="AdminController?action=medicine" class="btn btn-white shadow-sm border px-4 fw-bold" style="border-radius: 12px;">
            <i class="fa fa-arrow-left me-2"></i> Quay lại kho dược
        </a>
    </div>

    <div class="card">
        <div class="trash-header">
            <h4 class="fw-bold text-danger mb-1"><i class="fa fa-trash-can me-2"></i> Thuốc đã tạm xóa</h4>
            <p class="text-muted small mb-0">Danh sách các loại thuốc đang ở trạng thái ngừng cung cấp hoặc đã xóa mềm.</p>
        </div>
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th class="ps-4">Mã thuốc</th>
                        <th>Tên thuốc</th>
                        <th>Đơn vị</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${trashList}">
                        <tr>
                            <td class="ps-4 text-muted fw-bold">#${m.id}</td>
                            <td><span class="fw-bold">${m.name}</span></td>
                            <td>${m.unit}</td>
                            <td class="text-end pe-4">
                                <a href="AdminController?action=restoreMedicine&id=${m.id}" 
                                   class="btn btn-restore btn-sm" onclick="return confirm('Khôi phục thuốc này vào danh sách bán?')">
                                    <i class="fa fa-undo me-2"></i> Khôi phục
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty trashList}">
                        <tr><td colspan="4" class="text-center py-5 text-muted">Thùng rác đang trống.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>