<%@page contentType="text/html" pageEncoding="UTF-8"%>
<header class="header">
    <div class="header-content">
        <div class="header-left">
            <div class="logo-outer">
                <div class="logo-inner"><div class="logo-dot"></div></div>
            </div>
            <div class="header-titles">
                <h1 class="header-title">Hospital B</h1>
                <p class="header-subtitle">Your health is my passion</p>
            </div>
        </div>
        <div class="header-right">
            <button class="icon-button">
                <i data-lucide="bell" class="icon-bell"></i>
                <span class="notification-dot"></span>
            </button>
            <div class="dropdown">
                <button class="user-profile-btn">
                    <div class="avatar">
                        <img src="placeholder-avatar.jpg" alt="avatar" class="avatar-image" onerror="this.style.display='none'">
                        <span class="avatar-fallback">dr</span>
                    </div>
                    <div class="user-info">
                        <p class="user-name">
                            ${not empty sessionScope.user ? sessionScope.user.username : "guest"}
                        </p>
                        <p class="user-role">Patient</p>
                    </div>
                </button>
                <div class="dropdown-menu">
                    <button class="dropdown-item"><i data-lucide="user" class="dropdown-icon"></i>Feed Back</button>
                    <div class="dropdown-separator"></div>
                    <button class="dropdown-item text-red"><i data-lucide="log-out" class="dropdown-icon"></i>logout</button>
                </div>
            </div>
        </div>
    </div>
</header>