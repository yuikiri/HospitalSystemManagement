<%@page import="java.util.List"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Lấy danh sách user từ request attribute (Controller gửi sang)
    List<User> userList = (List<User>) request.getAttribute("userList");

    // 2. Lấy lại các giá trị parameter để hiển thị lại trên Form sau khi Search
    String emailVal = request.getParameter("email");
    if (emailVal == null) emailVal = "";

    String statusVal = request.getParameter("isActive");
    if (statusVal == null) statusVal = "";
%>
<!DOCTYPE html>
<html>
    <head>
        <title>User Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            body { background: #f5f7fb; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .card { border-radius: 15px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); border: none; }
            .table thead { background: #0d6efd; color: white; }
            .badge-active { background: #28a745; color: white; }
            .badge-inactive { background: #dc3545; color: white; }
            .btn-action { margin-right: 5px; }
        </style>
    </head>
    <body>
        <div class="container mt-5">
            <div class="card p-4 mb-4">
                <div class="d-flex justify-content-between align-items-center">
                    <h3 class="m-0"><i class="fa fa-users me-2"></i>User Management</h3>
                    <div>
                        <a href="AdminController" class="btn btn-outline-secondary me-2">
                            <i class="fa fa-home"></i> Dashboard
                        </a>
                        <a href="AdminController?action=trashUsers" class="btn btn-dark">
                            <i class="fa fa-trash"></i> Trash Bin
                        </a>
                    </div>
                </div>
            </div>

            <div class="card p-4 mb-4">
                <form action="AdminController" method="get" class="row g-3">
                    <input type="hidden" name="action" value="users">
                    
                    <div class="col-md-5">
                        <label class="form-label fw-bold">Email Search</label>
                        <input type="text" name="email" class="form-control" 
                               placeholder="Enter email to search..." value="<%= emailVal %>">
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-bold">Status Filter</label>
                        <select name="isActive" class="form-select">
                            <option value="" <%= statusVal.equals("") ? "selected" : "" %>>All Status</option>
                            <option value="1" <%= statusVal.equals("1") ? "selected" : "" %>>Active</option>
                            <option value="0" <%= statusVal.equals("0") ? "selected" : "" %>>Inactive</option>
                        </select>
                    </div>

                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fa fa-search"></i> Search
                        </button>
                    </div>

                    <div class="col-md-2 d-flex align-items-end">
                        <a href="AdminController?action=users" class="btn btn-secondary w-100">
                            <i class="fa fa-refresh"></i> Reset
                        </a>
                    </div>
                </form>
            </div>

            <div class="card p-4">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered align-middle text-center">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th width="180">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (userList != null && !userList.isEmpty()) {
                                    for (User u : userList) {
                            %>
                            <tr>
                                <td><%= u.getId() %></td>
                                <td class="text-start"><%= u.getUserName() %></td>
                                <td class="text-start"><%= u.getEmail() %></td>
                                <td>
                                    <span class="badge bg-info text-dark text-uppercase">
                                        <%= u.getRole() %>
                                    </span>
                                </td>
                                <td>
                                    <% if (u.getIsActive() == 1) { %>
                                        <span class="badge badge-active">Active</span>
                                    <% } else { %>
                                        <span class="badge badge-inactive">Inactive</span>
                                    <% } %>
                                </td>
                                <td>
                                    <a href="AdminController?action=toggleUser&id=<%= u.getId() %>" 
                                       class="btn btn-sm btn-outline-secondary btn-action" title="Toggle Status">
                                        <i class="fa fa-power-off"></i>
                                    </a>
                                    <a href="AdminController?action=editUser&id=<%= u.getId() %>" 
                                       class="btn btn-sm btn-warning btn-action" title="Edit">
                                        <i class="fa fa-edit"></i>
                                    </a>
                                    <a href="AdminController?action=deleteUser&id=<%= u.getId() %>" 
                                       class="btn btn-sm btn-danger btn-action" 
                                       onclick="return confirm('Move this user to trash?')" title="Delete">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center p-4 text-muted">No users found.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>