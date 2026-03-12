<%@page import="entity.User"%>
<%@page import="config.HospitalConfig"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
String loginErr = (String) request.getAttribute("message");
String regErr = (String) request.getAttribute("regError");
String regSuccess = (String) request.getAttribute("regSuccess");

User currentUser = (User) session.getAttribute("user");

// Nếu là staff thì chuyển qua staff dashboard
if(session.getAttribute("staff") != null){
    response.sendRedirect("component/staff/staffDashboard.jsp");
    return;
}

// Nếu là patient thì chuyển qua patient dashboard
if(session.getAttribute("patient") != null){
    response.sendRedirect("component/patient/patientDashboard.jsp");
    return;
}
%>

<!DOCTYPE html>

<html lang="en">
<head>
<meta charset="UTF-8">
<title><%= HospitalConfig.HOSPITAL_NAME %> | Home</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<style>
:root{
--med-blue:#007bff;
--med-dark:#343a40;
}
body{font-family:'Segoe UI',sans-serif;}

.navbar-brand{
font-weight:800;
color:var(--med-blue);
}

.hero-section{
background:linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)),
url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d');
background-size:cover;
background-position:center;
color:white;
padding:120px 0;
}

.service-card{
transition:0.3s;
border:none;
border-radius:15px;
box-shadow:0 4px 15px rgba(0,0,0,0.1);
}

.service-card:hover{
transform:translateY(-10px);
}

.icon-box{
font-size:3rem;
color:var(--med-blue);
margin-bottom:15px;
}

footer{
background:var(--med-dark);
color:#ccc;
padding:40px 0;
}
</style>

</head>

<body>

<nav class="navbar navbar-light bg-white py-3">
<div class="container">

<a class="navbar-brand fs-3" href="index.jsp">
<i class="fas fa-hand-holding-medical"></i>
<%= HospitalConfig.HOSPITAL_NAME %>
</a>

<div class="ms-auto d-flex gap-2">

<% if(currentUser == null){ %>

<button class="btn btn-outline-primary px-4"
data-bs-toggle="modal"
data-bs-target="#loginModal">
Login </button>

<button class="btn btn-primary px-4"
data-bs-toggle="modal"
data-bs-target="#registerModal">
Register </button>

<% } else { %>

<span class="navbar-text fw-bold text-primary me-3">
Xin chào <%= currentUser.getUserName() %>
</span>

<a href="LogoutController"
class="btn btn-outline-danger">
Logout </a>

<% } %>

</div>
</div>
</nav>

<section class="hero-section text-center">

<div class="container">

<h1 class="display-3 fw-bold">
Your Health, Our Mission
</h1>

<p class="lead mb-4 fs-4">
Advanced care for a better life
</p>

<% if(currentUser == null){ %>

<button class="btn btn-primary btn-lg px-5"
data-bs-toggle="modal"
data-bs-target="#registerModal">
Join Us Now </button>

<% } %>

</div>

</section>

<section class="container my-5 py-5">

<div class="row g-4 text-center">

<div class="col-md-4">
<div class="card h-100 p-4 service-card">

<div class="icon-box">
<i class="fas fa-user-md"></i>
</div>

<h4>Expert Doctors</h4>
<p class="text-muted">
Access to world-class specialists
</p>

</div>
</div>

<div class="col-md-4">
<div class="card h-100 p-4 service-card">

<div class="icon-box">
<i class="fas fa-clock"></i>
</div>

<h4>24/7 Support</h4>
<p class="text-muted">
Emergency services anytime
</p>

</div>
</div>

<div class="col-md-4">
<div class="card h-100 p-4 service-card">

<div class="icon-box">
<i class="fas fa-notes-medical"></i>
</div>

<h4>Online Records</h4>
<p class="text-muted">
View medical history online
</p>

</div>
</div>

</div>
</section>

<footer>

<div class="container text-center">

<h5 class="text-white">
<%= HospitalConfig.HOSPITAL_NAME %>
</h5>

<p>Email: <%= HospitalConfig.EMAIL %></p>
<p>Phone: <%= HospitalConfig.PHONE_NUMBER %></p>
<p>Address: <%= HospitalConfig.ADDRESS %></p>

</div>

</footer>

<div class="modal fade" id="loginModal">

<div class="modal-dialog modal-dialog-centered">

<div class="modal-content">

<div class="modal-header bg-primary text-white">
<h5 class="modal-title">Login</h5>
<button class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
</div>

<div class="modal-body">

<form action="LoginController" method="post">

<% if(loginErr != null){ %>

<div class="alert alert-danger">
<%= loginErr %>
</div>
<% } %>

<div class="mb-3">
<label>Email</label>
<input type="email" name="txtEmail"
class="form-control" required>
</div>

<div class="mb-3">
<label>Password</label>
<input type="password" name="txtPassword"
class="form-control" required>
</div>

<button class="btn btn-primary w-100">
Login
</button>

</form>

</div>

</div>
</div>
</div>

<div class="modal fade" id="registerModal">

<div class="modal-dialog modal-dialog-centered">

<div class="modal-content">

<div class="modal-header bg-primary text-white">
<h5 class="modal-title">Register</h5>
<button class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
</div>

<div class="modal-body">

<form action="RegisterController" method="post">

<% if(regErr != null){ %>

<div class="alert alert-danger">
<%= regErr %>
</div>
<% } %>
<% if(regSuccess != null){ %>
<div class="alert alert-success">
<%= regSuccess %>
</div>
<% } %>

<div class="mb-3">
<label>Name</label>
<input type="text" name="username"
class="form-control" required>
</div>

<div class="mb-3">
<label>Email</label>
<input type="email" name="email"
class="form-control" required>
</div>

<div class="mb-3">
<label>Password</label>
<input type="password" name="password"
class="form-control" required>
</div>

<div class="mb-3">
<label>Phone</label>
<input type="text" name="phone"
class="form-control" required>
</div>
<div class="mb-3">
<label>Address</label>
<input type="text" name="address"
class="form-control" required>
</div>

<button class="btn btn-primary w-100">
Register
</button>

</form>

</div>

</div>
</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>

<% if(regSuccess != null || regErr != null){ %>

var registerModal = new bootstrap.Modal(document.getElementById('registerModal'));
registerModal.show();

<% } %>

</script>
</body>
</html>
