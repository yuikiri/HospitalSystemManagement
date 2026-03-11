<%-- 
    Document   : admin_dashboard
    Created on : Mar 5, 2026, 5:19:55 PM
    Author     : Dang Khoa
--%>
các ô sidebar:
- patient: xóa(status = 0), sua thong tin, tim kiem, total(status=1??, status=0??)
- doctor: them bac si, xóa(status = 0), sua thong tin, tim kiem, total(status=1??, status=0??)
- staff: them nhan vien, xóa(status = 0), sua thong tin, tim kiem, total(status=1??, status=0??)
- Departments: xóa(status = 0), sua thong tin, tim kiem, total(status=1??, status=0??)
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Medi-Care</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root { --med-blue: #007bff; --med-dark: #2c3e50; }
        body { background-color: #f4f4f4; }
        .admin-card { cursor: pointer; transition: 0.3s; border: none; border-radius: 12px; }
        .admin-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .icon-circle { width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="bg-dark p-3 text-white mb-4">
        <div class="container d-flex justify-content-between">
            <span class="fw-bold">HỆ THỐNG QUẢN TRỊ MEDI-CARE</span>
            <a href="${pageContext.request.contextPath}/logout" class="text-white text-decoration-none"><i class="fas fa-power-off"></i></a>
        </div>
    </div>
    <div class="container">
        <h3 class="mb-4 fw-bold">Bảng điều khiển Admin</h3>
        <div class="row g-4 text-center">
            <div class="col-md-3">
                <div class="card admin-card p-4">
                    <div class="icon-circle bg-primary text-white mx-auto"><i class="fas fa-users fs-4"></i></div>
                    <h6>Quản lý User (CRUD)</h6>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card admin-card p-4">
                    <div class="icon-circle bg-success text-white mx-auto"><i class="fas fa-hospital fs-4"></i></div>
                    <h6>Chuyên khoa & Phòng</h6>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card admin-card p-4">
                    <div class="icon-circle bg-info text-white mx-auto"><i class="fas fa-pills fs-4"></i></div>
                    <h6>Danh mục Thuốc</h6>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card admin-card p-4">
                    <div class="icon-circle bg-warning text-white mx-auto"><i class="fas fa-calendar-check fs-4"></i></div>
                    <h6>Phân chia ca (Shifts)</h6>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
