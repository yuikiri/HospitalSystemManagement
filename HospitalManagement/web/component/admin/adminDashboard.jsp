<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Admin Dashboard | Medicare Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #0052cc 100%);
            --sidebar-width: 280px;
            --bg-main: #f4f7fe;
            --text-main: #2b3674;
            --text-muted: #a3aed0;
        }

        body {
            background: var(--bg-main);
            font-family: 'Plus Jakarta Sans', sans-serif;
            color: #4a5568;
            overflow-x: hidden;
        }

        /* Sidebar Modern */
        .sidebar {
            min-height: 100vh;
            background: var(--primary-gradient);
            color: white;
            box-shadow: 14px 17px 40px 4px rgba(112, 144, 176, 0.08);
            position: fixed;
            width: var(--sidebar-width);
            z-index: 1000;
            transition: all 0.3s ease;
        }

        .sidebar-brand {
            padding: 2.5rem 1.5rem;
            font-weight: 800;
            font-size: 1.4rem;
            letter-spacing: -0.5px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }

        .nav-links { padding: 1.5rem 0; }

        .sidebar a {
            display: flex;
            align-items: center;
            color: rgba(255,255,255,0.7);
            padding: 14px 25px;
            text-decoration: none !important;
            transition: all 0.3s;
            margin: 4px 20px;
            border-radius: 15px;
            font-weight: 500;
        }

        .sidebar a i {
            width: 35px;
            font-size: 1.2rem;
            transition: 0.3s;
        }

        .sidebar a:hover, .sidebar a.active {
            background: rgba(255,255,255,0.2);
            color: #fff;
            transform: translateX(5px);
        }
        
        .sidebar a.active {
            background: #fff;
            color: #0d6efd;
            box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.1);
        }

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            padding: 2.5rem;
            width: calc(100% - var(--sidebar-width));
            transition: all 0.3s;
        }

        /* Dashboard Cards */
        .dashboard-card {
            border: none;
            border-radius: 24px;
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            background: #fff;
            text-decoration: none !important;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.03);
            position: relative;
            overflow: hidden;
        }

        .dashboard-card:hover {
            transform: translateY(-12px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
        }

        .icon-box {
            width: 70px;
            height: 70px;
            background: #f4f7fe;
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            font-size: 1.8rem;
            transition: 0.4s;
        }

        .dashboard-card:hover .icon-box {
            background: var(--primary-gradient);
            color: white !important;
            transform: rotate(-10deg);
        }

        .card-title {
            color: var(--text-main);
            font-weight: 700;
            margin-bottom: 0.7rem;
            font-size: 1.2rem;
        }

        .card-desc {
            font-size: 0.9rem;
            color: var(--text-muted);
            line-height: 1.5;
        }

        /* Stats Section */
        .stat-small-card {
            background: white;
            border-radius: 20px;
            padding: 1.5rem;
            display: flex;
            align-items: center;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.02);
            margin-bottom: 2rem;
        }

        .stat-icon {
            width: 45px; height: 45px;
            border-radius: 12px;
            display: flex; align-items: center; justify-content: center;
            margin-right: 15px;
            font-size: 1.2rem;
        }

        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 3rem;
        }

        .user-profile {
            padding: 8px 15px;
            background: #fff;
            border-radius: 50px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.03);
            border: 1px solid #edf2f7;
        }

        @media (max-width: 992px) {
            .sidebar { width: 85px; }
            .sidebar-brand span, .sidebar a span { display: none; }
            .sidebar a { margin: 10px; padding: 15px; justify-content: center; }
            .sidebar a i { margin: 0; width: auto; }
            .main-content { margin-left: 85px; width: calc(100% - 85px); }
        }
    </style>
</head>
<body>

    <div class="sidebar shadow">
        <div class="sidebar-brand">
            <i class="fa fa-hand-holding-medical me-3 fs-3"></i>
            <span>MEDICARE PRO</span>
        </div>
        
        <div class="nav-links">
            <a href="AdminController" class="active">
                <i class="fa fa-grid-horizontal"></i> <span>Tổng quan</span>
            </a>
            <a href="AdminController?action=users">
                <i class="fa fa-user-gear"></i> <span>Người dùng</span>
            </a>
            <a href="AdminController?action=department">
                <i class="fa fa-hospital-user"></i> <span>Khoa điều trị</span>
            </a>
            <a href="AdminController?action=rooms">
                <i class="fa fa-bed-pulse"></i> <span>Phòng bệnh</span>
            </a>
            <a href="AdminController?action=medicine">
                <i class="fa fa-pills"></i> <span>Kho dược</span>
            </a>

            <div style="margin-top: 100px;">
                <a href="LogoutController" style="color: #ffbaba;">
                    <i class="fa fa-arrow-right-from-bracket"></i> <span>Đăng xuất</span>
                </a>
            </div>
        </div>
    </div>

    <div class="main-content">
        <div class="header-section">
            <div>
                <h2 class="fw-bold mb-1" style="color: var(--text-main);">Dashboard</h2>
                <p class="text-muted mb-0">Hệ thống đang hoạt động ổn định. Chào Admin!</p>
            </div>
            <div class="user-profile d-flex align-items-center">
                <div class="text-end me-3 d-none d-sm-block">
                    <div class="fw-bold text-dark" style="font-size: 0.9rem;">Administrator</div>
                    <div class="text-muted" style="font-size: 0.75rem;">Quản trị viên tối cao</div>
                </div>
                <img src="https://ui-avatars.com/api/?name=Admin&background=0D6EFD&color=fff&bold=true" class="rounded-circle" width="40" alt="avatar">
            </div>
        </div>

        <div class="row mb-2">
            <div class="col-md-3">
                <div class="stat-small-card">
                    <div class="stat-icon bg-primary bg-opacity-10 text-primary"><i class="fa fa-calendar-check"></i></div>
                    <div>
                        <div class="text-muted small fw-semibold">Lịch hẹn mới</div>
                        <div class="fw-bold h5 mb-0">24</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-small-card">
                    <div class="stat-icon bg-success bg-opacity-10 text-success"><i class="fa fa-user-plus"></i></div>
                    <div>
                        <div class="text-muted small fw-semibold">Bệnh nhân mới</div>
                        <div class="fw-bold h5 mb-0">12</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-small-card">
                    <div class="stat-icon bg-warning bg-opacity-10 text-warning"><i class="fa fa-door-open"></i></div>
                    <div>
                        <div class="text-muted small fw-semibold">Phòng trống</div>
                        <div class="fw-bold h5 mb-0">45</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-small-card">
                    <div class="stat-icon bg-danger bg-opacity-10 text-danger"><i class="fa fa-triangle-exclamation"></i></div>
                    <div>
                        <div class="text-muted small fw-semibold">Dược phẩm sắp hết</div>
                        <div class="fw-bold h5 mb-0">08</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-xl-3 col-md-6">
                <a href="AdminController?action=users" class="card dashboard-card p-5 text-center h-100">
                    <div class="icon-box text-primary">
                        <i class="fa fa-users"></i>
                    </div>
                    <h5 class="card-title">Người dùng</h5>
                    <p class="card-desc">Quản lý đội ngũ bác sĩ, nhân viên và hồ sơ bệnh nhân toàn hệ thống.</p>
                    <div class="mt-3 text-primary fw-bold small">Quản lý ngay <i class="fa fa-arrow-right ms-1"></i></div>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="AdminController?action=department" class="card dashboard-card p-5 text-center h-100">
                    <div class="icon-box text-success">
                        <i class="fa fa-hospital"></i>
                    </div>
                    <h5 class="card-title">Chuyên khoa</h5>
                    <p class="card-desc">Thiết lập cấu trúc các khoa lâm sàng, cận lâm sàng và phòng ban chức năng.</p>
                    <div class="mt-3 text-success fw-bold small">Xem cấu trúc <i class="fa fa-arrow-right ms-1"></i></div>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="AdminController?action=rooms" class="card dashboard-card p-5 text-center h-100">
                    <div class="icon-box text-warning">
                        <i class="fa fa-bed"></i>
                    </div>
                    <h5 class="card-title">Phòng bệnh</h5>
                    <p class="card-desc">Theo dõi tình trạng trống/đầy của phòng bệnh và điều phối giường bệnh.</p>
                    <div class="mt-3 text-warning fw-bold small">Kiểm tra phòng <i class="fa fa-arrow-right ms-1"></i></div>
                </a>
            </div>

            <div class="col-xl-3 col-md-6">
                <a href="AdminController?action=medicine" class="card dashboard-card p-5 text-center h-100">
                    <div class="icon-box text-danger">
                        <i class="fa fa-capsules"></i>
                    </div>
                    <h5 class="card-title">Dược phẩm</h5>
                    <p class="card-desc">Quản lý nhập xuất tồn kho thuốc, vật tư y tế và hóa chất xét nghiệm.</p>
                    <div class="mt-3 text-danger fw-bold small">Vào kho dược <i class="fa fa-arrow-right ms-1"></i></div>
                </a>
            </div>
        </div>
        
        <div class="mt-5 pt-4 border-top text-center text-muted small">
            &copy; 2026 Medicare Pro Hospital Management System. Designed for Excellence.
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>