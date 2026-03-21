<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận OTP | MediCare</title>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #f8fafc; height: 100vh; display: flex; align-items: center; }
        .card-custom { border-radius: 24px; border: none; box-shadow: 0 10px 25px rgba(0,0,0,0.05); }
        .btn-custom { border-radius: 50rem; padding: 12px; font-weight: 600; transition: 0.3s; }
        .btn-success-custom { background: #10b981; color: white; border: none; }
        .btn-success-custom:hover { background: #059669; transform: translateY(-2px); }
        .otp-input { letter-spacing: 8px; font-size: 1.5rem; text-align: center; font-weight: bold; border-radius: 15px; border: 2px solid #e2e8f0; padding: 15px; }
        .otp-input:focus { border-color: #10b981; box-shadow: 0 0 0 4px rgba(16, 185, 129, 0.1); outline: none; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card card-custom p-4 p-md-5">
                    <div class="text-center mb-4">
                        <i class="fas fa-shield-alt text-success mb-3" style="font-size: 3rem;"></i>
                        <h3 class="fw-bold text-dark">Xác Thực Mã OTP</h3>
                        <p class="text-muted small">Chúng tôi đã gửi 1 mã OTP gồm 6 chữ số đến Email: <br><b>${sessionScope.forgotEmail}</b></p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger rounded-3 fw-medium text-center small">
                            <i class="fas fa-times-circle me-1"></i>${errorMessage}
                        </div>
                    </c:if>

                    <form action="VerifyForgotPwdController" method="POST">
                        <div class="mb-4">
                            <input type="text" name="otpCode" class="form-control otp-input" placeholder="------" maxlength="6" pattern="[0-9]{6}" title="Mã OTP gồm 6 chữ số" required autocomplete="off" autofocus>
                        </div>

                        <button type="submit" class="btn btn-success-custom btn-custom w-100 fs-6 mb-3">
                            <i class="fas fa-check-circle me-2"></i>Xác Nhận Đổi Mật Khẩu
                        </button>
                        
                        <div class="text-center mt-3">
                            <span class="text-muted small">Chưa nhận được mã? </span>
                            <a href="ForgotPasswordController" class="text-success text-decoration-none fw-bold small">Gửi lại</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>