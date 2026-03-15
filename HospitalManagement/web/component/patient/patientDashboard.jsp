<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Portal | Medi-Care</title>
    
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        :root {
            --primary-color: #4361ee; 
            --primary-light: #e0eafc;
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

        /* --- BỐ CỤC KHUNG --- */
        #sidebar-wrapper {
            position: fixed;
            top: 0;
            left: 0;
            width: var(--sidebar-width);
            height: 100vh;
            background: #ffffff;
            box-shadow: 4px 0 24px rgba(0, 0, 0, 0.02);
            z-index: 1000;
        }

        #header-wrapper {
            position: fixed;
            top: 0;
            left: var(--sidebar-width);
            width: calc(100% - var(--sidebar-width));
            height: var(--header-height);
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(10px); 
            z-index: 999;
            box-shadow: 0 4px 24px rgba(0, 0, 0, 0.02);
        }

        #main-content-wrapper {
            margin-left: var(--sidebar-width);
            margin-top: var(--header-height);
            padding: 30px;
            min-height: calc(100vh - var(--header-height));
        }


        .fade-in {
            animation: fadeIn 0.4s ease-in-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>

    <div id="sidebar-wrapper">
        <jsp:include page="sidebar.jsp" />
    </div>

    <div id="header-wrapper">
        <jsp:include page="header.jsp" />
    </div>

    <div style="position: absolute; top: 20px; right: 20px; z-index: 9999;">
            <% 
                // DÙNG JAVA GỐC ĐỂ KIỂM TRA -> CHỐNG LỖI THƯ VIỆN TUYỆT ĐỐI
                String successMsg = (String) session.getAttribute("successMessage");
                String errorMsg = (String) session.getAttribute("errorMessage");
            %>

            <% if (successMsg != null && !successMsg.trim().isEmpty()) { %>
                <div id="alertSuccess" class="alert alert-success alert-dismissible fade show shadow-lg" role="alert" 
                     style="border-radius: 10px; font-weight: bold; border-left: 5px solid #198754; background-color: #d1e7dd; padding: 15px 20px;">
                    <i class="fas fa-check-circle me-2" style="font-size: 1.2rem;"></i> 
                    <%= successMsg %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("successMessage"); // Xóa sau khi hiện %>
            <% } %>

            <% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
                <div id="alertError" class="alert alert-danger alert-dismissible fade show shadow-lg" role="alert" 
                     style="border-radius: 10px; font-weight: bold; border-left: 5px solid #dc3545; background-color: #f8d7da; padding: 15px 20px;">
                    <i class="fas fa-exclamation-triangle me-2" style="font-size: 1.2rem;"></i> 
                    <%= errorMsg %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("errorMessage"); // Xóa sau khi hiện %>
            <% } %>
        </div>
        
        <script>
            // Tự động ẩn thông báo sau 4 giây
            setTimeout(function() {
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
                <span class="ms-3 text-muted fw-medium">Đang tải dữ liệu hệ thống...</span>
            </div>
        </div>
    </div>
    

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function loadContent(pageUrl, element) {
            const contentDiv = document.getElementById('dynamic-content');

            // 1. Cập nhật màu sắc cho Menu bên trái
            document.querySelectorAll('.nav-item-custom').forEach(el => el.classList.remove('active'));
            if(element) element.classList.add('active');

            // 2. Lưu Tab vào bộ nhớ
            if(element && element.id) {
                sessionStorage.setItem('savedTabUrl', pageUrl);
                sessionStorage.setItem('savedTabId', element.id);
            }

            // 3. HIỆN SPINNER
            contentDiv.innerHTML = `
                <div class="d-flex justify-content-center align-items-center fade-in" style="height: 50vh;">
                    <div class="spinner-border text-primary" role="status" style="width: 2.5rem; height: 2.5rem;"></div>
                    <span class="ms-3 text-muted fw-medium fs-5">Đang tải dữ liệu...</span>
                </div>
            `;

            // 4. Gọi Fetch API để lấy HTML từ file con
            fetch(pageUrl)
                .then(response => {
                    if(!response.ok) throw new Error("Network response was not ok");
                    return response.text();
                })
                .then(html => {
                    // Tải xong -> Xóa Spinner và đắp nội dung thật vào
                    contentDiv.classList.remove('fade-in');
                    void contentDiv.offsetWidth; 
                    contentDiv.innerHTML = html;
                    contentDiv.classList.add('fade-in');

                    // 🌟 BÍ KÍP CHỐNG ĐƠ SCRIPT TẠI ĐÂY:
                    // Ép trình duyệt tìm và chạy tất cả các đoạn <script> vừa được nhét vào màn hình
                    executeScripts(contentDiv);
                })
                .catch(error => {
                    contentDiv.innerHTML = `
                        <div class="alert alert-danger shadow-sm rounded-4 border-0 p-4 mt-4 fade-in">
                            <h5 class="text-danger fw-bold"><i class="fas fa-exclamation-triangle"></i> Lỗi tải trang!</h5>
                            <p class="mb-0">Không thể tải nội dung từ <b>${pageUrl}</b>.</p>
                        </div>`;
                });
        }

        // Hàm phụ trợ: Đánh thức các đoạn code JS ngủ quên trong innerHTML
        function executeScripts(element) {
            const scripts = element.querySelectorAll('script');
            scripts.forEach(oldScript => {
                const newScript = document.createElement('script');
                // Chép ruột code sang thẻ script mới để nó chạy
                newScript.textContent = oldScript.textContent;
                document.body.appendChild(newScript);
                oldScript.remove(); // Dọn dẹp xác thẻ cũ
            });
        }

        // TỰ ĐỘNG CHẠY KHI F5
        window.addEventListener('DOMContentLoaded', () => {
            let savedUrl = sessionStorage.getItem('savedTabUrl');
            let savedId = sessionStorage.getItem('savedTabId');

            if(savedUrl && savedId && document.getElementById(savedId)) {
                loadContent(savedUrl, document.getElementById(savedId));
            } else {
                loadContent('contents/patientProfile.jsp', document.getElementById('menu-info'));
            }
        });
    </script>
</body>
</html>