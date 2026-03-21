<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu | MediCare</title>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; height: 100vh; display: flex; align-items: center; }
        .card-custom { border-radius: 24px; border: none; box-shadow: 0 10px 25px rgba(0,0,0,0.05); }
        .btn-custom { border-radius: 50rem; padding: 12px; font-weight: 600; transition: 0.3s; }
        .btn-primary-custom { background: #2563eb; color: white; border: none; }
        .btn-primary-custom:hover { background: #1d4ed8; transform: translateY(-2px); }
        .form-control { border-radius: 12px; padding: 12px 15px; border: 2px solid #e2e8f0; }
        .form-control:focus { border-color: #2563eb; box-shadow: none; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card card-custom p-4 p-md-5">
                    <div class="text-center mb-4">
                        <i class="fas fa-user-lock text-primary mb-3" style="font-size: 3rem;"></i>
                        <h3 class="fw-bold text-dark">Khôi Phục Mật Khẩu</h3>
                        <p class="text-muted small">Vui lòng nhập Email đã đăng ký để đặt lại mật khẩu</p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger rounded-3 fw-medium small">
                            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                        </div>
                    </c:if>

                    <form action="ForgotPasswordController" method="POST">
                        <div class="form-floating mb-3">
                            <input type="email" name="email" class="form-control" id="email" placeholder="Email" required>
                            <label for="email"><i class="fas fa-envelope me-2 text-muted"></i>Email của bạn</label>
                        </div>
                        
                        <div class="form-floating mb-3">
                            <input type="password" name="newPassword" class="form-control" id="newPass" placeholder="Mật khẩu mới" required>
                            <label for="newPass"><i class="fas fa-key me-2 text-muted"></i>Mật khẩu mới</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input type="password" name="confirmPassword" class="form-control" id="confirmPass" placeholder="Nhập lại mật khẩu" required>
                            <label for="confirmPass"><i class="fas fa-check-double me-2 text-muted"></i>Nhập lại mật khẩu mới</label>
                        </div>

                        <button type="submit" class="btn btn-primary-custom btn-custom w-100 fs-6 mb-3">
                            <i class="fas fa-paper-plane me-2"></i>Gửi mã xác nhận OTP
                        </button>
                        
                        <div class="text-center">
                            <a href="index.jsp" class="text-decoration-none text-muted fw-medium"><i class="fas fa-arrow-left me-1"></i> Quay lại trang chủ</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>