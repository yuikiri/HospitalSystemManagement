<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hospital Admin Dashboard | Modern Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #003d99 100%);
            --sidebar-width: 260px;
            --bg-light: #f8f9fa;
        }

        body {
            background: var(--bg-light);
            font-family: 'Inter', sans-serif;
            overflow-x: hidden;
        }

        /* Sidebar Modern */
        .sidebar {
            min-height: 100vh;
            background: var(--primary-gradient);
            color: white;
            box-shadow: 4px 0 10px rgba(0,0,0,0.1);
            position: fixed;
            width: var(--sidebar-width);
            z-index: 1000;
        }

        .sidebar-brand {
            padding: 2rem 1.5rem;
            font-weight: 700;
            font-size: 1.25rem;
            letter-spacing: 1px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }

        .nav-links {
            padding: 1.5rem 0;
        }

        .sidebar a {
            display: flex;
            align-items: center;
            color: rgba(255,255,255,0.8);
            padding: 12px 25px;
            text-decoration: none;
            transition: all 0.3s;
            margin: 4px 15px;
            border-radius: 10px;
        }

        .sidebar a i {
            width: 30px;
            font-size: 1.1rem;
        }

        .sidebar a:hover, .sidebar a.active {
            background: rgba(255,255,255,0.15);
            color: #fff;
            transform: translateX(5px);
        }

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            padding: 2rem;
            width: calc(100% - var(--sidebar-width));
        }

        /* Dashboard Cards */
        .dashboard-card {
            border: none;
            border-radius: 20px;
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            background: #fff;
            overflow: hidden;
            text-decoration: none !important;
        }

        .dashboard-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        }

        .icon-shape {
            width: 60px;
            height: 60px;
            background: #eef2f7;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            transition: 0.3s;
        }

        .dashboard-card:hover .icon-shape {
            background: var(--primary-gradient);
            color: white !important;
        }

        .card-title {
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 0.5rem;
        }

        .card-desc {
            font-size: 0.9rem;
            color: #7f8c8d;
        }

        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2.5rem;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .sidebar { width: 70px; }
            .sidebar-brand span, .sidebar a span { display: none; }
            .main-content { margin-left: 70px; width: calc(100% - 70px); }
        }
    </style>
</head>
<body>

    <div class="sidebar">
        <div class="sidebar-brand text-center">
            <i class="fa fa-hospital-user me-2"></i>
            <span>MEDIC-ADMIN</span>
        </div>
        
        <div class="nav-links">
            <a href="<%=request.getContextPath()%>/AdminController" class="active">
                <i class="fa fa-th-large"></i> <span>Tổng quan</span>
            </a>
            <a href="<%=request.getContextPath()%>/AdminController?action=users">
                <i class="fa fa-user-md"></i> <span>Người dùng</span>
            </a>
            <a href="<%=request.getContextPath()%>/AdminController?action=department">
                <i class="fa fa-layer-group"></i> <span>Khoa điều trị</span>
            </a>
            <a href="<%=request.getContextPath()%>/AdminController?action=room">
                <i class="fa fa-door-open"></i> <span>Phòng bệnh</span>
            </a>
            <a href="<%=request.getContextPath()%>/AdminController?action=medicine">
                <i class="fa fa-briefcase-medical"></i> <span>Kho dược</span>
            </a>
        </div>
    </div>

    <div class="main-content">
        <div class="header-section">
            <div>
                <h2 class="fw-bold mb-1">Bảng điều khiển</h2>
                <p class="text-muted">Chào mừng trở lại, Quản trị viên!</p>
            </div>
            <div class="user-profile bg-white p-2 rounded-pill shadow-sm d-flex align-items-center">
                <img src="https://ui-avatars.com/api/?name=Admin&background=0D6EFD&color=fff" class="rounded-circle me-2" width="35" alt="avatar">
                <span class="fw-semibold me-3">Admin</span>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-xl-3 col-md-6">
                <a href="<%=request.getContextPath()%>/AdminController?action=users" class="card dashboard-card p-4 text-center h-100">
                    <div class="icon-shape text-primary">
                        <i class="fa fa-users fa-2x"></i>
                    </div>
                    <h5 class="card-title">Người dùng</h5>
                    <p class="card-desc">Quản lý bác sĩ, bệnh nhân & nhân viên</p>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="<%=request.getContextPath()%>/AdminController?action=department" class="card dashboard-card p-4 text-center h-100">
                    <div class="icon-shape text-success">
                        <i class="fa fa-hospital-alt fa-2x"></i>
                    </div>
                    <h5 class="card-title">Chuyên khoa</h5>
                    <p class="card-desc">Cấu trúc các khoa trong bệnh viện</p>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="<%=request.getContextPath()%>/AdminController?action=room" class="card dashboard-card p-4 text-center h-100">
                    <div class="icon-shape text-warning">
                        <i class="fa fa-bed-pulse fa-2x"></i>
                    </div>
                    <h5 class="card-title">Phòng bệnh</h5>
                    <p class="card-desc">Tình trạng phòng và giường bệnh</p>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="<%=request.getContextPath()%>/AdminController?action=medicine" class="card dashboard-card p-4 text-center h-100">
                    <div class="icon-shape text-danger">
                        <i class="fa fa-pills fa-2x"></i>
                    </div>
                    <h5 class="card-title">Dược phẩm</h5>
                    <p class="card-desc">Kiểm kê thuốc và vật tư y tế</p>
                </a>
            </div>
        </div>
    </div>

</body>
</html>