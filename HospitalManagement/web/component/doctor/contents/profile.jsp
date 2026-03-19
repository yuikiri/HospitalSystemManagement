<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    .avatar-wrapper { position: relative; width: 140px; height: 140px; margin: 0 auto; }
    .avatar-img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; box-shadow: 0 8px 20px rgba(13, 110, 253, 0.2); border: 4px solid #fff; transition: 0.3s; }
    .change-avt-btn { 
        position: absolute; bottom: 5px; right: 5px; background: var(--primary-color); color: white; 
        border-radius: 50%; width: 35px; height: 35px; display: flex; align-items: center; justify-content: center;
        border: 2px solid white; cursor: pointer; transition: 0.2s;
    }
    .change-avt-btn:hover { transform: scale(1.1); background: #0a58ca; }
</style>

<c:set var="defaultAvatar" value="https://ui-avatars.com/api/?name=${sessionScope.user.userName}&background=0d6efd&color=fff&size=150" />

<div class="content-section fade-in">
    <h4 class="fw-bold mb-4 text-primary"><i class="bi bi-person-vcard me-2"></i> Hồ Sơ Bác Sĩ</h4>
    <div class="row g-4">
        
        <div class="col-md-4">
            <div class="profile-card p-4 text-center h-100 border bg-white rounded-4 shadow-sm">
                <div class="avatar-wrapper mb-3">
                    <img id="currentAvatar" src="${not empty sessionScope.user.avatarUrl ? sessionScope.user.avatarUrl : defaultAvatar}" class="avatar-img" alt="Avatar">
                    <div class="change-avt-btn" data-bs-toggle="modal" data-bs-target="#changeAvatarModal" title="Đổi ảnh đại diện">
                        <i class="fas fa-camera"></i>
                    </div>
                </div>
                
                <h5 class="fw-bold text-dark mb-1">${doctor.name}</h5>
                <p class="text-primary fw-medium mb-3">${doctor.position}</p>
                <span class="badge bg-light text-secondary border rounded-pill px-3 py-2 mb-4 d-inline-block">
                    <i class="bi bi-award me-1"></i> CCHN: ${doctor.licenseNumber}
                </span>

                <div class="d-grid gap-2 px-2">
                    <button class="btn btn-primary rounded-pill shadow-sm" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                        <i class="bi bi-pencil-square me-2"></i> Cập nhật hồ sơ
                    </button>
                    <button class="btn btn-outline-info rounded-pill fw-semibold" data-bs-toggle="modal" data-bs-target="#changeEmailModal">
                        <i class="bi bi-envelope me-2"></i> Đổi Email
                    </button>
                    <button class="btn btn-outline-danger rounded-pill fw-semibold" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                        <i class="bi bi-key me-2"></i> Đổi Mật khẩu
                    </button>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="profile-card p-4 h-100 border bg-white rounded-4 shadow-sm">
                <h5 class="fw-bold border-bottom pb-3 mb-4 text-dark">Thông tin chi tiết</h5>
                <div class="row">
                    <div class="col-sm-6 mb-4">
                        <p class="text-muted mb-1 text-uppercase fw-bold" style="font-size: 0.8rem;"><i class="bi bi-person me-1"></i> Họ và tên</p>
                        <p class="fs-6 text-dark fw-bold">${doctor.name}</p>
                    </div>
                    <div class="col-sm-6 mb-4">
                        <p class="text-muted mb-1 text-uppercase fw-bold" style="font-size: 0.8rem;"><i class="bi bi-gender-ambiguous me-1"></i> Giới tính</p>
                        <p class="fs-6">${doctor.gender == 1 ? 'Nam' : 'Nữ'}</p>
                    </div>
                    <div class="col-sm-6 mb-4">
                        <p class="text-muted mb-1 text-uppercase fw-bold" style="font-size: 0.8rem;"><i class="bi bi-telephone me-1"></i> Số điện thoại</p>
                        <p class="fs-6">${doctor.phone}</p>
                    </div>
                    <div class="col-sm-6 mb-4">
                        <p class="text-muted mb-1 text-uppercase fw-bold" style="font-size: 0.8rem;"><i class="bi bi-envelope me-1"></i> Email đăng nhập</p>
                        <p class="fs-6 text-info fw-bold">${sessionScope.user.email}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editProfileModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 rounded-4">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-primary"><i class="bi bi-pencil-square me-2"></i> Cập nhật thông tin Bác sĩ</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/UpdateProfileController1" method="POST">
                    <input type="hidden" name="doctorId" value="${doctor.id}">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Họ và tên</label>
                            <input type="text" name="name" class="form-control rounded-3" value="${doctor.name}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control rounded-3" value="${doctor.phone}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Chức vụ chuyên môn</label>
                            <input type="text" name="position" class="form-control rounded-3" value="${doctor.position}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Giới tính</label>
                            <select name="gender" class="form-select rounded-3">
                                <option value="1" ${doctor.gender == 1 ? 'selected' : ''}>Nam</option>
                                <option value="0" ${doctor.gender == 0 ? 'selected' : ''}>Nữ</option>
                            </select>
                        </div>
                        <div class="col-md-12">
                            <label class="form-label text-muted fw-semibold">Số Chứng chỉ hành nghề (CCHN)</label>
                            <input type="text" name="licenseNumber" class="form-control rounded-3" value="${doctor.licenseNumber}" required>
                        </div>
                    </div>
                    <div class="mt-4 text-end">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary rounded-pill px-5">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="changeEmailModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-4">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-info"><i class="bi bi-envelope me-2"></i> Xác thực thay đổi Email</h5>
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
                        <input type="email" name="newEmail" class="form-control rounded-3 border-info" placeholder="Nhập email mới..." required>
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

<div class="modal fade" id="changePasswordModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-4">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-danger"><i class="bi bi-key me-2"></i> Thay đổi Mật khẩu</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/RequestChangePasswordController" method="POST">
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Mật khẩu hiện tại</label>
                        <input type="password" name="currentPassword" class="form-control rounded-3" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted fw-semibold">Mật khẩu mới</label>
                        <input type="password" name="newPassword" class="form-control rounded-3" required minlength="6">
                    </div>
                    <div class="mb-4">
                        <label class="form-label text-muted fw-semibold">Nhập lại mật khẩu mới</label>
                        <input type="password" name="confirmPassword" class="form-control rounded-3" required minlength="6">
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

<div class="modal fade" id="changeAvatarModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-4">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-primary"><i class="fas fa-image me-2"></i> Thay Ảnh đại diện</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form action="${pageContext.request.contextPath}/UpdateAvatarController" method="POST" enctype="multipart/form-data">
                    <div class="mb-4">
                        <label class="form-label text-muted fw-semibold">Chọn ảnh từ thiết bị (Max: 5MB)</label>
                        <input type="file" name="avatarFile" id="inputFile" class="form-control rounded-3" accept="image/png, image/jpeg, image/jpg" required>
                    </div>
                    
                    <div class="text-center mb-3">
                        <p class="text-muted small mb-2">Xem trước:</p>
                        <img id="previewUpload" src="${not empty sessionScope.user.avatarUrl ? sessionScope.user.avatarUrl : defaultAvatar}" 
                             style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 2px solid #0d6efd;">
                    </div>

                    <div class="text-end border-top pt-3 mt-4">
                        <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary rounded-pill px-5">Lưu ảnh</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // JS: Tự động đổi ảnh xem trước
    document.getElementById('inputFile').addEventListener('change', function(event) {
        const [file] = event.target.files;
        if (file) {
            document.getElementById('previewUpload').src = URL.createObjectURL(file);
        }
    });
</script>