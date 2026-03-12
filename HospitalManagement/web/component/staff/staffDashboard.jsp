<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Staff Dashboard | MediCare</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<style>

:root{
    --main-blue:#1a73e8;
}

body{
    background:#f4f6f9;
    font-family:'Segoe UI',sans-serif;
}

/* NAVBAR */

.navbar-staff{
    background:white;
    border-bottom:3px solid var(--main-blue);
}

/* CARD */

.dashboard-card{
    border:none;
    border-radius:16px;
    box-shadow:0 8px 25px rgba(0,0,0,0.08);
    transition:0.3s;
}

.dashboard-card:hover{
    transform:translateY(-5px);
    box-shadow:0 12px 35px rgba(0,0,0,0.15);
}

/* AVATAR */

.avatar-img{
    width:170px;
    height:170px;
    border-radius:50%;
    border:6px solid #e9ecef;
    object-fit:cover;
    transition:0.3s;
}

.avatar-img:hover{
    transform:scale(1.05);
}

/* TITLE */

.card-title{
    font-weight:700;
}

/* INFO */

.info-item{
    margin-bottom:10px;
}

</style>

</head>

<body>

<!-- NAVBAR -->

<nav class="navbar navbar-staff px-4 py-3">

<span class="navbar-brand fw-bold text-primary fs-4">
<i class="fas fa-user-nurse me-2"></i>
STAFF PORTAL
</span>

<div class="ms-auto">

<span class="me-3 fw-semibold">
Nhân viên: ${sessionScope.staff.name}
</span>

<a href="${pageContext.request.contextPath}/LogoutController"
class="btn btn-outline-danger btn-sm">
Đăng xuất
</a>

</div>

</nav>


<!-- DASHBOARD -->

<div class="container mt-5">

<div class="row g-4 align-items-stretch">

<!-- PROFILE CARD -->

<div class="col-lg-4">

<div class="card dashboard-card p-4 h-100">

<h5 class="card-title text-primary">
<i class="fas fa-info-circle me-2"></i>
Thông tin cá nhân
</h5>

<hr>

<div class="info-item">
<b>UserID:</b> ${sessionScope.staff.userId}
</div>

<div class="info-item">
<b>Họ tên:</b> ${sessionScope.staff.name}
</div>

<div class="info-item">
<b>Email:</b> ${sessionScope.staff.email}
</div>

<div class="info-item">
<b>SĐT:</b> ${sessionScope.staff.phone}
</div>

<div class="info-item">
<b>Position:</b> ${sessionScope.staff.position}
</div>

<div class="info-item">
<b>Gender:</b>

<span class="badge ${sessionScope.staff.gender == 1 ? 'bg-primary' : 'bg-danger'}">

${sessionScope.staff.gender == 1 ? "Male" : "Female"}

</span>

</div>

<div class="info-item">
<b>Created At:</b> ${sessionScope.staff.createdAt}
</div>

<a href="${pageContext.request.contextPath}/component/staff/updateProfile.jsp"
class="btn btn-primary w-100 mt-3">

<i class="fas fa-edit me-1"></i>
Update Profile

</a>

</div>

</div>


<!-- AVATAR CARD -->

<div class="col-lg-4 text-center">

<div class="card dashboard-card p-4 h-100">

<h5 class="card-title text-info">
<i class="fas fa-user-circle me-2"></i>
Avatar
</h5>

<hr>

<div class="d-flex justify-content-center align-items-center flex-column">

<img 
src="${empty sessionScope.staff.avatarUrl 
? pageContext.request.contextPath.concat('/images/staff.jpg') 
: pageContext.request.contextPath.concat('/images/').concat(sessionScope.staff.avatarUrl)}"
class="avatar-img"
>

<p class="text-muted mt-3">
Staff Profile Picture
</p>

</div>

</div>

</div>


<!-- SHIFT CARD -->

<div class="col-lg-4">

<div class="card dashboard-card p-4 h-100">

<h5 class="card-title text-success">
<i class="fas fa-calendar-alt me-2"></i>
Ca làm việc của tôi
</h5>

<hr>

<div class="list-group list-group-flush">

<div class="list-group-item d-flex justify-content-between">

<span>Thứ Hai</span>

<span class="badge bg-success">
Ca Chiều
</span>

</div>

<div class="list-group-item d-flex justify-content-between">

<span>Thứ Ba</span>

<span class="badge bg-success">
Ca Chiều
</span>

</div>

<div class="list-group-item d-flex justify-content-between">

<span>Thứ Tư</span>

<span class="badge bg-warning text-dark">
Ca Sáng
</span>

</div>

</div>

</div>

</div>

</div>

</div>

</body>
</html>