<%@page import="java.util.List"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
List<User> userList = (List<User>) request.getAttribute("userList");
%>

<!DOCTYPE html>

<html>

<head>

<title>User Management</title>

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

.table thead{
background:#0d6efd;
color:white;
}

.badge-active{
background:#28a745;
}

.badge-inactive{
background:#dc3545;
}

</style>

</head>

<body>

<div class="container mt-5">

<div class="card p-4">

<h3 class="mb-4">
<i class="fa fa-users"></i>
User Management
</h3>


<a href="AdminController" class="btn btn-secondary">

<i class="fa fa-home"></i>
Admin Dashboard

</a>

</div>

<div class="mb-3 text-end">

<a href="AdminController?action=trashUsers" class="btn btn-dark">
<i class="fa fa-trash"></i> Trash
</a>

</div>

<form action="AdminController" method="get" class="row g-3 mb-4">

<input type="hidden" name="action" value="users">

<div class="col-md-5">

<input type="text"
name="email"
class="form-control"
placeholder="Search email...">

</div>

<div class="col-md-3">

<select name="isActive" class="form-select">

<option value="">All Status</option>
<option value="1">Active</option>
<option value="0">Inactive</option>

</select>

</div>

<div class="col-md-2">

<button class="btn btn-primary w-100">

<i class="fa fa-search"></i>
Search

</button>

</div>

<div class="col-md-2">

<a href="AdminController?action=users"
class="btn btn-secondary w-100">

Reset

</a>

</div>

</form>

<div class="table-responsive">

<table class="table table-hover table-bordered align-middle">

<thead>

<tr>

<th>ID</th>
<th>Name</th>
<th>Email</th>
<th>Role</th>
<th>Status</th>
<th width="200">Action</th>

</tr>

</thead>

<tbody>

<%

if(userList != null){

for(User u : userList){

%>

<tr>

<td><%=u.getId()%></td>

<td><%=u.getUserName()%></td>

<td><%=u.getEmail()%></td>

<td>

<span class="badge bg-info text-dark">
<%=u.getRole()%>
</span>

</td>

<td>

<%

if(u.getIsActive() == 1){

%>

<span class="badge badge-active">
Active
</span>

<%

}else if(u.getIsActive() == 0){

%>

<span class="badge badge-inactive">
Inactive
</span>

<%

}

%>

</td>

<td>

<a href="AdminController?action=toggleUser&id=<%=u.getId()%>"
class="btn btn-secondary btn-sm">

<i class="fa fa-toggle-on"></i>

</a>

<a href="AdminController?action=editUser&id=<%=u.getId()%>"
class="btn btn-warning btn-sm">

<i class="fa fa-edit"></i>

</a>

<a href="AdminController?action=deleteUser&id=<%=u.getId()%>"
class="btn btn-danger btn-sm"
onclick="return confirm('Are you sure to delete this user?')">

<i class="fa fa-trash"></i>

</a>

</td>

</tr>

<%

}

}

%>

</tbody>

</table>

</div>

</div>

</div>

</body>

</html>
