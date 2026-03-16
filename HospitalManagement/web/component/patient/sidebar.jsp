<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .brand-logo {
        height: 80px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-bottom: 1px solid #f1f3f5;
    }
    
    .menu-list {
        padding-top: 20px;
    }

    .nav-item-custom {
        display: flex;
        align-items: center;
        padding: 14px 24px;
        color: var(--text-muted);
        text-decoration: none;
        font-weight: 500;
        border-radius: 0 30px 30px 0; 
        margin-right: 20px;
        margin-bottom: 8px;
        transition: all 0.3s ease;
        cursor: pointer;
    }

    .nav-item-custom:hover {
        background: var(--bg-color);
        color: var(--primary-color);
    }

    .nav-item-custom.active {
        background: var(--primary-light);
        color: var(--primary-color);
        border-left: 5px solid var(--primary-color);
        font-weight: 600;
        box-shadow: 2px 4px 12px rgba(67, 97, 238, 0.15); 
    }

    .nav-icon { 
        width: 30px; 
        font-size: 1.2rem;
    }
</style>

<div class="brand-logo">
    <h3 class="fw-bold text-primary mb-0">
        <i class="fas fa-heartbeat me-2"></i>Medi<span class="text-dark">Care</span>
    </h3>
</div>

<div class="menu-list">
    <a id="menu-info" onclick="loadContent('contents/patientProfile.jsp', this)" class="nav-item-custom active">
        <i class="fas fa-id-card nav-icon"></i> Thông tin cá nhân
    </a>
    
    <a id="menu-booking" onclick="loadContent('${pageContext.request.contextPath}/LoadBookingPageController', this)" class="nav-item-custom">
        <i class="fas fa-calendar-plus nav-icon"></i> Đặt lịch hẹn
    </a>
    
    <a id="menu-history" onclick="loadContent('${pageContext.request.contextPath}/LoadMedicalHistoryController', this)" class="nav-item-custom">
        <i class="fas fa-file-medical-alt nav-icon"></i> Lịch sử khám
    </a>
    
    <a id="menu-doctors" onclick="loadContent('contents/searchDoctor.jsp', this)" class="nav-item-custom">
        <i class="fas fa-user-md nav-icon"></i> Tra cứu Bác sĩ
    </a>
    
    <a id="menu-about" onclick="loadContent('contents/aboutUs.jsp', this)" class="nav-item-custom">
        <i class="fas fa-hand-holding-heart nav-icon"></i> Về chúng tôi
    </a>

    <div class="mt-5 mx-4 border-top pt-4">
        <a href="index" class="nav-item-custom text-danger" style="margin-right: 0;">
            <i class="fas fa-sign-out-alt nav-icon"></i> Đăng xuất
        </a>
    </div>
</div>