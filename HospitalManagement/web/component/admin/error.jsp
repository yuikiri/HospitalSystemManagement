<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lỗi Hệ Thống | Medicare</title>

    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        body { 
            background-color: #f2f6fc; 
            font-family: 'Public Sans', sans-serif;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
        }
        .error-card {
            background: white;
            border-radius: 20px;
            padding: 50px 40px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.05);
            text-align: center;
            max-width: 500px;
            width: 90%;
            border-top: 5px solid #dc3545;
        }
        .error-icon-wrapper {
            width: 100px;
            height: 100px;
            background-color: #fce8e8;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 25px;
        }
        .error-icon {
            font-size: 3.5rem;
            color: #dc3545;
        }
        .error-title {
            color: #2c3e50;
            font-weight: 700;
            font-size: 1.75rem;
            margin-bottom: 15px;
        }
        .error-desc {
            color: #69707a;
            font-size: 1rem;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        .btn-home {
            background-color: #0061f2;
            color: white;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            transition: all 0.3s ease;
        }
        .btn-home:hover {
            background-color: #004fc6;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 97, 242, 0.3);
        }
    </style>
</head>
<body>

    <div class="error-card">
        <div class="error-icon-wrapper">
            <i class="bi bi-exclamation-triangle-fill error-icon"></i>
        </div>
        <h2 class="error-title">Oops! Đã xảy ra sự cố</h2>
        <p class="error-desc">
            Rất xin lỗi, hệ thống không thể xử lý yêu cầu của bạn lúc này. Dữ liệu có thể bị sai định dạng hoặc máy chủ đang quá tải.
        </p>
        
        <a href="${pageContext.request.contextPath}/" class="btn-home">
            <i class="bi bi-house-door-fill me-2"></i> Về lại Trang chủ
        </a>
    </div>

</body>
</html>