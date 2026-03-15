<%@page import="dao.DoctorDTO"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Lấy thông tin từ Session [cite: 43, 44]
    User user = (User) session.getAttribute("user");
    DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctorInfo");
    
    // Kiểm tra quyền truy cập
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
        
        <style>
            :root {
                --medicare-blue: #0d6efd;
                --medicare-light: #e7f1ff;
                --bg-body: #f0f2f9;
                --text-muted: #6c757d;
            }

            body { background-color: var(--bg-body); font-family: 'Segoe UI', sans-serif; }

            /* Sidebar [cite: 5, 34] */
            .sidebar { min-height: 100vh; background: white; border-right: 1px solid #dee2e6; position: fixed; width: 250px; z-index: 1000; }
            .logo-area { padding: 25px 20px; display: flex; align-items: center; color: var(--medicare-blue); font-weight: bold; font-size: 24px; border-bottom: 1px solid #f1f1f1; }
            .nav-link { color: var(--text-muted); padding: 15px 20px; font-weight: 500; display: flex; align-items: center; border-radius: 0 50px 50px 0; margin: 4px 10px 4px 0; transition: 0.3s; }
            .nav-link:hover, .nav-link.active { background: var(--medicare-light); color: var(--medicare-blue); }

            /* Main Content [cite: 16] */
            .main-content { margin-left: 250px; padding: 30px; }

            /* Profile Card [cite: 18, 46] */
            .profile-card { background: white; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); border: none; }
            .avatar-circle { width: 120px; height: 120px; background: var(--medicare-blue); color: white; font-size: 40px; font-weight: bold; display: flex; align-items: center; justify-content: center; border-radius: 50%; margin: 0 auto 20px; position: relative; }
            .camera-badge { position: absolute; bottom: 5px; right: 5px; background: white; width: 35px; height: 35px; border-radius: 50%; display: flex; align-items: center; justify-content: center; border: 1px solid #ddd; }
            
            .info-label { color: #adb5bd; font-size: 0.75rem; text-transform: uppercase; letter-spacing: 1px; }
            .info-value { font-weight: 600; color: #333; margin-bottom: 15px; }

            .content-section { animation: fadeIn 0.4s ease; }
            @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
        </style>
    </head>
    <body>

        <div class="container-fluid p-0">
            <div class="row g-0">
                <nav class="sidebar d-flex flex-column">
                    <div class="logo-area">
                        <i class="bi bi-heart-pulse-fill"></i> MediCare
                    </div>
                    
                    <div class="nav flex-column mt-4 px-2">
                        <a href="#" class="nav-link active" onclick="showSection('profile', this)">
                            <i class="bi bi-person-vcard me-2"></i> Thông tin cá nhân
                        </a>
                        <a href="#" class="nav-link" onclick="showSection('shift', this)">
                            <i class="bi bi-calendar-event me-2"></i> Ca trực
                        </a>
                        <a href="#" class="nav-link" onclick="showSection('history', this)">
                            <i class="bi bi-clock-history me-2"></i> Lịch sử khám bệnh
                        </a>
                        
                        <div style="margin-top: 100px;">
                            <hr class="mx-3 text-muted">
                            <a href="LogoutController" class="nav-link text-danger">
                                <i class="bi bi-box-arrow-left me-2"></i> Đăng xuất
                            </a>
                        </div>
                    </div>
                </nav>

                <main class="main-content">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h4 class="fw-bold mb-0">Chào mừng, BS. <%= (doctor != null) ? doctor.getName() : user.getUserName() %>! 👋</h4>
                            <p class="text-muted small">Chúc bạn một ngày làm việc hiệu quả tại MediCare.</p>
                        </div>
                        <div class="bg-white p-2 rounded-pill shadow-sm px-3 d-flex align-items-center">
                            <i class="bi bi-bell text-muted me-3"></i>
                            <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 35px; height: 35px;">
                                <%= (doctor != null) ? doctor.getName().substring(0,1).toUpperCase() : "D" %>
                            </div>
                            <span class="fw-bold small">Bác sĩ</span>
                        </div>
                    </div>

                    <div id="dynamic-content">
                        <div id="section-profile" class="content-section">
                            <div class="row g-4">
                                <div class="col-lg-4">
                                    <div class="profile-card p-4 text-center">
                                        <div class="avatar-circle">
                                            <%= (doctor != null) ? doctor.getName().substring(0,1).toUpperCase() : "D" %>
                                            <div class="camera-badge" style="cursor: pointer;" onclick="document.getElementById('avatarFile').click()">
                                                <i class="bi bi-camera-fill"></i>
                                            </div>
                                        </div>
                                        <form id="avatarForm" action="UpdateAvatarController" method="POST" enctype="multipart/form-data" class="d-none">
                                            <input type="file" id="avatarFile" name="avatarFile" onchange="document.getElementById('avatarForm').submit()">
                                        </form>

                                        <h4 class="fw-bold mb-1"><%= (doctor != null) ? doctor.getName() : "N/A" %></h4>
                                        <p class="text-muted small">Mã: #BS-<%= user.getId() %></p>
                                        
                                        <div class="mt-4 px-2">
                                            <button class="btn btn-primary w-100 rounded-pill mb-3 py-2 text-start px-4 d-flex align-items-center" onclick="showUpdateModal()">
                                                <i class="bi bi-person-check-fill me-3 fs-5"></i> Cập nhật hồ sơ
                                            </button>
                                            <button class="btn btn-outline-info w-100 rounded-pill mb-3 py-2 text-start px-4 d-flex align-items-center" onclick="location.href='requestChangeEmail.jsp'">
                                                <i class="bi bi-envelope-at-fill me-3 fs-5"></i> Thay đổi Email
                                            </button>
                                            <button class="btn btn-outline-danger w-100 rounded-pill py-2 text-start px-4 d-flex align-items-center" onclick="location.href='requestChangePassword.jsp'">
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
                                                <div class="info-value"><%= (doctor != null) ? doctor.getName() : "N/A" %></div>
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
                                                <div class="info-value text-primary"><%= (doctor != null) ? doctor.getPhone() : "N/A" %></div>
                                            </div>
                                            <div class="col-md-6 border-bottom pb-2">
                                                <div class="info-label">Vị trí</div>
                                                <div class="info-value"><%= (doctor != null) ? doctor.getPosition() : "N/A" %></div>
                                            </div>
                                            <div class="col-12 border-bottom pb-2">
                                                <div class="info-label">Số giấy phép (License)</div>
                                                <div class="info-value text-success"><%= (doctor != null) ? doctor.getLicenseNumber() : "N/A" %></div>
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

                        <div id="section-shift" class="content-section d-none">
                            <div class="profile-card p-4">
                                <h5 class="text-primary fw-bold mb-4">Lịch trực của tôi</h5>
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Thời gian</th>
                                                <th>Phòng</th>
                                                <th>Khoa</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>08:00 - 12:00</td>
                                                <td>Phòng 102</td>
                                                <td>Nội tổng quát</td>
                                                <td><button class="btn btn-sm btn-primary px-3 rounded-pill" onclick="startExam()">Vào khám</button></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div id="section-history" class="content-section d-none">
                            <div class="profile-card p-4">
                                <h5 class="text-primary fw-bold mb-4">Lịch sử khám bệnh</h5>
                                <table class="table table-striped align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Bệnh nhân</th>
                                            <th>Chuẩn đoán</th>
                                            <th>Ngày tạo</th>
                                            <th>Tổng phí</th>
                                            <th>Trạng thái</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Lê Văn Tám</td>
                                            <td>Viêm họng cấp</td>
                                            <td>14/03/2026</td>
                                            <td class="fw-bold">450,000 đ</td>
                                            <td><span class="badge bg-success">Đã hoàn thành</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <div class="modal fade" id="updateProfileModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow-lg border-0" style="border-radius: 20px;">
                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title fw-bold text-primary">Chỉnh sửa hồ sơ</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="updateProfile" method="POST">
                        <div class="modal-body p-4">
                            <input type="hidden" name="doctorId" value="<%= (doctor != null) ? doctor.getId() : "" %>">
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted text-uppercase">Họ và Tên</label>
                                <input type="text" class="form-control rounded-3" name="name" value="<%= (doctor != null) ? doctor.getName() : "" %>" required>
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
                                    <input type="text" class="form-control rounded-3" name="phone" value="<%= (doctor != null) ? doctor.getPhone() : "" %>" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted text-uppercase">Chức vụ / Vị trí</label>
                                <input type="text" class="form-control rounded-3" name="position" value="<%= (doctor != null) ? doctor.getPosition() : "" %>" required>
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

        <script>
            function showSection(sectionId, element) {
                document.querySelectorAll('.content-section').forEach(s => s.classList.add('d-none'));
                document.getElementById('section-' + sectionId).classList.remove('d-none');
                document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
                element.classList.add('active');
            }

            function showUpdateModal() {
                var myModal = new bootstrap.Modal(document.getElementById('updateProfileModal'));
                myModal.show();
            }

            function startExam() {
                // Logic để mở form khám bệnh và kê đơn
                alert("Đang chuẩn bị phòng khám...");
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>