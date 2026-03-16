<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác thực Thay đổi Email | MediCare</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body { background-color: #f4f7fe; font-family: 'Inter', sans-serif; display: flex; align-items: center; justify-content: center; height: 100vh; }
        .otp-card { background: #fff; border-radius: 20px; box-shadow: 0 10px 40px rgba(0,0,0,0.08); padding: 40px; max-width: 450px; width: 100%; text-align: center; border-top: 5px solid #0dcaf0;}
        .icon-box { width: 80px; height: 80px; background: #e0f8f8; color: #0dcaf0; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 35px; margin: 0 auto 20px; }
        .otp-input { letter-spacing: 12px; font-size: 2rem; font-weight: bold; text-align: center; background: #f8f9fc; border: 2px solid #e3e6f0; transition: 0.3s;}
        .otp-input:focus { border-color: #0dcaf0; box-shadow: none; background: #fff; }
    </style>
</head>
<body>
    <div class="otp-card fade-in">
        <div class="icon-box"><i class="fas fa-envelope-open-text"></i></div>
        <h4 class="fw-bold text-dark mb-2">Xác minh Email mới</h4>
        <p class="text-muted small mb-4 px-2">Mã xác thực 6 số đã được gửi đến <b class="text-info">${sessionScope.pendingEmail}</b>. Vui lòng kiểm tra hộp thư.</p>
        
        <c:if test="${not empty requestScope.errorMessage}">
            <div class="alert alert-danger small py-2 rounded-3 text-start"><i class="fas fa-times-circle me-2"></i>${requestScope.errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/VerifyEmailController" method="POST">
            <div class="mb-4 mt-2">
                <input type="text" name="otpCode" class="form-control form-control-lg rounded-3 otp-input" maxlength="6" placeholder="------" required autocomplete="off" autofocus>
            </div>
            <button type="submit" class="btn btn-info btn-lg w-100 rounded-pill fw-bold text-white shadow-sm mb-3">Xác nhận thay đổi</button>
            <a href="${pageContext.request.contextPath}/component/patient/patientDashboard.jsp" class="text-muted small text-decoration-none hover-text-info">Quay lại trang hồ sơ</a>
        </form>
    </div>
</body>
</html>