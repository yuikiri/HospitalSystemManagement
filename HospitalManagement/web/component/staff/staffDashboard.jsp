<%@page import="entity.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // 1. CHỐNG LƯU CACHE TRÌNH DUYỆT (CHỐNG NÚT BACK)
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // 2. KIỂM TRA ĐĂNG NHẬP VÀ ĐÚNG ROLE
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equalsIgnoreCase("staff")) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển Nhân viên | MediCare</title>

    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <style>
        :root { 
            --emerald-main: #10b981; 
            --emerald-dark: #059669;
            --emerald-subtle: #d1fae5;
            --bg-main: #f1f5f9;
            --surface: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border-color: #e2e8f0;
        }

        body { 
            background-color: var(--bg-main); 
            font-family: 'Outfit', sans-serif; 
            color: var(--text-main);
        }
        
        /* --- Nabar Premium --- */
        .navbar-staff { 
            background: var(--surface); 
            border-bottom: 1px solid var(--border-color); 
            box-shadow: 0 4px 20px rgba(0,0,0,0.02); 
            padding: 15px 0;
        }
        .navbar-brand { font-weight: 800; font-size: 1.4rem; letter-spacing: -0.5px; }
        
        /* --- Dashboard Cards --- */
        .premium-card { 
            background: var(--surface); 
            border: 1px solid rgba(255,255,255,0.8); 
            border-radius: 24px; 
            box-shadow: 0 10px 40px rgba(0,0,0,0.03); 
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275); 
            position: relative;
            overflow: hidden;
        }
        .premium-card:hover { 
            transform: translateY(-8px); 
            box-shadow: 0 20px 40px rgba(16, 185, 129, 0.08); 
        }
        
        /* --- Tiêu đề Card --- */
        .card-header-custom {
            display: flex; align-items: center; margin-bottom: 1.5rem; padding-bottom: 1rem; border-bottom: 1px dashed var(--border-color);
        }
        .card-header-custom h5 { margin: 0; font-weight: 700; color: var(--text-main); font-size: 1.2rem; }
        .icon-box { 
            width: 40px; height: 40px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 12px; font-size: 1.2rem;
        }

        /* --- Thông tin cá nhân List --- */
        .info-row { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #f8fafc; }
        .info-row:last-child { border-bottom: none; }
        .info-label { color: var(--text-muted); font-weight: 500; font-size: 0.95rem; display: flex; align-items: center; gap: 8px;}
        .info-value { color: var(--text-main); font-weight: 600; text-align: right;}
        
        /* --- Avatar --- */
        .avatar-container { position: relative; display: inline-block; margin-bottom: 1rem; }
        .avatar-img { 
            width: 140px; height: 140px; border-radius: 50%; border: 4px solid var(--surface); 
            box-shadow: 0 12px 24px rgba(16, 185, 129, 0.2); object-fit: cover;
        }
        .status-dot {
            position: absolute; bottom: 8px; right: 8px; width: 20px; height: 20px; background-color: var(--emerald-main);
            border: 3px solid var(--surface); border-radius: 50%;
        }
        
        /* --- Buttons --- */
        .btn-emerald { 
            background: var(--emerald-main); color: white; font-weight: 600; border-radius: 14px; padding: 12px 20px; border: none; transition: 0.3s;
        }
        .btn-emerald:hover { background: var(--emerald-dark); color: white; transform: scale(1.02); box-shadow: 0 8px 20px rgba(16, 185, 129, 0.2); }
        
        .btn-outline-custom {
            border: 1px solid var(--border-color); color: var(--text-main); font-weight: 600; border-radius: 14px; padding: 10px 20px; transition: 0.3s; background: white;
        }
        .btn-outline-custom:hover { background: #f8fafc; border-color: #cbd5e1; }

        /* Animation */
        .fade-in-up { animation: fadeInUp 0.6s ease-out forwards; opacity: 0; transform: translateY(20px); }
        .delay-1 { animation-delay: 0.1s; }
        .delay-2 { animation-delay: 0.2s; }
        .delay-3 { animation-delay: 0.3s; }
        @keyframes fadeInUp { to { opacity: 1; transform: translateY(0); } }
    </style>
</head>
<body>

<nav class="navbar navbar-staff sticky-top">
    <div class="container-fluid px-4">
        <span class="navbar-brand text-dark">
            <i class="fas fa-hospital-user text-success me-2" style="color: var(--emerald-main) !important;"></i>MediCare <span style="color: var(--emerald-main);">STAFF</span>
        </span>
        <div class="ms-auto d-flex align-items-center gap-4">
            <span class="fw-semibold text-muted">
                Chào mừng, <span style="color: var(--text-main); font-weight: 700;">${sessionScope.staff.name}</span> 👋
            </span>
            <a href="${pageContext.request.contextPath}/LogoutController" class="btn btn-outline-danger btn-sm rounded-pill px-4 fw-bold shadow-sm">
                <i class="fas fa-sign-out-alt me-1"></i> Đăng xuất
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5 mb-5">
    <div class="row g-4">
        
        <div class="col-lg-4 col-md-6 fade-in-up delay-1">
            <div class="premium-card p-4 h-100 d-flex flex-column">
                <div class="card-header-custom">
                    <div class="icon-box bg-primary bg-opacity-10 text-primary">
                        <i class="bi bi-person-vcard"></i>
                    </div>
                    <h5>Hồ sơ của tôi</h5>
                </div>
                
                <div class="info-group flex-grow-1">
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-hash text-muted"></i> UserID</span> 
                        <span class="info-value">#${sessionScope.staff.userId}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-person text-muted"></i> Họ tên</span> 
                        <span class="info-value">${sessionScope.staff.name}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-envelope text-muted"></i> Email</span> 
                        <span class="info-value text-muted" style="font-size: 0.9rem;">${sessionScope.staff.email}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-telephone text-muted"></i> Điện thoại</span> 
                        <span class="info-value">${sessionScope.staff.phone}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-briefcase text-muted"></i> Chức vụ</span> 
                        <span class="info-value badge bg-primary bg-opacity-10 text-primary px-3 py-2 rounded-pill">${sessionScope.staff.position}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="bi bi-gender-ambiguous text-muted"></i> Giới tính</span> 
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${sessionScope.staff.gender == 1}">Nam</c:when>
                                <c:otherwise>Nữ</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
                
                <a href="${pageContext.request.contextPath}/component/staff/updateProfile.jsp" class="btn btn-outline-custom w-100 mt-4 text-center">
                    <i class="bi bi-pencil-square me-2"></i>Chỉnh sửa hồ sơ
                </a>
            </div>
        </div>

        <div class="col-lg-4 col-md-6 fade-in-up delay-2">
            <div class="premium-card p-4 h-100 text-center d-flex flex-column justify-content-center align-items-center" style="border-top: 5px solid var(--emerald-main);">
                
                <div class="avatar-container mt-3">
                    <img src="${empty sessionScope.staff.avatarUrl ? pageContext.request.contextPath.concat('/images/staff_default.jpg') : pageContext.request.contextPath.concat('/images/').concat(sessionScope.staff.avatarUrl)}" 
                         class="avatar-img" alt="Staff Avatar">
                    <div class="status-dot" title="Đang trực tuyến"></div>
                </div>
                
                <h4 class="fw-bold mt-3 mb-1 text-dark">${sessionScope.staff.name}</h4>
                <p class="badge bg-secondary bg-opacity-10 text-secondary px-3 py-2 rounded-pill mb-4" style="font-size: 0.85rem;">Mã NV: STF-${sessionScope.staff.userId}</p>
                
                <button class="btn btn-outline-custom rounded-pill mt-auto mb-2">
                    <i class="bi bi-camera me-2"></i>Đổi ảnh đại diện
                </button>
            </div>
        </div>

        <div class="col-lg-4 col-md-12 fade-in-up delay-3">
            <div class="premium-card p-4 h-100 d-flex flex-column" style="background: linear-gradient(to bottom right, #ffffff, #f0fdf4); border: 1px solid var(--emerald-subtle);">
                <div class="card-header-custom border-bottom-0 pb-0">
                    <div class="icon-box text-white" style="background: var(--emerald-main);">
                        <i class="bi bi-calendar-check"></i>
                    </div>
                    <h5 style="color: var(--emerald-dark);">Quản lý Công việc</h5>
                </div>
                
                <p class="text-muted small mt-3 mb-4" style="line-height: 1.6;">
                    Truy cập vào hệ thống để xem chi tiết ca làm việc, phòng trực và các ghi chú nhiệm vụ cụ thể được phân công trong tuần.
                </p>
                
                <div class="text-center mb-4 py-4 rounded-4" style="background: rgba(16, 185, 129, 0.05); border: 1px dashed rgba(16, 185, 129, 0.3);">
                    <i class="bi bi-calendar-range text-success" style="font-size: 3.5rem; color: var(--emerald-main) !important;"></i>
                    <h5 class="fw-bold mt-3 mb-1" style="color: var(--emerald-dark);">Thời Khóa Biểu</h5>
                    <span class="text-muted small">Dữ liệu cập nhật tự động</span>
                </div>

                <a href="${pageContext.request.contextPath}/MySchedule" class="btn btn-emerald w-100 text-center fs-6 mt-auto">
                    XEM LỊCH TRỰC CÁ NHÂN <i class="bi bi-arrow-right-circle ms-2"></i>
                </a>
                
                <div class="mt-3 text-center">
                    <small class="text-muted" style="font-size: 0.75rem;">
                        <i class="bi bi-info-circle me-1"></i>Liên hệ Admin nếu có sai sót về ca trực.
                    </small>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>