<%-- 
    Document   : bookAppointment
    Created on : Mar 14, 2026, 2:35:33 AM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .booking-card { border-radius: 20px; border: none; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff; }
    .form-control, .form-select { border-radius: 12px; padding: 12px 15px; border: 1px solid #e2e8f0; background-color: #f8fafc; transition: all 0.3s; }
    .form-control:focus, .form-select:focus { border-color: var(--primary-color); box-shadow: 0 0 0 0.25rem rgba(67, 97, 238, 0.15); background-color: #fff; }
    .step-badge { background: var(--primary-light); color: var(--primary-color); width: 35px; height: 35px; display: inline-flex; align-items: center; justify-content: center; border-radius: 50%; font-weight: bold; margin-right: 15px; }
</style>

<div class="row fade-in justify-content-center">
    <div class="col-lg-9">
        <div class="booking-card p-5">
            <div class="text-center mb-5">
                <h3 class="fw-bold text-primary">Đăng Ký Lịch Khám</h3>
                <p class="text-muted">Vui lòng điền thông tin bên dưới để hệ thống sắp xếp lịch khám tốt nhất cho bạn.</p>
            </div>

            <form action="#" method="POST" id="bookingForm">
                
                <div class="mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center">
                        <span class="step-badge">1</span> Thời gian khám
                    </h5>
                    <div class="row g-3 ms-4">
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Chọn ngày khám</label>
                            <input type="date" class="form-control" name="bookingDate" id="bookingDate" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Chọn khung giờ</label>
                            <select class="form-select" name="bookingTime" id="bookingTime" required disabled>
                                <option value="" selected disabled>-- Vui lòng chọn ngày trước --</option>
                                <option value="06:00">06:00 - 08:00 (Sáng sớm)</option>
                                <option value="08:00">08:00 - 10:00 (Sáng)</option>
                                <option value="10:00">10:00 - 12:00 (Trưa)</option>
                                <option value="13:00">13:00 - 15:00 (Đầu giờ chiều)</option>
                                <option value="15:00">15:00 - 17:00 (Cuối chiều)</option>
                                <option value="18:00">18:00 - 20:00 (Ca tối)</option>
                                <option value="20:00">20:00 - 22:00 (Ca đêm)</option>
                            </select>
                        </div>
                    </div>
                </div>

                <hr class="my-4 border-light">

                <div class="mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center">
                        <span class="step-badge">2</span> Chuyên khoa & Bác sĩ
                    </h5>
                    <div class="row g-3 ms-4">
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Chọn chuyên khoa</label>
                            <select class="form-select" name="departmentId" id="departmentSelect" required>
                                <option value="" selected disabled>-- Chọn khoa cần khám --</option>
                                <option value="1">Khoa Tim Mạch</option>
                                <option value="2">Khoa Nhi</option>
                                <option value="3">Khoa Nội Tổng Hợp</option>
                                <option value="4">Khoa Răng Hàm Mặt</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label text-muted fw-semibold">Chọn Bác sĩ</label>
                            <select class="form-select" name="doctorId" id="doctorSelect" disabled>
                                <option value="0" selected>Khám Bác sĩ bất kỳ (Hệ thống tự xếp)</option>
                                <option value="1">Bác sĩ Nguyễn Bá Tĩnh</option>
                                <option value="2">Bác sĩ Lê Thị Hương</option>
                            </select>
                        </div>
                    </div>
                </div>

                <hr class="my-4 border-light">

                <div class="mb-4 ms-5">
                    <label class="form-label text-muted fw-semibold">Triệu chứng hiện tại (Không bắt buộc)</label>
                    <textarea class="form-control" name="notes" rows="3" placeholder="Ví dụ: Đau đầu, sốt nhẹ về đêm..."></textarea>
                </div>

                <div class="text-center mt-5">
                    <button type="submit" class="btn btn-primary btn-lg rounded-pill px-5 shadow-sm">
                        <i class="fas fa-paper-plane me-2"></i> Gửi Yêu Cầu Đặt Lịch
                    </button>
                    <p class="text-muted mt-3 small"><i class="fas fa-info-circle"></i> Đơn của bạn sẽ được chuyển đến trạng thái chờ xác nhận.</p>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // 1. Chỉ mở khóa chọn Giờ khi đã chọn Ngày
    document.getElementById('bookingDate').addEventListener('change', function() {
        let timeSelect = document.getElementById('bookingTime');
        if(this.value) {
            timeSelect.disabled = false;
            timeSelect.options[0].text = "-- Chọn khung giờ --";
        } else {
            timeSelect.disabled = true;
        }
    });

    // 2. Chỉ mở khóa chọn Bác sĩ khi đã chọn Khoa (Mô phỏng)
    document.getElementById('departmentSelect').addEventListener('change', function() {
        let doctorSelect = document.getElementById('doctorSelect');
        if(this.value) {
            doctorSelect.disabled = false;
        } else {
            doctorSelect.disabled = true;
        }
    });

    // 3. Ràng buộc không cho chọn ngày trong quá khứ
    let today = new Date().toISOString().split('T')[0];
    document.getElementById('bookingDate').setAttribute('min', today);
</script>
