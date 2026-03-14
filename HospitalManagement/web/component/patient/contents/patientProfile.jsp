<%-- 
    Document   : patientProfile
    Created on : Mar 14, 2026
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
    .profile-card { border-radius: 20px; border: none; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff; }
    .info-label { color: var(--text-muted); font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; font-weight: 600; margin-bottom: 5px; }
    .info-value { color: var(--text-main); font-size: 1.1rem; font-weight: 500; margin-bottom: 20px; }
    
    /* Vùng chứa Avatar */
    .avatar-wrapper { position: relative; width: 140px; height: 140px; margin: 0 auto; }
    .avatar-img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; box-shadow: 0 8px 20px rgba(67, 97, 238, 0.2); border: 4px solid #fff; transition: 0.3s; }
    
    /* Nút đổi Avatar đè lên ảnh */
    .change-avt-btn { 
        position: absolute; 
        bottom: 5px; 
        right: 5px; 
        background: var(--primary-color); 
        color: white; 
        border-radius: 50%; 
        width: 35px; height: 35px; 
        display: flex; align-items: center; justify-content: center;
        border: 2px solid white; cursor: pointer; transition: 0.2s;
    }
    .change-avt-btn:hover { transform: scale(1.1); background: #2b3674; }
</style>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show mb-4 shadow-sm rounded-3" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i> ${sessionScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success alert-dismissible fade show mb-4 shadow-sm rounded-3" role="alert">
        <i class="fas fa-check-circle me-2"></i> ${sessionScope.successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<c:set var="defaultAvatar" value="https://ui-avatars.com/api/?name=${sessionScope.user.userName}&background=4361ee&color=fff&size=150" />

<div class="row fade-in">
    <div class="col-md-4 mb-4">
        <div class="profile-card p-4 text-center h-100 d-flex flex-column justify-content-center">
            
            <div class="avatar-wrapper mb-3">
                <img id="currentAvatar" src="${not empty sessionScope.user.avatarUrl ? sessionScope.user.avatarUrl : defaultAvatar}" class="avatar-img" alt="Avatar">
                
                <div class="change-avt-btn" data-bs-toggle="modal" data-bs-target="#changeAvatarModal" title="Đổi ảnh đại diện">
                    <i class="fas fa-camera"></i>
                </div>
            </div>

            <h4 class="fw-bold mb-1">${sessionScope.user.userName != null ? sessionScope.user.userName : 'Bệnh Nhân'}</h4>
            <p class="text-muted mb-4"><i class="fas fa-id-badge me-2"></i>Mã BN: #BN-${patient.id}</p>
            
            <div class="d-grid gap-2 px-3">
                <button class="btn btn-primary rounded-pill shadow-sm" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                    <i class="fas fa-user-edit me-2"></i> Cập nhật hồ sơ
                </button>
                <button class="btn btn-outline-info rounded-pill fw-semibold" data-bs-toggle="modal" data-bs-target="#changeEmailModal">
                    <i class="fas fa-envelope-open-text me-2"></i> Thay đổi Email
                </button>
                <button class="btn btn-outline-danger rounded-pill fw-semibold" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                    <i class="fas fa-key me-2"></i> Đổi Mật khẩu
                </button>
            </div>
        </div>
    </div>

    <div class="col-md-8 mb-4">
        <div class="profile-card p-5 h-100">
            <div class="d-flex justify-content-between border-bottom pb-3 mb-4">
                <h5 class="fw-bold text-primary mb-0"><i class="fas fa-file-medical me-2"></i> Thông tin cơ bản</h5>
            </div>
            
            <div class="row">
                <div class="col-sm-6">
                    <p class="info-label">Họ và tên</p>
                    <p class="info-value">${patient.name}</p>
                </div>
                <div class="col-sm-6">
                    <p class="info-label">Ngày sinh</p>
                    <p class="info-value"><fmt:formatDate value="${patient.dob}" pattern="dd/MM/yyyy" /></p>
                </div>
                <div class="col-sm-6">
                    <p class="info-label">Giới tính</p>
                    <p class="info-value">
                        <c:choose>
                            <c:when test="${patient.gender == 1}"><i class="fas fa-mars text-primary me-2"></i>Nam</c:when>
                            <c:otherwise><i class="fas fa-venus text-danger me-2"></i>Nữ</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="col-sm-6">
                    <p class="info-label">Số điện thoại</p>
                    <p class="info-value">${patient.phone}</p>
                </div>
                <div class="col-md-12">
                    <p class="info-label">Email tài khoản</p>
                    <p class="info-value text-info fw-bold">${sessionScope.user.email}</p>
                </div>
                <div class="col-md-12">
                    <p class="info-label">Địa chỉ liên hệ</p>
                    <p class="info-value mb-0">${patient.address}</p>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editProfileModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-primary"><i class="fas fa-edit me-2"></i> Cập nhật hồ sơ cá nhân</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/UpdateProfileController1" method="POST">
                    <input type="hidden" name="patientId" value="${patient.id}">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Họ và tên</label>
                            <input type="text" name="name" class="form-control rounded-3" value="${patient.name}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control rounded-3" value="${patient.phone}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Ngày sinh</label>
                            <input type="date" name="dob" class="form-control rounded-3" value="<fmt:formatDate value='${patient.dob}' pattern='yyyy-MM-dd'/>" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Giới tính</label>
                            <select name="gender" class="form-select rounded-3">
                                <option value="1" ${patient.gender == 1 ? 'selected' : ''}>Nam</option>
                                <option value="0" ${patient.gender == 0 ? 'selected' : ''}>Nữ</option>
                            </select>
                        </div>

                        <div class="col-md-12">
                            <label class="form-label text-muted fw-semibold">Địa chỉ</label>
                            <textarea name="address" class="form-control rounded-3" rows="2">${patient.address}</textarea>
                        </div>
                    </div>
                    <div class="mt-4 text-end">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary rounded-pill px-4">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="changeEmailModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-info"><i class="fas fa-envelope-open-text me-2"></i> Xác thực thay đổi Email</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/RequestChangeEmailController" method="POST">
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Email hiện tại</label>
                        <input type="email" name="currentEmail" class="form-control rounded-3" placeholder="Nhập lại email đang dùng..." required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Mật khẩu xác nhận</label>
                        <input type="password" name="currentPassword" class="form-control rounded-3" placeholder="Nhập mật khẩu để xác minh..." required>
                    </div>
                    <hr class="text-muted">
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold text-info">Email MỚI muốn đổi</label>
                        <input type="email" name="newEmail" class="form-control rounded-3 border-info" placeholder="Nhập email mới của bạn..." required>
                    </div>
                    <div class="text-end mt-4">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-info text-white rounded-pill px-4 fw-bold">Gửi OTP xác thực</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="changePasswordModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-danger"><i class="fas fa-key me-2"></i> Thay đổi Mật khẩu</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/RequestChangePasswordController" method="POST">
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Mật khẩu hiện tại</label>
                        <input type="password" name="currentPassword" class="form-control rounded-3" placeholder="Nhập mật khẩu đang dùng..." required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Mật khẩu mới</label>
                        <input type="password" name="newPassword" class="form-control rounded-3" placeholder="Nhập mật khẩu mới..." required minlength="6">
                    </div>
                    <div class="mb-4">
                        <label class="form-label text-muted fw-semibold">Nhập lại mật khẩu mới</label>
                        <input type="password" name="confirmPassword" class="form-control rounded-3" placeholder="Xác nhận lại mật khẩu mới..." required minlength="6">
                    </div>
                    <div class="text-end mt-3">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger rounded-pill px-4 fw-bold">Gửi mã OTP</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="changeAvatarModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-primary"><i class="fas fa-image me-2"></i> Cập nhật Ảnh đại diện</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/UpdateAvatarController" method="POST" enctype="multipart/form-data">
                    <div class="mb-4">
                        <label class="form-label text-muted fw-semibold">Chọn ảnh từ thiết bị của bạn</label>
                        <input type="file" name="avatarFile" id="inputFile" class="form-control rounded-3" accept="image/png, image/jpeg, image/jpg" required>
                    </div>
                    
                    <div class="text-center mb-3">
                        <p class="text-muted small mb-2">Xem trước:</p>
                        <img id="previewUpload" src="${not empty sessionScope.user.avatarUrl ? sessionScope.user.avatarUrl : defaultAvatar}" style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 2px solid #ddd;">
                    </div>

                    <div class="text-end">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary rounded-pill px-4">Lưu ảnh</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Xử lý Preview ảnh khi chọn từ máy tính
    document.getElementById('inputFile').addEventListener('change', function(event) {
        const [file] = event.target.files;
        if (file) {
            document.getElementById('previewUpload').src = URL.createObjectURL(file);
        }
    });
</script>