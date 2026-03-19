<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bảng điều khiển Nhân viên | MediCare</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        :root { --main-blue: #1a73e8; --soft-bg: #f8f9fa; }
        body { background: var(--soft-bg); font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        
        /* NAVBAR */
        .navbar-staff { background: white; border-bottom: 3px solid var(--main-blue); box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        
        /* DASHBOARD CARDS */
        .dashboard-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); transition: all 0.3s ease; background: white; }
        .dashboard-card:hover { transform: translateY(-5px); box-shadow: 0 15px 40px rgba(0,0,0,0.12); }
        
        /* AVATAR */
        .avatar-img { width: 150px; height: 150px; border-radius: 50%; border: 5px solid #f1f3f5; object-fit: cover; }
        
        .card-title { font-weight: 700; font-size: 1.15rem; margin-bottom: 1.5rem; padding-bottom: 0.75rem; border-bottom: 1px solid #f1f3f5; }
        .info-label { color: #6c757d; font-weight: 500; width: 110px; display: inline-block; }
        .info-value { color: #212529; font-weight: 600; }
        
        .btn-schedule { background: linear-gradient(135deg, #28a745 0%, #218838 100%); border: none; color: white; transition: 0.3s; }
        .btn-schedule:hover { background: linear-gradient(135deg, #218838 0%, #1e7e34 100%); transform: scale(1.02); color: white; }
    </style>
</head>
<body>

<nav class="navbar navbar-staff px-4 py-3 sticky-top">
    <div class="container-fluid">
        <span class="navbar-brand fw-bold text-primary fs-4">
            <i class="fas fa-hospital-user me-2"></i>MediCare STAFF
        </span>
        <div class="ms-auto d-flex align-items-center">
            <span class="me-3 fw-semibold">
                Chào, <span class="text-primary">${sessionScope.staff.name}</span>
            </span>
            <a href="${pageContext.request.contextPath}/LogoutController" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                <i class="fas fa-sign-out-alt me-1"></i>Đăng xuất
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="row g-4">
        
        <div class="col-lg-4 col-md-6">
            <div class="card dashboard-card p-4 h-100">
                <h5 class="card-title text-primary">
                    <i class="fas fa-id-badge me-2"></i>Thông tin cá nhân
                </h5>
                <div class="mb-3">
                    <span class="info-label">UserID:</span> 
                    <span class="info-value">#${sessionScope.staff.userId}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Họ tên:</span> 
                    <span class="info-value">${sessionScope.staff.name}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Email:</span> 
                    <span class="info-value text-muted">${sessionScope.staff.email}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Số điện thoại:</span> 
                    <span class="info-value">${sessionScope.staff.phone}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Chức vụ:</span> 
                    <span class="info-value badge bg-info-subtle text-info border border-info-subtle">${sessionScope.staff.position}</span>
                </div>
                <div class="mb-3">
                    <span class="info-label">Giới tính:</span> 
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${sessionScope.staff.gender == 1}">Nam</c:when>
                            <c:otherwise>Nữ</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                
                <a href="${pageContext.request.contextPath}/component/staff/updateProfile.jsp" class="btn btn-primary w-100 mt-auto rounded-pill py-2">
                    <i class="fas fa-user-edit me-2"></i>Cập nhật hồ sơ
                </a>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card dashboard-card p-4 h-100 text-center d-flex flex-column justify-content-center border-top border-4 border-info">
                <div class="mb-3">
                    <img src="${empty sessionScope.staff.avatarUrl ? pageContext.request.contextPath.concat('/images/staff_default.jpg') : pageContext.request.contextPath.concat('/images/').concat(sessionScope.staff.avatarUrl)}" 
                         class="avatar-img shadow-sm" alt="Staff Avatar">
                </div>
                <h4 class="fw-bold text-dark mt-2">${sessionScope.staff.name}</h4>
                <p class="text-muted small">Mã nhân viên: STF-${sessionScope.staff.userId}</p>
                <div class="mt-3">
                    <button class="btn btn-sm btn-outline-secondary rounded-pill">Đổi ảnh đại diện</button>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-12">
            <div class="card dashboard-card p-4 h-100 border-start border-4 border-success">
                <h5 class="card-title text-success">
                    <i class="fas fa-calendar-alt me-2"></i>Lịch trình làm việc
                </h5>
                <p class="text-muted small mb-4">
                    Nhấn vào nút bên dưới để xem chi tiết ca làm việc, phòng trực và các ghi chú nhiệm vụ cụ thể của bạn trong tuần này và tuần tới.
                </p>
                
                <div class="bg-success-subtle p-4 rounded-4 mb-4 text-center border border-success-subtle">
                    <i class="fas fa-user-clock fa-3x text-success mb-3"></i>
                    <h6 class="fw-bold text-success mb-1">Thời Khóa Biểu Cá Nhân</h6>
                    <small class="text-success opacity-75">Dữ liệu cập nhật thời gian thực</small>
                </div>

                <a href="${pageContext.request.contextPath}/MySchedule" class="btn btn-success w-100 rounded-pill py-3 shadow">
    <i class="fas fa-calendar-alt me-2"></i>XEM THỜI KHÓA BIỂU
</a>
                
                <div class="mt-4 p-2 bg-light rounded-3">
                    <small class="text-muted d-block italic">
                        <i class="fas fa-info-circle me-1 text-info"></i>Lưu ý: Mọi thay đổi về ca trực vui lòng liên hệ phòng Admin để được hỗ trợ.
                    </small>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>