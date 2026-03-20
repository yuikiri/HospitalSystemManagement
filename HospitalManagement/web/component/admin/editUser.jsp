<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
User u = (User) request.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>

<title>Edit User</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<style>

body{
background:#f5f7fb;
font-family:Segoe UI;
}

.card{
border-radius:15px;
box-shadow:0 4px 10px rgba(0,0,0,0.1);
}

</style>

</head>

<body>

<div class="container mt-5">

<div class="card p-4">

<h3 class="mb-4">
<i class="fa fa-edit"></i>
Edit User
</h3>

<form action="AdminController" method="post">

<input type="hidden" name="action" value="updateUser">

<input type="hidden" name="id" value="<%=u.getId()%>">

<div class="mb-3">

<label class="form-label">User Name</label>

<input type="text"
name="userName"
class="form-control"
value="<%=u.getUserName()%>"
required>

</div>

<div class="mb-3">

<label class="form-label">Email</label>

<input type="email"
name="email"
class="form-control"
value="<%=u.getEmail()%>"
required>

</div>

<div class="mb-3">

<label class="form-label">Role</label>

<select name="role" class="form-select">

<option value="ADMIN"
<%=u.getRole().equals("ADMIN") ? "selected" : ""%>>
ADMIN
</option>

<option value="DOCTOR"
<%=u.getRole().equals("DOCTOR") ? "selected" : ""%>>
DOCTOR
</option>

<option value="PATIENT"
<%=u.getRole().equals("PATIENT") ? "selected" : ""%>>
PATIENT
</option>

</select>

</div>

<div class="mt-4">

<button class="btn btn-primary">

<i class="fa fa-save"></i>
Update User

</button>

<a href="AdminController?action=users"
class="btn btn-secondary">

Cancel

</a>

</div>

</form>

</div>

</div>

</body>
</html>