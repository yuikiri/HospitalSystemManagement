<%-- 
    Document   : header
    Created on : Mar 14, 2026, 2:22:28 AM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="h-100 px-4 d-flex justify-content-between align-items-center">
    
    <div>
        <h5 class="fw-bold mb-0 text-dark">
            Chào mừng, ${sessionScope.account.userName != null ? sessionScope.account.userName : 'Bệnh nhân'}! 👋
        </h5>
        <small class="text-muted">Chúc bạn một ngày nhiều sức khỏe.</small>
    </div>

    <div class="d-flex align-items-center">
        
        <div class="position-relative me-4" style="cursor: pointer;">
            <i class="fas fa-bell text-muted fs-5"></i>
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                3
            </span>
        </div>

        <div class="dropdown">
            <div class="d-flex align-items-center" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="${not empty sessionScope.account.avatarUrl ? sessionScope.account.avatarUrl : 'https://ui-avatars.com/api/?name=Patient&background=4361ee&color=fff'}" 
                     alt="Avatar" 
                     class="rounded-circle border border-2 border-white shadow-sm" 
                     width="45" height="45" style="object-fit: cover;">
                
                <div class="ms-3 d-none d-md-block">
                    <p class="mb-0 fw-semibold text-dark">${sessionScope.account.userName != null ? sessionScope.account.userName : 'User'}</p>
                    <p class="mb-0 text-muted" style="font-size: 0.8rem;">Hồ sơ Bệnh nhân</p>
                </div>
                <i class="fas fa-chevron-down ms-3 text-muted" style="font-size: 0.8rem;"></i>
            </div>
            
            <ul class="dropdown-menu dropdown-menu-end shadow border-0 rounded-3 mt-2">
                <li><a class="dropdown-item py-2" href="#"><i class="fas fa-cog me-2 text-muted"></i> Cài đặt tài khoản</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item py-2 text-danger" href="logout"><i class="fas fa-sign-out-alt me-2"></i> Đăng xuất</a></li>
            </ul>
        </div>
    </div>
</div>