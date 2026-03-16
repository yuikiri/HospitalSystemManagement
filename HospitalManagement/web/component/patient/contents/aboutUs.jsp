<%-- 
    Document   : aboutUs
    Created on : Mar 14, 2026, 2:35:54 AM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .about-card { border-radius: 20px; border: none; overflow: hidden; background: #fff; box-shadow: 0 10px 30px rgba(0,0,0,0.05); }
    .hero-bg { background: linear-gradient(135deg, rgba(67, 97, 238, 0.9), rgba(58, 12, 163, 0.9)), url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'); background-size: cover; background-position: center; color: white; padding: 60px 40px; }
    .heart-icon { color: #f72585; animation: heartbeat 1.5s infinite; }
    @keyframes heartbeat { 0% { transform: scale(1); } 50% { transform: scale(1.2); } 100% { transform: scale(1); } }
</style>

<div class="fade-in row justify-content-center">
    <div class="col-lg-10">
        <div class="about-card mb-5">
            <div class="hero-bg text-center">
                <i class="fas fa-heart heart-icon fa-3x mb-3"></i>
                <h2 class="fw-bold mb-3">Hành Trình Trao Gửi Yêu Thương</h2>
                <p class="lead mb-0" style="font-weight: 300; font-style: italic;">
                    "Tại Medi-Care, chúng tôi không chỉ chữa bệnh, chúng tôi chữa lành những tâm hồn. 
                    Mỗi sinh mệnh đều quý giá, và không ai phải bỏ cuộc chỉ vì gánh nặng viện phí."
                </p>
            </div>

            <div class="p-5">
                <div class="row align-items-center">
                    <div class="col-md-7">
                        <h4 class="fw-bold text-primary mb-3">Về Bệnh Viện Medi-Care</h4>
                        <p class="text-muted" style="line-height: 1.8;">
                            Được thành lập với sứ mệnh mang lại dịch vụ y tế chuẩn mực và tận tâm nhất, hệ thống <b>Medi-Care</b> tự hào sở hữu 10 chuyên khoa mũi nhọn cùng đội ngũ y bác sĩ hàng đầu. 
                        </p>
                        <p class="text-muted" style="line-height: 1.8;">
                            Nhưng y tế không chỉ là khoa học, đó là <b>Tình người</b>. Hàng ngày, chúng tôi chứng kiến những giọt nước mắt bất lực của các bệnh nhân nghèo mắc bệnh hiểm nghèo. Quỹ <b>"Nhịp Đập Hy Vọng"</b> của Medi-Care ra đời để nối dài sự sống cho họ.
                        </p>
                        <hr class="my-4">
                        <h5 class="fw-bold text-dark mb-3"><i class="fas fa-hands-helping text-warning me-2"></i> Xin hãy đồng hành cùng chúng tôi</h5>
                        <p class="text-muted mb-0">
                            Chỉ một phần nhỏ đóng góp của bạn cũng có thể mua thêm một ống thuốc, một ca phẫu thuật, hay đơn giản là một bữa ăn ấm áp cho bệnh nhân nhi đang giành giật sự sống. <b>Hãy cùng Medi-Care viết tiếp những câu chuyện cổ tích giữa đời thường.</b>
                        </p>
                    </div>
                    
                    <div class="col-md-5 text-center mt-4 mt-md-0">
                        <div class="bg-light p-4 rounded-4 border border-2 border-primary border-opacity-25 shadow-sm">
                            <h5 class="fw-bold text-primary mb-3">Quỹ "Nhịp Đập Hy Vọng"</h5>
                            <img src="https://img.vietqr.io/image/MB-0963518963-print.png?addInfo=TaiTroQuy_NhipDapHyVong" class="img-fluid rounded-3 mb-3 w-75 border" alt="QR Donation">
                            <p class="text-dark fw-bold mb-1">Ngân hàng MB Bank</p>
                            <p class="text-muted small mb-0">STK: <b>0963518963</b></p>
                            <p class="text-muted small">Tên: <b>PHAM QUOC HOANG</b></p>
                            <span class="badge bg-danger rounded-pill px-3 py-2 mt-2"><i class="fas fa-heart me-1"></i> Trân trọng cảm ơn!</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>