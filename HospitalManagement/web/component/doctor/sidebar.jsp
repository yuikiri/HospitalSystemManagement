<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .logo-area {
        padding: 25px 20px; display: flex; align-items: center; 
        color: var(--primary-color); font-weight: bold; font-size: 24px; 
        border-bottom: 1px solid #f1f1f1;
    }
    .nav-item-custom {
        color: var(--text-muted); padding: 15px 20px; font-weight: 500; 
        display: flex; align-items: center; border-radius: 0 50px 50px 0; 
        margin: 4px 10px 4px 0; transition: 0.3s; cursor: pointer; text-decoration: none;
    }
    .nav-item-custom:hover, .nav-item-custom.active {
        background: var(--primary-light); color: var(--primary-color);
    }
</style>

<div class="logo-area">
    <i class="fas fa-heartbeat me-2"></i> MediCare
</div>

<div class="nav flex-column mt-4 px-2">
    <a id="menu-doc-profile" class="nav-item-custom" 
       onclick="loadContent('${pageContext.request.contextPath}/MainController?action=LoadDoctorProfile', this)">
        <i class="fas fa-user-md me-2"></i> Thông tin cá nhân
    </a>
    
    <a id="menu-doc-appointment" class="nav-item-custom" 
       onclick="loadContent('${pageContext.request.contextPath}/MainController?action=LoadGetAppointment', this)">
        <i class="fas fa-clipboard-list me-2"></i> Tiếp nhận Lịch hẹn
    </a>
    
    <a id="menu-doc-shift" class="nav-item-custom" 
       onclick="loadContent('${pageContext.request.contextPath}/MainController?action=LoadDoctorShift', this)">
        <i class="fas fa-calendar-alt me-2"></i> Lịch Ca Trực
    </a>
    
    <a id="menu-doc-history" class="nav-item-custom" 
       onclick="loadContent('${pageContext.request.contextPath}/MainController?action=LoadDoctorHistory', this)">
        <i class="fas fa-history me-2"></i> Lịch sử khám bệnh
    </a>
    
    <div style="margin-top: 100px;">
        <hr class="mx-3 text-muted">
        <a href="${pageContext.request.contextPath}/MainController?action=Logout" class="nav-item-custom text-danger">
            <i class="fas fa-sign-out-alt me-2"></i> Đăng xuất
        </a>
    </div>
</div>