<%@page import="entity.User"%>
<%@page import="config.HospitalConfig"%>
<%@page import="config.HospitalConfig"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // LẤY DỮ LIỆU TỪ SERVER (THÔNG QUA MAIN CONTROLLER)
    String loginErr = (String) request.getAttribute("message"); // Lỗi đăng nhập
    String regErr = (String) request.getAttribute("regError");  // Lỗi đăng ký (trùng email...)
    String regSuccess = (String) request.getAttribute("regSuccess"); // Đăng ký thành công
    
    // Lấy thông tin user nếu đã đăng nhập thành công
    User currentUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= HospitalConfig.HOSPITAL_NAME %> | Home</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            :root {
                --med-blue: #007bff;
                --med-dark: #343a40;
            }
            body { font-family: 'Segoe UI', sans-serif; }
            .navbar-brand { font-weight: 800; color: var(--med-blue); }
            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                    url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=1350&q=80');
                background-size: cover;
                background-position: center;
                color: white;
                padding: 120px 0;
            }
            .service-card { transition: 0.3s; border: none; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
            .service-card:hover { transform: translateY(-10px); }
            .icon-box { font-size: 3rem; color: var(--med-blue); margin-bottom: 15px; }
            footer { background: var(--med-dark); color: #ccc; padding: 40px 0; }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-light bg-white py-3">
            <div class="container">
                <a class="navbar-brand fs-3" href="index.jsp">
                    <i class="fas fa-hand-holding-medical"></i> <%= HospitalConfig.HOSPITAL_NAME %>
                </a>
                <div class="ms-auto d-flex gap-2">
                    <% if(currentUser == null) { %>
                        <button type="button" class="btn btn-outline-primary px-4" data-bs-toggle="modal" data-bs-target="#loginModal">
                            Login
                        </button>                
                        <button class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal" data-bs-target="#registerModal">
                            <i class="fas fa-user-plus me-2"></i> Register
                        </button>
                    <% } else { %>
                        <span class="navbar-text fw-bold me-3 text-primary">Xin chào, <%= currentUser.getUserName() %>!</span>
                        <a href="MainController?action=logout" class="btn btn-outline-danger px-4">Logout</a>
                    <% } %>
                </div>
            </div>
        </nav>

        <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top shadow">
            <div class="container">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="mainNav">
                    <ul class="navbar-nav mx-auto text-uppercase">
                        <li class="nav-item"><a class="nav-link active px-3" href="index.jsp">Home</a></li>
                        <li class="nav-item"><a class="nav-link px-3" href="#" data-bs-toggle="modal" data-bs-target="#aboutModal">About Us</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <% if (regSuccess != null) { %>
            <div class="alert alert-success text-center m-0 fw-bold rounded-0">
                <i class="fas fa-check-circle"></i> <%= regSuccess %>
            </div>
        <% } %>

        <section class="hero-section text-center">
            <div class="container">
                <h1 class="display-3 fw-bold">Your Health, Our Mission</h1>
                <p class="lead mb-4 fs-4">Advanced care for a better life. Register now to manage your health records.</p>
                <% if(currentUser == null) { %>
                    <button class="btn btn-primary btn-lg px-5 shadow" data-bs-toggle="modal" data-bs-target="#registerModal">Join Us Now</button>
                <% } %>
            </div>
        </section>

        <section class="container my-5 py-5">
            <div class="row g-4 text-center">
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-user-md"></i></div>
                        <h4>Expert Doctors</h4>
                        <p class="text-muted">Access to world-class specialists in every field.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-clock"></i></div>
                        <h4>24/7 Support</h4>
                        <p class="text-muted">Emergency services and support whenever you need.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-notes-medical"></i></div>
                        <h4>Online Records</h4>
                        <p class="text-muted">View your medical history and prescriptions online.</p>
                    </div>
                </div>
            </div>
        </section>
            <!--final end page-->
        <footer class="mt-auto">
            <div class="container text-center text-md-start">
                <div class="row gy-4">
                    <div class="col-md-6">
                        <h5 class="fw-bold text-white mb-4"><%= HospitalConfig.HOSPITAL_NAME %></h5>
                        <p><i class="fas fa-envelope text-primary me-2"></i> <b>Email:</b> <%= HospitalConfig.EMAIL %></p>
                        <p><i class="fas fa-phone-alt text-primary me-2"></i> <b>Phone:</b> <%= HospitalConfig.PHONE_NUMBER %></p>
                        <p><i class="fas fa-map-marker-alt text-primary me-2"></i> <%= HospitalConfig.ADDRESS %></p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <h5 class="fw-bold text-white mb-4">Follow Us</h5>
                        <div class="fs-3 d-flex justify-content-md-end justify-content-center gap-3">
                            <a href="#" class="text-white"><i class="fab fa-facebook"></i></a>
                            <a href="#" class="text-white"><i class="fab fa-twitter"></i></a>
                            <a href="#" class="text-white"><i class="fab fa-linkedin"></i></a>
                        </div>
                    </div>
                </div>
                <hr class="my-4 border-secondary">
                <p class="text-center mb-0 small text-secondary">© 2026 Medi-Care Hospital System. All rights reserved.</p>
            </div>
        </footer>

        <div class="modal fade" id="aboutModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Về Chúng Tôi</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4 text-center">
                        <h3 class="text-primary fw-bold"><%= HospitalConfig.HOSPITAL_NAME %></h3>
                        <p class="text-muted mt-3"><%= HospitalConfig.DESCRIPTION %></p>
                        <hr>
                        <h4><i class="fas fa-phone-volume text-success"></i> <%= HospitalConfig.PHONE_NUMBER %></h4>
                        <p class="mb-0 text-muted"><%= HospitalConfig.WORKING_HOURS %></p>
                    </div>
                </div>
            </div>
        </div>
                    <!--Login-->
        <div class="modal fade" id="loginModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Patient Login</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form action="MainController" method="post">
                            <input type="hidden" name="action" value="login">
                            <% if (loginErr != null) { %>
                                <div class="alert alert-danger text-center fw-bold"><i class="fas fa-exclamation-circle"></i> <%= loginErr %></div>
                            <% } %>

                            <div class="mb-3">
                                <label class="form-label fw-bold" for="emailLog">Email Address</label>
                                <input id="emailLog" type="email" name="txtEmail" class="form-control" required placeholder="example@mail.com"
                                       value="<%= request.getAttribute("tempEmail") != null ? request.getAttribute("tempEmail") : "" %>">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold" for="loginPass">Password</label>
                                <div class="input-group">
                                    <input type="password" name="txtPassword" id="loginPass" class="form-control" required placeholder="Enter your password">
                                    <button class="btn btn-outline-secondary toggle-password" type="button" data-target="loginPass">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 py-2 fs-5 mt-2 shadow-sm">Login</button>
                        </form>
                    </div>
                    <div class="modal-footer justify-content-center border-0">
                        <p class="mb-0 text-muted">Don't have an account? 
                            <a href="#" class="text-primary fw-bold text-decoration-none" data-bs-toggle="modal" data-bs-target="#registerModal">Register Now</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
                            <!--register-->
        <div class="modal fade" id="registerModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Create New Patient Account</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form action="MainController" method="post" id="registerForm">
                            <input type="hidden" name="action" value="register">
                            <% if (regErr != null) { %>
                                <div class="alert alert-danger text-center fw-bold"><i class="fas fa-times-circle"></i> <%= regErr %></div>
                            <% } %>

                            <div class="mb-3">
                                <label class="form-label fw-bold" for="nameReg">Full Name</label>
                                <input id="nameReg" type="text" placeholder="Nguyen Van A" name="username" class="form-control" required value="<%= request.getAttribute("tempName")!=null ? request.getAttribute("tempName") : "" %>">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold" for="emailReg">Email Address</label>
                                <input id="emailReg" type="email" placeholder="example@mail.com" name="email" class="form-control" required value="<%= request.getAttribute("tempEmail")!=null ? request.getAttribute("tempEmail") : "" %>">
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold" for="regPass">Password</label>
                                <div class="input-group">
                                    <input type="password" name="password" id="regPass" class="form-control" required placeholder="Min 8 characters">
                                    <button class="btn btn-outline-secondary toggle-password" type="button" data-target="regPass">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold" for="regConfirmPass">Confirm Password</label>
                                <div class="input-group">
                                    <input type="password" name="confirmPassword" id="regConfirmPass" class="form-control" required placeholder="Retype your password">
                                    <button class="btn btn-outline-secondary toggle-password" type="button" data-target="regConfirmPass">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                                <div id="passError" class="text-danger mt-1 fw-bold" style="display: none;">
                                    <i class="fas fa-exclamation-triangle"></i> Mật khẩu không khớp!
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold" for="phoneReg">Phone Number</label>
                                <input id="phoneReg" type="text" placeholder="0123456789" name="phone" class="form-control" required value="<%= request.getAttribute("tempPhone")!=null ? request.getAttribute("tempPhone") : "" %>">
                            </div>
                            <button type="submit" class="btn btn-primary w-100 py-2 fs-5 mt-2 shadow-sm">Sign Up</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            document.addEventListener("DOMContentLoaded", function() {
                // 1. Tự động bật Modal lỗi
                <% if (loginErr != null) { %>
                    new bootstrap.Modal(document.getElementById('loginModal')).show();
                <% } else if (regErr != null) { %>
                    new bootstrap.Modal(document.getElementById('registerModal')).show();
                <% } %>

                // 2. Chức năng Hiện/Ẩn mật khẩu (Con mắt)
                document.querySelectorAll('.toggle-password').forEach(function(button) {
                    button.addEventListener('click', function() {
                        let targetId = this.getAttribute('data-target');
                        let input = document.getElementById(targetId);
                        let icon = this.querySelector('i');
                        
                        if (input.type === "password") {
                            input.type = "text";
                            icon.classList.remove('fa-eye');
                            icon.classList.add('fa-eye-slash'); // Đổi icon thành mắt gạch chéo
                        } else {
                            input.type = "password";
                            icon.classList.remove('fa-eye-slash');
                            icon.classList.add('fa-eye'); // Đổi icon về mắt bình thường
                        }
                    });
                });

                // 3. Kiểm tra mật khẩu khớp nhau khi Đăng ký
                document.getElementById('registerForm').addEventListener('submit', function(e) {
                    let pass = document.getElementById('regPass').value;
                    let confirmPass = document.getElementById('regConfirmPass').value;
                    
                    if (pass !== confirmPass) {
                        e.preventDefault(); // Chặn không cho gửi form
                        document.getElementById('passError').style.display = 'block'; // Hiện chữ đỏ
                    } else {
                        document.getElementById('passError').style.display = 'none'; // Ẩn chữ đỏ
                    }
                });
            });
            
        </script>
    </body>
</html>