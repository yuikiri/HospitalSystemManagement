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
        
        

        <div class="d-flex align-items-center">
            <img src="${not empty sessionScope.account.avatarUrl ? sessionScope.account.avatarUrl : 'https://ui-avatars.com/api/?name=Patient&background=4361ee&color=fff'}" 
                 alt="Avatar" 
                 class="rounded-circle border border-2 border-white shadow-sm" 
                 width="45" height="45" style="object-fit: cover;">
            
            <div class="ms-3 d-none d-md-block">
                <p class="mb-0 fw-semibold text-dark">${sessionScope.account.userName != null ? sessionScope.account.userName : 'User'}</p>
                <p class="mb-0 text-muted" style="font-size: 0.8rem;">Hồ sơ Bệnh nhân</p>
            </div>
        </div>
        
    </div>
</div>