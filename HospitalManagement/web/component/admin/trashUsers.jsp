<%@page import="java.util.List"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
List<User> userList = (List<User>) request.getAttribute("userList");
%>

<!DOCTYPE html>
<html>
<head>

<title>Trash Users</title>

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
background:#dc3545;
color:white;
}

</style>

</head>

<body>

<div class="container mt-5">

<div class="card p-4">

<h3 class="mb-4">

<i class="fa fa-trash"></i>
Trash Users
<div class="mb-3">

<a href="AdminController?action=users" class="btn btn-primary">

<i class="fa fa-arrow-left"></i>
Back to User Management

</a>

</div>


</h3>

<div class="table-responsive">

<table class="table table-hover table-bordered align-middle">

<thead>

<tr>

<th>ID</th>
<th>Name</th>
<th>Email</th>
<th>Role</th>
<th width="150">Action</th>

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

<td><%=u.getRole()%></td>

<td>

<a href="AdminController?action=restoreUser&id=<%=u.getId()%>"
class="btn btn-success btn-sm">

<i class="fa fa-undo"></i>
Restore

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