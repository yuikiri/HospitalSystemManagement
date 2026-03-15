<%-- 
    Document   : profile
    Created on : Mar 15, 2026, 8:41:47 PM
    Author     : Dang Khoa
--%>

<%@page import="entity.User"%>
<%@page import="dao.DoctorDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) request.getAttribute("user");
    DoctorDTO doctor = (DoctorDTO) request.getAttribute("doctor");
    String avatarChar = (doctor != null && doctor.getName() != null && !doctor.getName().isEmpty()) 
                        ? doctor.getName().substring(0, 1).toUpperCase() : "D";
    // Hiển thị thông báo lỗi nếu có từ Servlet
    String errorMsg = (String) session.getAttribute("errorMessage");
%>

<% if (errorMsg != null) { %>
    <div class="alert alert-danger"><%= errorMsg %></div>
<% session.removeAttribute("errorMessage"); } %>

<div class="content-section">
    <div class="row g-4">
        <div class="col-lg-4">
            <div class="profile-card p-4 text-center">
                <div class="avatar-circle">
                    <%= avatarChar %>
                    <div class="camera-badge" style="cursor: pointer;" onclick="document.getElementById('avatarFile').click()">
                        <i class="bi bi-camera-fill"></i>
                    </div>
                </div>
                <form id="avatarForm" action="../../UpdateAvatarController" method="POST" enctype="multipart/form-data" class="d-none">
                    <input type="file" id="avatarFile" name="avatarFile" onchange="document.getElementById('avatarForm').submit()">
                </form>

                <h4 class="fw-bold mb-1"><%= (doctor != null && doctor.getName() != null) ? doctor.getName() : "N/A" %></h4>
                <p class="text-muted small">Mã: #BS-<%= user.getId() %></p>

                <div class="mt-4 px-2">
                    <button class="btn btn-primary w-100 rounded-pill mb-3 py-2 text-start px-4 d-flex align-items-center" onclick="showUpdateModal()">
                        <i class="bi bi-person-check-fill me-3 fs-5"></i> Cập nhật hồ sơ
                    </button>
                    <button class="btn btn-outline-info w-100 rounded-pill mb-3 py-2 text-start px-4 d-flex align-items-center" onclick="location.href = 'requestChangeEmail.jsp'">
                        <i class="bi bi-envelope-at-fill me-3 fs-5"></i> Thay đổi Email
                    </button>
                    <button class="btn btn-outline-danger w-100 rounded-pill py-2 text-start px-4 d-flex align-items-center" onclick="location.href = 'requestChangePassword.jsp'">
                        <i class="bi bi-shield-lock-fill me-3 fs-5"></i> Đổi mật khẩu
                    </button>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="profile-card p-4 h-100">
                <h5 class="text-primary fw-bold mb-4"><i class="bi bi-file-earmark-person-fill me-2"></i> Thông tin cơ bản</h5>
                <div class="row g-3">
                    <div class="col-md-6 border-bottom pb-2">
                        <div class="info-label">Họ và Tên</div>
                        <div class="info-value"><%= (doctor != null && doctor.getName() != null) ? doctor.getName() : "N/A" %></div>
                    </div>
                    <div class="col-md-6 border-bottom pb-2">
                        <div class="info-label">Giới tính</div>
                        <div class="info-value">
                            <i class="bi <%= (doctor != null && doctor.getGender() == 1) ? "bi-gender-male text-primary" : "bi-gender-female text-danger" %> me-2"></i>
                            <%= (doctor != null && doctor.getGender() == 1) ? "Nam" : "Nữ" %>
                        </div>
                    </div>
                    <div class="col-md-6 border-bottom pb-2">
                        <div class="info-label">Số điện thoại</div>
                        <div class="info-value text-primary"><%= (doctor != null && doctor.getPhone() != null) ? doctor.getPhone() : "N/A" %></div>
                    </div>
                    <div class="col-md-6 border-bottom pb-2">
                        <div class="info-label">Vị trí</div>
                        <div class="info-value"><%= (doctor != null && doctor.getPosition() != null) ? doctor.getPosition() : "N/A" %></div>
                    </div>
                    <div class="col-12 border-bottom pb-2">
                        <div class="info-label">Số giấy phép (License)</div>
                        <div class="info-value text-success"><%= (doctor != null && doctor.getLicenseNumber() != null) ? doctor.getLicenseNumber() : "N/A" %></div>
                    </div>
                    <div class="col-12">
                        <div class="info-label">Email đăng ký</div>
                        <div class="info-value text-info"><%= user.getEmail() %></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="updateProfileModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0" style="border-radius: 20px;">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold text-primary">Chỉnh sửa hồ sơ</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="../../UpdateProfileController1" method="POST">
                <div class="modal-body p-4">
                    <input type="hidden" name="doctorId" value="<%= (doctor != null) ? doctor.getId() : "" %>">
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Họ và Tên</label>
                        <input type="text" class="form-control rounded-3" name="name" value="<%= (doctor != null && doctor.getName() != null) ? doctor.getName() : "" %>" required>
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-bold text-muted text-uppercase">Giới tính</label>
                            <select class="form-select rounded-3" name="gender">
                                <option value="1" <%= (doctor != null && doctor.getGender() == 1) ? "selected" : "" %>>Nam</option>
                                <option value="0" <%= (doctor != null && doctor.getGender() == 0) ? "selected" : "" %>>Nữ</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-bold text-muted text-uppercase">Số điện thoại</label>
                            <input type="text" class="form-control rounded-3" name="phone" value="<%= (doctor != null && doctor.getPhone() != null) ? doctor.getPhone() : "" %>" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Chức vụ / Vị trí</label>
                        <input type="text" class="form-control rounded-3" name="position" value="<%= (doctor != null && doctor.getPosition() != null) ? doctor.getPosition() : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold text-muted text-uppercase">Số Giấy Phép</label>
                        <input type="text" class="form-control rounded-3" name="licenseNumber" value="<%= (doctor != null && doctor.getLicenseNumber() != null) ? doctor.getLicenseNumber() : "" %>" required>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>
