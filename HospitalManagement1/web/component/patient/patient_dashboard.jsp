<%-- 
    Document   : patient_dashboard
    Created on : Mar 9, 2026, 8:10:33 AM
    Author     : Dang Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patient Dashboard | Medi-Care</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        :root { --med-blue: #007bff; --med-dark: #343a40; }
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .sidebar { min-height: 100vh; background: white; border-right: 1px solid #dee2e6; }
        .nav-link { color: var(--med-dark); font-weight: 500; padding: 12px 20px; }
        .nav-link.active { background: #e7f1ff; color: var(--med-blue); border-right: 4px solid var(--med-blue); }
        .stat-card { border: none; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-none d-md-block sidebar px-0">
                <div class="p-4 border-bottom text-center">
                    <h4 class="text-primary fw-bold"><i class="fas fa-hand-holding-medical"></i> MEDI-CARE</h4>
                </div>
                <div class="mt-4">
                    <a href="#" class="nav-link active"><i class="fas fa-home me-2"></i> Thông tin</a>
                    <a href="#" class="nav-link"><i class="fas fa-calendar-check me-2"></i> Đặt lịch khám</a>
                    <a href="#" class="nav-link"><i class="fas fa-clock-rotate-left me-2"></i> Trạng thái hẹn</a>
                    <a href="#" class="nav-link"><i class="fas fa-file-medical me-2"></i> Lịch sử khám</a>
                    <hr>
                    <a href="../logout" class="nav-link text-danger"><i class="fas fa-sign-out-alt me-2"></i> Đăng xuất</a>
                </div>
            </nav>

            <main class="col-md-10 ms-sm-auto px-md-4 py-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4>Chào mừng, ${sessionScope.account.userName}!</h4>
                </div>

                <div class="row g-4">
                    <div class="col-md-8">
                        <div class="card stat-card p-4">
                            
                        </div>
                    </div>
                    <div class="col-md-4">
                        
                    </div>
                </div>
            </main>
        </div>
    </div>
</body>
</html>