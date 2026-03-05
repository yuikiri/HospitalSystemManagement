<%-- 
    Document   : staff_dashboard
    Created on : Mar 5, 2026, 6:59:11 PM
    Author     : Dang Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff Dashboard | Medi-Care</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root { --med-blue: #007bff; }
        body { background-color: #f0f2f5; font-family: 'Segoe UI', sans-serif; }
        .navbar-staff { background: white; border-bottom: 2px solid var(--med-blue); }
    </style>
</head>
<body>
    <nav class="navbar navbar-staff px-4 py-2">
        <span class="navbar-brand fw-bold text-primary"><i class="fas fa-user-tie me-2"></i> STAFF PORTAL</span>
        <div class="ms-auto"><span class="me-3">Nhân viên: ${sessionScope.account.username}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger">Đăng xuất</a>
        </div>
    </nav>
    <div class="container mt-5">
        <div class="row g-4">
            <div class="col-md-6">
                <div class="card border-0 shadow-sm p-4 h-100">
                    <h5 class="fw-bold text-primary"><i class="fas fa-info-circle me-2"></i> Thông tin cá nhân</h5>
                    <p class="mt-3"><b>Họ tên:</b> ${sessionScope.account.userName}</p>
                    <p><b>Email:</b> ${sessionScope.account.email}</p>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card border-0 shadow-sm p-4 h-100">
                    <h5 class="fw-bold text-success"><i class="fas fa-calendar-alt me-2"></i> Ca làm việc của tôi</h5>
                    <div class="list-group list-group-flush mt-2">
                        <div class="list-group-item d-flex justify-content-between"><span>Thứ Hai</span><span class="badge bg-success">Ca Chiều</span></div>
                        <div class="list-group-item d-flex justify-content-between"><span>Thứ Ba</span><span class="badge bg-success">Ca Chiều</span></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
