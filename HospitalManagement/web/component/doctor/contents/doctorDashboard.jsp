<%@page import="dao.DoctorDTO"%>
<%@page import="entity.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. CHỐNG LƯU CACHE TRÌNH DUYỆT
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // 2. FIX LỖI TÊN SESSION: Đổi "user" thành "LOGIN_USER" cho khớp Controller
    User user = (User) session.getAttribute("LOGIN_USER"); 
    if (user == null || !user.getRole().equalsIgnoreCase("doctor")) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    DoctorDTO doctor = (DoctorDTO) session.getAttribute("doctor"); 
    if (doctor == null) {
        doctor = new DoctorDTO();
    }
    
    String currentPage = request.getParameter("page");
    if (currentPage == null || currentPage.trim().isEmpty()) {
        currentPage = "profile"; 
    }
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("doctor", doctor);
    request.setAttribute("user", user);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Doctor Portal | Medi-Care</title>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

        <style>
            :root {
                --primary-color: #0d6efd; 
                --primary-light: #e7f1ff;
                --bg-color: #f4f7fe;
                --text-main: #2b3674;
                --text-muted: #a3aed1;
                --sidebar-width: 260px;
                --header-height: 80px;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-color);
                color: var(--text-main);
                overflow-x: hidden;
            }

            #sidebar-wrapper {
                position: fixed; top: 0; left: 0;
                width: var(--sidebar-width); height: 100vh;
                background: #ffffff;
                box-shadow: 4px 0 24px rgba(0, 0, 0, 0.02);
                z-index: 1000;
            }

            #header-wrapper {
                position: fixed; top: 0; left: var(--sidebar-width);
                width: calc(100% - var(--sidebar-width)); height: var(--header-height);
                background: rgba(255, 255, 255, 0.85);
                backdrop-filter: blur(10px); z-index: 999;
                box-shadow: 0 4px 24px rgba(0, 0, 0, 0.02);
            }

            #main-content-wrapper {
                margin-left: var(--sidebar-width);
                margin-top: var(--header-height);
                padding: 30px;
                min-height: calc(100vh - var(--header-height));
            }

            .fade-in { animation: fadeIn 0.4s ease-in-out; }
            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(10px); }
                to { opacity: 1; transform: translateY(0); }
            }
        </style>
    </head>
    <body>

        <div id="sidebar-wrapper">
            <%-- FIX ĐƯỜNG DẪN: Lùi ra ngoài 1 cấp để vào includes --%>
            <jsp:include page="../includes/sidebar.jsp" />
        </div>

        <div id="header-wrapper">
            <%-- FIX ĐƯỜNG DẪN: Lùi ra ngoài 1 cấp để vào includes --%>
            <jsp:include page="../includes/header.jsp" />
        </div>

        <div style="position: absolute; top: 20px; right: 20px; z-index: 9999;">
            <%
                String successMsg = (String) session.getAttribute("successMessage");
                String errorMsg = (String) session.getAttribute("errorMessage");
            %>
            <% if (successMsg != null && !successMsg.trim().isEmpty()) {%>
            <div id="alertSuccess" class="alert alert-success alert-dismissible fade show shadow-lg" role="alert" 
                 style="border-radius: 10px; font-weight: bold; border-left: 5px solid #198754; background-color: #d1e7dd; padding: 15px 20px;">
                <i class="fas fa-check-circle me-2" style="font-size: 1.2rem;"></i> 
                <%= successMsg%>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("successMessage"); } %>

            <% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
            <div id="alertError" class="alert alert-danger alert-dismissible fade show shadow-lg" role="alert" 
                 style="border-radius: 10px; font-weight: bold; border-left: 5px solid #dc3545; background-color: #f8d7da; padding: 15px 20px;">
                <i class="fas fa-exclamation-triangle me-2" style="font-size: 1.2rem;"></i> 
                <%= errorMsg%>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("errorMessage"); } %>
        </div>

        <script>
            setTimeout(function () {
                let alertS = document.getElementById('alertSuccess');
                let alertE = document.getElementById('alertError');
                if (alertS) { alertS.style.transition = 'opacity 0.5s'; alertS.style.opacity = '0'; setTimeout(() => alertS.remove(), 500); }
                if (alertE) { alertE.style.transition = 'opacity 0.5s'; alertE.style.opacity = '0'; setTimeout(() => alertE.remove(), 500); }
            }, 4000);
        </script>

        <div id="main-content-wrapper">
            <div id="dynamic-content">
                <div class="d-flex justify-content-center align-items-center" style="height: 50vh;">
                    <div class="spinner-border text-primary" role="status"></div>
                    <span class="ms-3 text-muted fw-medium">Đang tải dữ liệu bác sĩ...</span>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            function loadContent(pageUrl, element) {
                const contentDiv = document.getElementById('dynamic-content');

                document.querySelectorAll('.nav-item-custom').forEach(el => el.classList.remove('active'));
                if (element) element.classList.add('active');

                if (element && element.id) {
                    sessionStorage.setItem('savedDoctorTabUrl', pageUrl);
                    sessionStorage.setItem('savedDoctorTabId', element.id);
                }

                contentDiv.innerHTML = `
                    <div class="d-flex justify-content-center align-items-center fade-in" style="height: 50vh;">
                        <div class="spinner-border text-primary" role="status" style="width: 2.5rem; height: 2.5rem;"></div>
                        <span class="ms-3 text-muted fw-medium fs-5">Đang xử lý dữ liệu...</span>
                    </div>`;

                fetch(pageUrl)
                    .then(response => {
                        if (!response.ok) throw new Error("Network response was not ok");
                        return response.text();
                    })
                    .then(html => {
                        contentDiv.classList.remove('fade-in');
                        void contentDiv.offsetWidth;
                        contentDiv.innerHTML = html;
                        contentDiv.classList.add('fade-in');
                        executeScripts(contentDiv); 
                    })
                    .catch(error => {
                        contentDiv.innerHTML = `
                        <div class="alert alert-danger shadow-sm rounded-4 border-0 p-4 mt-4 fade-in">
                            <h5 class="text-danger fw-bold"><i class="fas fa-exclamation-triangle"></i> Lỗi tải nội dung!</h5>
                            <p class="mb-0">Đường dẫn <b>${pageUrl}</b> không tồn tại hoặc bị lỗi.</p>
                        </div>`;
                    });
            }

            function executeScripts(element) {
                const scripts = element.querySelectorAll('script');
                scripts.forEach(oldScript => {
                    const newScript = document.createElement('script');
                    newScript.textContent = oldScript.textContent;
                    document.body.appendChild(newScript);
                    oldScript.remove(); 
                });
            }

            window.addEventListener('DOMContentLoaded', () => {
                let savedUrl = sessionStorage.getItem('savedDoctorTabUrl');
                let savedId = sessionStorage.getItem('savedDoctorTabId');

                if (savedUrl && savedId && document.getElementById(savedId)) {
                    loadContent(savedUrl, document.getElementById(savedId));
                } else {
                    // Load trang Profile mặc định của bác sĩ
                    let defaultUrl = 'profile.jsp'; 
                    loadContent(defaultUrl, document.getElementById('menu-doc-profile'));
                }
            });
        </script>
    </body>
</html>