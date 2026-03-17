<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thùng rác Phòng Bệnh | Medicare Pro</title>
    
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

        /* Avatar/Icon Room */
        .avatar-trash {
            width: 42px; height: 42px;
            border-radius: 12px;
            background: #fff;
            display: flex; align-items: center; justify-content: center;
            color: #e53e3e; 
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            margin-right: 15px;
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
        <a href="${pageContext.request.contextPath}/AdminController?action=rooms" class="btn btn-back">
            <i class="fa fa-arrow-left me-2"></i> Quay lại quản lý phòng
        </a>
    </div>

    <div class="card">
        <div class="trash-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h3 class="trash-title mb-1">
                        <i class="fa fa-trash-can me-2"></i> Thùng rác Phòng Bệnh
                    </h3>
                    <p class="text-muted mb-0 small">Danh sách các phòng bệnh đã bị xóa tạm thời khỏi hệ thống.</p>
                </div>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle bg-white">
                <thead>
                    <tr>
                        <th class="ps-4 text-center">ID</th>
                        <th>Thông tin Phòng</th>
                        <th>Trực thuộc Khoa</th>
                        <th>Loại Phòng</th>
                        <th class="text-end">Đơn giá</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="room" items="${roomList}">
                        <tr>
                            <td class="ps-4 text-center text-muted fw-bold">#${room.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-trash">
                                        <i class="fa fa-door-closed"></i>
                                    </div>
                                    <span class="fw-bold" style="color: var(--text-main);">Phòng ${room.roomNumber}</span>
                                </div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty room.departmentName}">
                                        <span class="fw-semibold text-dark">${room.departmentName}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger bg-opacity-10 text-danger px-2 py-1 small">Chưa phân khoa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="text-muted fw-500">${room.roomTypeName}</span></td>
                            <td class="text-end text-success fw-bold">
                                <fmt:formatNumber value="${room.price}" pattern="#,### VNĐ"/>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/AdminController?action=restoreRoom&id=${room.id}" 
                                   class="btn btn-restore btn-sm"
                                   onclick="return confirm('Bạn có chắc chắn muốn khôi phục phòng này?');">
                                    <i class="fa fa-undo me-2"></i> Khôi phục
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty roomList}">
                        <tr>
                            <td colspan="6">
                                <div class="empty-state">
                                    <i class="fa fa-box-open"></i>
                                    <h5 class="fw-bold text-muted">Thùng rác đang trống</h5>
                                    <p class="text-muted small">Hiện tại không có phòng bệnh nào bị xóa tạm thời.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-4 text-center">
        <p class="text-muted small italic"><i class="fa fa-info-circle me-1"></i> Lưu ý: Khi khôi phục, tình trạng phòng sẽ được đặt mặc định là 'Trống' hoặc trạng thái cũ.</p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>