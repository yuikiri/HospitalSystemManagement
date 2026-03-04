<%@page contentType="text/html" pageEncoding="UTF-8"%>
<aside class="sidebar">
    <nav class="nav-menu">
        <button class="nav-item active">
            <i data-lucide="home" class="nav-icon"></i>
            <span class="nav-label">Home</span>
        </button>
        
        <button class="nav-item active">
            <i data-lucide="addAppointment" class="nav-icon"></i>
            <span class="nav-label">Add appointment</span>
        </button>
        
        <button class="nav-item active">
            <i data-lucide="appointments" class="nav-icon"></i>
            <span class="nav-label">Appointments</span>
        </button>
        
        <button class="nav-item active">
            <i data-lucide="history" class="nav-icon"></i>
            <span class="nav-label">History</span>
        </button>
    </nav>
    
    <div class="stats-card">
        <div class="stats-header">
            <i data-lucide="bar-chart-3" class="stats-icon"></i>
            <span class="stats-title">training network</span>
        </div>
        <p class="stats-subtitle">15 active branches</p>
        <div class="progress-track">
            <div class="progress-fill"></div>
        </div>
    </div>
</aside>