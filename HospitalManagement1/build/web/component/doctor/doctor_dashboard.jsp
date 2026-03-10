<%-- 
    Document   : doctor_dashboard
    Created on : Mar 5, 2026, 6:57:09 PM
    Author     : Dang Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doctor Dashboard | Medi-Care</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root { --med-blue: #007bff; --med-dark: #343a40; }
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .sidebar { min-height: 100vh; background: var(--med-dark); color: white; }
        .sidebar .nav-link { color: #ccc; padding: 15px 20px; }
        .sidebar .nav-link.active { color: white; background: rgba(255,255,255,0.1); border-right: 4px solid var(--med-blue); }
        .card-doctor { border: none; border-left: 5px solid var(--med-blue); border-radius: 10px; }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 sidebar px-0">
                <div class="p-4 text-center"><h5 class="fw-bold text-white">BS. ${sessionScope.account.userName}</h5></div>
                <div class="mt-2">
                    <a href="#" class="nav-link active"><i class="fas fa-user-md me-2"></i> Ca trực hôm nay</a>
                    <a href="#" class="nav-link"><i class="fas fa-users me-2"></i> Bệnh nhân chờ</a>
                    <a href="#" class="nav-link"><i class="fas fa-book-medical me-2"></i> Lịch sử khám</a>
                    <hr class="bg-secondary">
                    <a href="${pageContext.request.contextPath}/logout" class="nav-link text-warning"><i class="fas fa-sign-out-alt me-2"></i> Thoát</a>
                </div>
            </nav>
            <main class="col-md-10 py-4 px-4">
                <div class="row g-4 mb-4">
                    <div class="col-md-4">
                        <div class="card card-doctor shadow-sm p-3">
                            <small class="text-muted">Ca trực hiện tại</small>
                            <h4 class="mb-0">Ca Sáng: 07:00 - 11:30</h4>
                        </div>
                    </div>
                </div>
                <div class="card shadow-sm border-0 rounded-4 p-4">
                    <h5 class="fw-bold mb-4">Danh sách bệnh nhân đang chờ</h5>
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr><th>STT</th><th>Bệnh nhân</th><th>Số điện thoại</th><th>Giờ hẹn</th><th>Thao tác</th></tr>
                        </thead>
                        <tbody>
                            <tr><td>01</td><td>Nguyễn Văn A</td><td>0905xxxxxx</td><td>08:15</td>
                                <td><button class="btn btn-sm btn-primary">Bắt đầu khám</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</body>
</html>