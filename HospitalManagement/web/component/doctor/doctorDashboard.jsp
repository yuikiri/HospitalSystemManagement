<%@page import="dao.DoctorDTO"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Kiểm tra quyền truy cập
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equalsIgnoreCase("doctor")) {
        response.sendRedirect("../../index.jsp");
        return;
    }

    DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor"); // Sửa thành doctor cho giống backend nếu cần
    if (doctor == null) {
        doctor = new DoctorDTO();
    }
    
    // Lấy tham số page để biết đang ở chức năng nào
    String currentPage = request.getParameter("page");
    if (currentPage == null || currentPage.isEmpty()) {
        currentPage = "profile"; // Mặc định là trang cá nhân
    }
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("doctor", doctor);
    request.setAttribute("user", user);
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
            .sidebar { min-height: 100vh; background: white; border-right: 1px solid #dee2e6; position: fixed; width: 250px; z-index: 1000; }
            .logo-area { padding: 25px 20px; display: flex; align-items: center; color: var(--medicare-blue); font-weight: bold; font-size: 24px; border-bottom: 1px solid #f1f1f1; }
            .nav-link { color: var(--text-muted); padding: 15px 20px; font-weight: 500; display: flex; align-items: center; border-radius: 0 50px 50px 0; margin: 4px 10px 4px 0; transition: 0.3s; }
            .nav-link:hover, .nav-link.active { background: var(--medicare-light); color: var(--medicare-blue); }
            .main-content { margin-left: 250px; padding: 30px; }
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
                <jsp:include page="includes/sidebar.jsp" />

                <main class="main-content">
                    <jsp:include page="includes/header.jsp" />

                    <div id="dynamic-content">
                        <jsp:include page='<%="contents/" + currentPage + ".jsp" %>' />
                    </div>
                </main>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function showUpdateModal() {
                var myModal = new bootstrap.Modal(document.getElementById('updateProfileModal'));
                myModal.show();
            }
            function startExam() {
                alert("Đang chuẩn bị phòng khám...");
            }
        </script>
    </body>
</html>