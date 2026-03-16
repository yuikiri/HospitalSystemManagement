<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Profile</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

</head>

<body>

<div class="container mt-5">

<div class="card shadow p-4">

<h4 class="mb-4">Cập nhật thông tin cá nhân</h4>

<form action="${pageContext.request.contextPath}/updateProfile" method="post">

<input type="hidden" name="userId" value="${sessionScope.staff.userId}">

<div class="mb-3">
<label>Email</label>
<input type="email" class="form-control" name="email"
value="${sessionScope.staff.email}" required>
</div>

<div class="mb-3">
<label>Họ tên</label>
<input type="text" class="form-control" name="name"
value="${sessionScope.staff.name}" required>
</div>

<div class="mb-3">
<label>Số điện thoại</label>
<input type="text" class="form-control" name="phone"
value="${sessionScope.staff.phone}">
</div>

<div class="mb-3">
<label>Position</label>
<input type="text" class="form-control" name="position"
value="${sessionScope.staff.position}">
</div>

<div class="mb-3">
<label>Gender</label>

<select class="form-control" name="gender">

<option value="1" ${sessionScope.staff.gender == 1 ? "selected" : ""}>
Male
</option>

<option value="0" ${sessionScope.staff.gender == 0 ? "selected" : ""}>
Female
</option>

</select>

</div>

<button type="submit" class="btn btn-primary">
Update Profile
</button>

<a href="${pageContext.request.contextPath}/component/staff/staffDashboard.jsp" class="btn btn-secondary">
    Cancel
</a>

</form>

</div>

</div>

</body>
</html>