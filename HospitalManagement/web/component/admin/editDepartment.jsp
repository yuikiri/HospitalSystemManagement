<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.DepartmentDTO"%>

<%

DepartmentDTO d = (DepartmentDTO) request.getAttribute("department");

%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Edit Department</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

</head>

<body>

<div class="container mt-5">

<h3>Edit Department</h3>

<form method="post" action="AdminController">

<input type="hidden" name="action" value="updateDepartment">
<input type="hidden" name="id" value="<%=d.getId()%>">

<div class="mb-3">

<label>Name</label>

<input type="text" name="name" class="form-control" value="<%=d.getName()%>" required>

</div>

<div class="mb-3">

<label>Description</label>

<input type="text" name="description" class="form-control" value="<%=d.getDescription()%>">

</div>

<button class="btn btn-primary">Update</button>

<a href="AdminController?action=department" class="btn btn-secondary">Back</a>

</form>

</div>

</body>
</html>