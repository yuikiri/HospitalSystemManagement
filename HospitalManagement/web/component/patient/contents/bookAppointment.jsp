<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    .form-select {
        border-radius: 12px; padding: 12px 15px; border: 1px solid #e2e8f0;
        background-color: #f8fafc; transition: all 0.3s; cursor: pointer;
    }
    .form-select:focus { border-color: #4361ee; box-shadow: 0 0 0 0.25rem rgba(67, 97, 238, 0.15); background-color: #fff; }
    .step-badge {
        background: #e0eafc; color: #4361ee; width: 35px; height: 35px;
        display: inline-flex; align-items: center; justify-content: center;
        border-radius: 50%; font-weight: bold; margin-right: 15px;
    }
</style>

<div class="row justify-content-center pb-5">
    <div class="col-lg-9">
        <div class="p-5 mt-3" style="border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff;">
            <div class="text-center mb-5">
                <h3 class="fw-bold text-primary">Đăng Ký Lịch Khám</h3>
                <p class="text-muted">Lựa chọn Khoa và Phòng khám mong muốn. Bạn có thể để hệ thống tự xếp phòng (0đ) nếu không rõ.</p>
            </div>

            <form action="${pageContext.request.contextPath}/MainController" method="POST">
                
                <input type="hidden" name="action" value="SubmitBooking">
                
                <div class="mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center"><span class="step-badge">1</span> Thời gian khám</h5>
                    <div class="row g-3 ms-4">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-muted">Chọn ngày khám *</label>
                            <input type="date" class="form-control form-select" name="bookingDate" id="bookingDate" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-muted">Chọn khung giờ *</label>
                            <select class="form-select" name="bookingTime" required>
                                <option value="" selected disabled>-- Vui lòng chọn khung giờ --</option>
                                <option value="06:00:00">06:00 - 08:00 (Sáng sớm)</option>
                                <option value="08:00:00">08:00 - 10:00 (Sáng)</option>
                                <option value="10:00:00">10:00 - 12:00 (Trưa)</option>
                                <option value="13:00:00">13:00 - 15:00 (Đầu giờ chiều)</option>
                                <option value="15:00:00">15:00 - 17:00 (Cuối chiều)</option>
                                <option value="18:00:00">18:00 - 20:00 (Ca tối)</option>
                                <option value="20:00:00">20:00 - 22:00 (Ca đêm)</option>
                            </select>
                        </div>
                    </div>
                </div>

                <hr class="my-4 border-light">

                <div class="mb-4">
                    <h5 class="fw-bold mb-3 d-flex align-items-center"><span class="step-badge">2</span> Chuyên khoa & Dịch vụ</h5>
                    <div class="row g-3 ms-4">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-primary">Chọn Khoa / Phòng Ban *</label>
                            <select class="form-select border-primary text-primary fw-bold" name="departmentId" id="departmentSelect" required onchange="filterRooms(this.value)">
                                <option value="" selected disabled>-- Chọn Khoa bạn muốn khám --</option>
                                <c:forEach items="${departmentList}" var="dept">
                                    <option value="${dept.id}">${dept.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-success">Phòng / Gói dịch vụ (Tùy chọn)</label>
                            <select class="form-select border-success text-success fw-bold" name="roomId" id="roomSelect" disabled>
                                <option value="" selected>-- Vui lòng chọn Khoa trước --</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="text-center mt-5">
                    <button type="submit" class="btn btn-primary btn-lg rounded-pill px-5 shadow-sm">
                        <i class="fas fa-paper-plane me-2"></i> Xác nhận Đặt Lịch
                    </button>
                </div>
            </form>

            <div id="hiddenRoomStorage" style="display: none;">
                <c:forEach items="${roomList}" var="r">
                    <div class="room-data" data-id="${r.id}" data-dept="${r.departmentId}" data-name="Phòng ${r.roomNumber} - ${r.roomTypeName}" data-price="${r.price}"></div>
                </c:forEach>
            </div>

        </div>
    </div>
</div>

<script>
    // 1. Chặn chọn ngày trong quá khứ
    setTimeout(function() {
        let dateInput = document.getElementById('bookingDate');
        if(dateInput) {
            dateInput.setAttribute('min', new Date().toISOString().split('T')[0]);
        }
    }, 200);

    // 2. Logic Lọc Phòng xổ xuống
    window.filterRooms = function(selectedDeptId) {
        let roomSelect = document.getElementById('roomSelect');
        let roomDataElements = document.querySelectorAll('#hiddenRoomStorage .room-data');
        
        let htmlContent = '<option value="">-- Không chọn (Hệ thống tự xếp phòng 0đ) --</option>';
        let foundRooms = 0;

        roomDataElements.forEach(function(el) {
            if(el.getAttribute('data-dept') === selectedDeptId) {
                let id = el.getAttribute('data-id');
                let name = el.getAttribute('data-name');
                let priceVal = parseFloat(el.getAttribute('data-price'));
                let priceText = priceVal === 0 ? "Miễn phí" : priceVal.toLocaleString('vi-VN') + " VNĐ";
                
                htmlContent += '<option value="' + id + '">' + name + ' (' + priceText + ')</option>';
                foundRooms++;
            }
        });

        if(foundRooms > 0) {
            roomSelect.innerHTML = htmlContent;
            roomSelect.disabled = false;
        } else {
            roomSelect.innerHTML = '<option value="">-- Khoa này chưa có phòng (Tự động xếp) --</option>';
            roomSelect.disabled = false;
        }
    };
</script>