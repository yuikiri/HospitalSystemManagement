<%@page import="dao.DoctorDTO"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    // ==========================================
    // KIỂM TRA QUYỀN TRUY CẬP (ACCESS CONTROL)
    // ==========================================
    User user = (User) session.getAttribute("user");
    DoctorDTO doctorInfo = (DoctorDTO) session.getAttribute("doctorInfo");
    
    if (user == null || !user.getRole().equalsIgnoreCase("doctor")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Doctor Dashboard - MediCare</title>
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        
        <style>
            :root { --medicare-blue: #0d6efd; --medicare-light: #e7f1ff; --bg-body: #f0f2f9; }
            body { background-color: var(--bg-body); font-family: 'Segoe UI', sans-serif; }

            /* CSS CHO SIDEBAR (THANH ĐIỀU HƯỚNG BÊN TRÁI) */
            .sidebar { min-height: 100vh; background: white; border-right: 1px solid #dee2e6; position: fixed; width: 250px; z-index: 1000; }
            .logo-area { padding: 25px 20px; display: flex; align-items: center; color: var(--medicare-blue); font-weight: bold; font-size: 24px; border-bottom: 1px solid #f1f1f1; }
            .nav-link { color: #6c757d; padding: 15px 20px; font-weight: 500; border-radius: 0 50px 50px 0; margin-right: 10px; transition: 0.3s; }
            .nav-link:hover, .nav-link.active { background: var(--medicare-light); color: var(--medicare-blue); }

            /* CSS CHO PHẦN PROFILE (THEO STYLE BẢN PATIENT BẠN GỬI) */
            .profile-card { border-radius: 20px; border: none; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff; }
            .info-label { color: #6c757d; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; font-weight: 600; margin-bottom: 5px; }
            .info-value { color: #333; font-size: 1.1rem; font-weight: 500; margin-bottom: 20px; }
            .avatar-wrapper { position: relative; width: 140px; height: 140px; margin: 0 auto; }
            .avatar-img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; box-shadow: 0 8px 20px rgba(13, 110, 253, 0.2); border: 4px solid #fff; }
            .change-avt-btn { position: absolute; bottom: 5px; right: 5px; background: var(--medicare-blue); color: white; border-radius: 50%; width: 35px; height: 35px; display: flex; align-items: center; justify-content: center; border: 2px solid white; cursor: pointer; }
            
            .main-content { margin-left: 250px; padding: 30px; }
            .fade-in { animation: fadeIn 0.5s ease-in; }
            @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        </style>
    </head>
    <body>

        <nav class="sidebar d-flex flex-column">
            <div class="logo-area"><i class="bi bi-heart-pulse-fill me-2"></i> MediCare</div>
            <div class="nav flex-column mt-4 px-2">
                <a href="#" class="nav-link active" onclick="showSection('profile', this)"><i class="fas fa-user-circle me-2"></i> Hồ sơ bác sĩ</a>
                <a href="#" class="nav-link" onclick="showSection('shift', this)"><i class="fas fa-calendar-alt me-2"></i> Ca trực của tôi</a>
                <a href="#" class="nav-link" onclick="showSection('history', this)"><i class="fas fa-history me-2"></i> Lịch sử khám</a>
                <div class="mt-auto mb-4">
                    <hr class="mx-3">
                    <a href="LogoutController" class="nav-link text-danger"><i class="fas fa-sign-out-alt me-2"></i> Đăng xuất</a>
                </div>
            </div>
        </nav>

        <main class="main-content">
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm" role="alert">
                    <i class="fas fa-check-circle me-2"></i> ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <div id="section-profile" class="content-section fade-in">
                <c:set var="defaultAvatar" value="https://ui-avatars.com/api/?name=${user.userName}&background=0d6efd&color=fff&size=150" />
                <div class="row">
                    <div class="col-md-4 mb-4">
                        <div class="profile-card p-4 text-center">
                            <div class="avatar-wrapper mb-3">
                                <img id="currentAvatar" src="${not empty user.avatarUrl ? user.avatarUrl : defaultAvatar}" class="avatar-img">
                                <div class="change-avt-btn" data-bs-toggle="modal" data-bs-target="#changeAvatarModal"><i class="fas fa-camera"></i></div>
                            </div>
                            <h4 class="fw-bold mb-1">${doctorInfo.name}</h4>
                            <p class="text-muted mb-4">Mã BS: #BS-${doctorInfo.id}</p>
                            <div class="d-grid gap-2 px-3">
                                <button class="btn btn-primary rounded-pill" data-bs-toggle="modal" data-bs-target="#editDoctorProfileModal"><i class="fas fa-user-edit me-2"></i> Cập nhật hồ sơ</button>
                                <button class="btn btn-outline-info rounded-pill" data-bs-toggle="modal" data-bs-target="#changeEmailModal"><i class="fas fa-envelope me-2"></i> Đổi Email</button>
                                <button class="btn btn-outline-danger rounded-pill" data-bs-toggle="modal" data-bs-target="#changePasswordModal"><i class="fas fa-key me-2"></i> Đổi Mật khẩu</button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-8 mb-4">
                        <div class="profile-card p-5">
                            <h5 class="fw-bold text-primary border-bottom pb-3 mb-4"><i class="fas fa-user-md me-2"></i> Thông tin cơ bản</h5>
                            <div class="row">
                                <div class="col-sm-6"><p class="info-label">Họ và tên</p><p class="info-value">${doctorInfo.name}</p></div>
                                <div class="col-sm-6"><p class="info-label">Giới tính</p><p class="info-value">${doctorInfo.gender == 1 ? 'Nam' : 'Nữ'}</p></div>
                                <div class="col-sm-6"><p class="info-label">Vị trí / Chức vụ</p><p class="info-value">${doctorInfo.position}</p></div>
                                <div class="col-sm-6"><p class="info-label">Số điện thoại</p><p class="info-value">${doctorInfo.phone}</p></div>
                                <div class="col-12"><p class="info-label">Số giấy phép (License)</p><p class="info-value text-success fw-bold">${doctorInfo.licenseNumber}</p></div>
                                <div class="col-12"><p class="info-label">Email tài khoản</p><p class="info-value text-info">${user.email}</p></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div id="section-shift" class="content-section d-none"><h3>Lịch làm việc đang cập nhật...</h3></div>
            <div id="section-history" class="content-section d-none"><h3>Lịch sử khám bệnh đang cập nhật...</h3></div>
        </main>

        <div class="modal fade" id="editDoctorProfileModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0" style="border-radius: 20px;">
                    <div class="modal-header border-0 mt-3 px-4">
                        <h5 class="modal-title fw-bold text-primary"><i class="fas fa-edit me-2"></i> Cập nhật hồ sơ bác sĩ</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form action="UpdateProfileController1" method="POST">
                            <input type="hidden" name="doctorId" value="${doctorInfo.id}">
                            <div class="row g-3">
                                <div class="col-md-6"><label class="form-label text-muted fw-semibold">Họ và tên</label><input type="text" name="name" class="form-control rounded-3" value="${doctorInfo.name}" required></div>
                                <div class="col-md-6"><label class="form-label text-muted fw-semibold">Số điện thoại</label><input type="text" name="phone" class="form-control rounded-3" value="${doctorInfo.phone}" required></div>
                                <div class="col-md-6"><label class="form-label text-muted fw-semibold">Vị trí</label><input type="text" name="position" class="form-control rounded-3" value="${doctorInfo.position}" required></div>
                                <div class="col-md-6"><label class="form-label text-muted fw-semibold">Giới tính</label>
                                    <select name="gender" class="form-select rounded-3">
                                        <option value="1" ${doctorInfo.gender == 1 ? 'selected' : ''}>Nam</option>
                                        <option value="0" ${doctorInfo.gender == 0 ? 'selected' : ''}>Nữ</option>
                                    </select>
                                </div>
                            </div>
                            <div class="mt-4 text-end">
                                <button type="button" class="btn btn-light rounded-pill px-4 me-2" data-bs-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary rounded-pill px-4">Lưu thông tin</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="changeAvatarModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0" style="border-radius: 20px;">
                    <div class="modal-header border-0 mt-3 px-4"><h5 class="modal-title fw-bold text-primary">Đổi ảnh đại diện</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                    <div class="modal-body p-4 text-center">
                        <form action="UpdateAvatarController" method="POST" enctype="multipart/form-data">
                            <input type="file" name="avatarFile" id="inputFile" class="form-control mb-3" accept="image/*" required onchange="previewImg(this)">
                            <img id="previewUpload" src="${user.avatarUrl}" style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 2px solid #ddd;">
                            <div class="mt-4"><button type="submit" class="btn btn-primary rounded-pill px-4">Cập nhật ảnh</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function showSection(sectionId, element) {
                document.querySelectorAll('.content-section').forEach(s => s.classList.add('d-none'));
                document.getElementById('section-' + sectionId).classList.remove('d-none');
                document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
                element.classList.add('active');
            }
            function previewImg(input) {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function(e) { document.getElementById('previewUpload').src = e.target.result; };
                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>