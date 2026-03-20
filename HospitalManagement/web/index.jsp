<%@page import="entity.User"%>
<%@page import="config.HospitalConfig"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String loginErr = (String) request.getAttribute("message"); 
    String regErr = (String) request.getAttribute("regError");  
    String regSuccess = (String) request.getAttribute("regSuccess"); 

    User currentUser = (User) session.getAttribute("user");

    if (session.getAttribute("staff") != null) {
        response.sendRedirect("component/staff/staffDashboard.jsp");
        return;
    }
    if (session.getAttribute("patient") != null) {
        response.sendRedirect("component/patient/patientDashboard.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= HospitalConfig.HOSPITAL_NAME%> | Trang chủ</title>

    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --secondary: #0f172a;
            --light-bg: #f8fafc;
        }

        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: var(--light-bg);
            overflow-x: hidden;
        }

        /* ================= HEADER TẦNG 1 (DÍNH TRÊN TOP) ================= */
        .top-bar {
            background-color: rgba(255, 255, 255, 0.98);
            backdrop-filter: blur(10px);
            padding: 15px 0;
            z-index: 1030;
            box-shadow: 0 4px 15px -3px rgba(0, 0, 0, 0.05);
        }
        .brand-text {
            font-weight: 800;
            color: var(--primary);
            letter-spacing: -0.5px;
            text-decoration: none;
        }
        
        /* ================= HEADER TẦNG 2 (CUỘN THEO CHUỘT) ================= */
        .menu-bar {
            background-color: #ffffff;
            border-top: 1px solid #e2e8f0;
            box-shadow: 0 4px 10px -2px rgba(0, 0, 0, 0.05);
        }
        
        .nav-link {
            font-weight: 600;
            color: var(--secondary) !important;
            position: relative;
            padding: 16px 5px !important; 
            margin: 0 15px;
            text-transform: uppercase;
            font-size: 0.95rem;
        }
        .nav-link::after {
            content: ''; position: absolute; width: 0; height: 3px;
            bottom: 0; left: 50%; transform: translateX(-50%);
            background-color: var(--primary); transition: width 0.3s ease;
            border-radius: 5px 5px 0 0;
        }
        .nav-link:hover::after, .nav-link.active::after { width: 100%; }

        /* ================= HERO SECTION ================= */
        .hero-section {
            background: linear-gradient(135deg, rgba(15, 23, 42, 0.8), rgba(37, 99, 235, 0.7)),
                        url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=2000&auto=format&fit=crop');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            min-height: 80vh; 
            display: flex;
            align-items: center;
            color: white;
        }
        .hero-title { font-weight: 800; font-size: 3.5rem; letter-spacing: -1px; text-shadow: 0 4px 10px rgba(0,0,0,0.3); }
        .hero-subtitle { font-weight: 400; font-size: 1.25rem; opacity: 0.9; }

        .service-card {
            background: #fff; border: 1px solid rgba(0,0,0,0.05); border-radius: 20px;
            padding: 40px 30px; transition: all 0.4s ease; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
            height: 100%; cursor: pointer;
        }
        .service-card:hover { transform: translateY(-15px); box-shadow: 0 20px 25px -5px rgba(37, 99, 235, 0.15); border-color: rgba(37, 99, 235, 0.3); }
        .icon-box {
            width: 80px; height: 80px; background: rgba(37, 99, 235, 0.1); color: var(--primary);
            border-radius: 20px; display: flex; align-items: center; justify-content: center;
            font-size: 2.5rem; margin: 0 auto 20px; transition: 0.3s;
        }
        .service-card:hover .icon-box { background: var(--primary); color: #fff; transform: scale(1.1); }

        .btn-custom { border-radius: 50rem; padding: 12px 30px; font-weight: 600; transition: 0.3s; }
        .btn-primary-custom { background: var(--primary); color: white; border: none; box-shadow: 0 4px 14px rgba(37, 99, 235, 0.4); }
        .btn-primary-custom:hover { background: var(--primary-hover); transform: translateY(-2px); color: white;}
        .modal-content { border-radius: 24px; border: none; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.25); }
        .form-control { border-radius: 12px; padding: 12px 15px; border: 2px solid #e2e8f0; font-weight: 500; }
        .form-control:focus { border-color: var(--primary); box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1); }
        
        footer { background: var(--secondary); color: #94a3b8; padding: 60px 0 30px; }
        
        .password-toggle {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #6c757d;
            font-size: 1.1rem;
            z-index: 10;
        }
        .password-toggle:hover { color: var(--primary); }
    </style>
</head>

<body>

    <div class="top-bar sticky-top">
        <div class="container-fluid px-4 px-xl-5 d-flex justify-content-between align-items-center">
            <a class="brand-text fs-3 d-flex align-items-center" href="index.jsp">
                <i class="fas fa-heartbeat text-primary me-2 fs-2"></i>
                <%= HospitalConfig.HOSPITAL_NAME%>
            </a>
            <div class="d-flex gap-3 align-items-center">
                <% if (currentUser == null) { %>
                    <button class="btn btn-light btn-custom text-primary fw-bold border" data-bs-toggle="modal" data-bs-target="#loginModal">
                        <i class="fas fa-sign-in-alt me-2"></i>Đăng Nhập
                    </button>
                    <button class="btn btn-primary-custom btn-custom" data-bs-toggle="modal" data-bs-target="#registerModal">
                        <i class="fas fa-user-plus me-2"></i>Đăng Ký
                    </button>
                <% } else { %>
                    <div class="dropdown">
                        <button class="btn btn-light btn-custom border dropdown-toggle fw-bold text-primary" type="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-2"></i>Xin chào, <%= currentUser.getUserName()%>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow border-0 rounded-4 mt-2">
                            <li><a class="dropdown-item fw-medium py-2 text-danger" href="LogoutController"><i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                        </ul>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <nav class="navbar navbar-expand-lg menu-bar py-0">
        <div class="container-fluid px-4 px-xl-5">
            <button class="navbar-toggler border-0 my-2" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link active" href="index.jsp">Trang Chủ</a></li>
                    <li class="nav-item"><a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#aboutModal">Về Chúng Tôi</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <section class="hero-section">
        <div class="container text-center text-lg-start">
            <div class="row align-items-center">
                <div class="col-lg-7">
                    <span class="badge bg-primary px-3 py-2 rounded-pill mb-3 fw-bold fs-6">
                        <i class="fas fa-star me-2 text-warning"></i>Dịch vụ Y tế Chuẩn Quốc Tế
                    </span>
                    <h1 class="hero-title mb-4">Sức Khỏe Của Bạn,<br><span class="text-primary" style="text-shadow: none;">Sứ Mệnh Của Chúng Tôi</span></h1>
                    <p class="hero-subtitle mb-5"><%= HospitalConfig.DESCRIPTION%></p>
                    
                    <% if (currentUser == null) { %>
                        <div class="d-flex gap-3 justify-content-center justify-content-lg-start">
                            <button class="btn btn-primary-custom btn-custom btn-lg" data-bs-toggle="modal" data-bs-target="#registerModal">
                                <i class="fas fa-calendar-check me-2"></i>Đặt lịch ngay
                            </button>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
    </section>

    <section class="container my-5 py-5">
        <div class="text-center mb-5">
            <h2 class="fw-bold text-dark mb-3">Dịch Vụ Nổi Bật</h2>
            <p class="text-muted fs-5">Hệ thống chăm sóc sức khỏe toàn diện và hiện đại nhất</p>
        </div>
        <div class="row g-4 text-center">
            <div class="col-md-4">
                <div class="service-card">
                    <div class="icon-box"><i class="fas fa-user-md"></i></div>
                    <h4 class="fw-bold mb-3">Đội Ngũ Chuyên Gia</h4>
                    <p class="text-muted mb-0">Hội tụ các bác sĩ đầu ngành, giàu kinh nghiệm.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="service-card">
                    <div class="icon-box"><i class="fas fa-ambulance"></i></div>
                    <h4 class="fw-bold mb-3"><%= HospitalConfig.WORKING_HOURS%></h4>
                    <p class="text-muted mb-0">Hệ thống cấp cứu luôn túc trực, sẵn sàng hỗ trợ.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="service-card">
                    <div class="icon-box"><i class="fas fa-laptop-medical"></i></div>
                    <h4 class="fw-bold mb-3">Bệnh Án Điện Tử</h4>
                    <p class="text-muted mb-0">Quản lý hồ sơ trực tuyến an toàn, dễ dàng tra cứu.</p>
                </div>
            </div>
        </div>
    </section>

    <footer>
        <div class="container text-center">
            <h4 class="text-white mb-3 fw-bold"><i class="fas fa-heartbeat text-primary me-2"></i><%= HospitalConfig.HOSPITAL_NAME%></h4>
            <p class="mb-1"><strong>Hotline:</strong> <%= HospitalConfig.PHONE_NUMBER%> | <strong>Email:</strong> <%= HospitalConfig.EMAIL%></p>
            <p class="mb-0"><%= HospitalConfig.ADDRESS%></p>
            <hr class="border-secondary mt-4">
            <p class="mb-0 text-muted">&copy; 2026 <%= HospitalConfig.HOSPITAL_NAME%>. All rights reserved.</p>
        </div>
    </footer>

    <div class="modal fade" id="loginModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content p-2">
                <div class="modal-header border-0 pb-0">
                    <h4 class="modal-title fw-bold text-primary">Đăng Nhập</h4>
                    <button class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form action="LoginController" method="post" accept-charset="UTF-8">
                        <% if (loginErr != null) {%>
                        <div class="alert alert-danger rounded-3 fw-medium">
                            <i class="fas fa-exclamation-circle me-2"></i><%= loginErr%>
                        </div>
                        <% } %>
                        <div class="form-floating mb-3">
                            <input type="email" name="txtEmail" class="form-control" id="logEmail" placeholder="Email" value="${param.txtEmail}" required>
                            <label for="logEmail"><i class="fas fa-envelope me-2 text-muted"></i>Địa chỉ Email</label>
                        </div>
                        
                        <div class="form-floating mb-2 position-relative">
                            <input type="password" name="txtPassword" class="form-control" id="logPass" placeholder="Password" required>
                            <label for="logPass"><i class="fas fa-lock me-2 text-muted"></i>Mật khẩu</label>
                            <i class="fas fa-eye password-toggle" onclick="togglePassword('logPass', this)"></i>
                        </div>
                        
                        <div class="text-end mb-4">
                            <a href="${pageContext.request.contextPath}/ForgotPasswordController" class="text-primary text-decoration-none fw-semibold small">Quên mật khẩu?</a>
                        </div>

                        <button type="submit" class="btn btn-primary-custom btn-custom w-100 fs-5 mt-2">Đăng Nhập</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="registerModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content p-2">
                <div class="modal-header border-0 pb-0">
                    <h4 class="modal-title fw-bold text-primary">Đăng Ký Tài Khoản</h4>
                    <button class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form action="RegisterController" method="post" id="regForm" accept-charset="UTF-8" onsubmit="return validatePassword()">
                        <% if (regErr != null) {%>
                        <div class="alert alert-danger rounded-3 fw-medium">
                            <i class="fas fa-exclamation-triangle me-2"></i><%= regErr%>
                        </div>
                        <% } %>
                        <% if (regSuccess != null) {%>
                        <div class="alert alert-success rounded-3 fw-medium">
                            <i class="fas fa-check-circle me-2"></i><%= regSuccess%>
                        </div>
                        <% } %>
                        
                        <div id="passError" class="alert alert-danger rounded-3 fw-medium d-none">
                            <i class="fas fa-times-circle me-2"></i>Mật khẩu nhập lại không khớp!
                        </div>

                        <div class="form-floating mb-3">
                            <input type="text" name="username" class="form-control" placeholder="Họ Tên" 
                                   value="${param.username}" pattern="[a-zA-ZÀ-ỹ\s]+" title="Chỉ nhập chữ cái và khoảng trắng" required>
                            <label><i class="fas fa-user me-2 text-muted"></i>Họ và Tên *</label>
                        </div>
                        
                        <div class="form-floating mb-3">
                            <input type="email" name="email" class="form-control" placeholder="Email" value="${param.email}" required>
                            <label><i class="fas fa-envelope me-2 text-muted"></i>Email *</label>
                        </div>

                        <div class="row g-2 mb-3">
                            <div class="col-6">
                                <div class="form-floating position-relative">
                                    <input type="password" name="password" id="regPass" class="form-control" placeholder="Pass" required>
                                    <label><i class="fas fa-lock me-2 text-muted"></i>Mật khẩu *</label>
                                    <i class="fas fa-eye password-toggle" onclick="togglePassword('regPass', this)"></i>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="form-floating position-relative">
                                    <input type="password" id="regConfirmPass" class="form-control" placeholder="Re-Pass" required>
                                    <label><i class="fas fa-check-double me-2 text-muted"></i>Nhập lại *</label>
                                    <i class="fas fa-eye password-toggle" onclick="togglePassword('regConfirmPass', this)"></i>
                                </div>
                            </div>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="text" name="phone" class="form-control" placeholder="SĐT" 
                                   value="${param.phone}" pattern="[0-9]{10,11}" title="Chỉ nhập số, từ 10-11 ký tự" required>
                            <label><i class="fas fa-phone me-2 text-muted"></i>Số điện thoại *</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input type="text" name="address" class="form-control" placeholder="Địa chỉ" value="${param.address}">
                            <label><i class="fas fa-map-marker-alt me-2 text-muted"></i>Địa chỉ (Tùy chọn)</label>
                        </div>

                        <button type="submit" class="btn btn-primary-custom btn-custom w-100 fs-5">Tạo Tài Khoản</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="aboutModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content p-4 text-center">
                <i class="fas fa-hospital-alt text-primary mb-3" style="font-size: 4rem;"></i>
                <h2 class="fw-bold mb-3"><%= HospitalConfig.HOSPITAL_NAME%></h2>
                <p class="text-muted"><%= HospitalConfig.DESCRIPTION%></p>
                <div class="bg-light p-3 rounded-3 text-start mt-3">
                    <p class="mb-1"><strong><i class="fas fa-map-marker-alt text-primary me-2"></i></strong> <%= HospitalConfig.ADDRESS%></p>
                    <p class="mb-0"><strong><i class="fas fa-phone text-primary me-2"></i></strong> <%= HospitalConfig.PHONE_NUMBER%></p>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // HÀM 1: Ẩn/Hiện Mật Khẩu
        function togglePassword(inputId, iconElement) {
            var input = document.getElementById(inputId);
            if (input.type === "password") {
                input.type = "text";
                iconElement.classList.remove("fa-eye");
                iconElement.classList.add("fa-eye-slash");
            } else {
                input.type = "password";
                iconElement.classList.remove("fa-eye-slash");
                iconElement.classList.add("fa-eye");
            }
        }

        // HÀM 2: Validate xác nhận mật khẩu
        function validatePassword() {
            var pass = document.getElementById("regPass").value;
            var confirmPass = document.getElementById("regConfirmPass").value;
            if (pass !== confirmPass) {
                document.getElementById("passError").classList.remove("d-none"); 
                return false; 
            }
            return true; 
        }

        // HÀM 3: Tự động mở form báo lỗi
        document.addEventListener("DOMContentLoaded", function() {
            <% if (loginErr != null) { %>
                var loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                loginModal.show();
            <% } %>

            <% if (regSuccess != null || regErr != null) { %>
                var registerModal = new bootstrap.Modal(document.getElementById('registerModal'));
                registerModal.show();
            <% } %>
        });
    </script>
</body>
</html>
