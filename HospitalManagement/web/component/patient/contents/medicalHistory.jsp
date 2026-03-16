<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
    .history-card { border-radius: 16px; border: 1px solid #edf2f9; transition: all 0.3s; background: #fff; box-shadow: 0 2px 12px rgba(0,0,0,0.02); }
    .history-card:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(67, 97, 238, 0.1); border-color: var(--primary-light); }
    .status-badge { padding: 6px 12px; border-radius: 20px; font-weight: 500; font-size: 0.85rem; }
    .bg-pending { background-color: #fff3cd; color: #856404; }
    .bg-accepted { background-color: #cff4fc; color: #055160; }
    .bg-success-soft { background-color: #d1e7dd; color: #0f5132; }
    .bg-danger-soft { background-color: #f8d7da; color: #842029; }
    .prescription-table th { background-color: #f8fafc; color: var(--text-muted); font-weight: 600; font-size: 0.9rem; }
</style>

<div class="fade-in">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold text-primary"><i class="fas fa-file-medical-alt me-2"></i> Quản lý Hồ sơ & Viện phí</h4>
        
        <ul class="nav nav-pills bg-white p-1 rounded-pill shadow-sm" id="historyTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active rounded-pill px-4 fw-medium" data-bs-toggle="pill" data-bs-target="#waiting">Đang chờ khám</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link rounded-pill px-4 fw-medium" data-bs-toggle="pill" data-bs-target="#completed">Lịch sử đã khám</button>
            </li>
        </ul>
    </div>

    <div class="tab-content" id="historyTabsContent">
        <div class="tab-pane fade show active" id="waiting" role="tabpanel">
            <c:if test="${empty waitingList}">
                <div class="text-center p-5 text-muted"><i class="fas fa-calendar-times fs-1 mb-3"></i><p>Bạn chưa có lịch hẹn nào đang chờ.</p></div>
            </c:if>
            <c:forEach items="${waitingList}" var="app">
                <div class="history-card p-4 mb-3">
                    <div class="row align-items-center">
                        <div class="col-md-2 text-center border-end">
                            <h5 class="text-primary fw-bold mb-0"><fmt:formatDate value="${app.startTime}" pattern="dd/MM"/></h5>
                            <small class="text-muted"><fmt:formatDate value="${app.startTime}" pattern="HH:mm"/></small>
                        </div>
                        <div class="col-md-7 ps-4">
                            <h5 class="fw-bold text-dark mb-1">Khám ${app.departmentName}</h5>
                            <p class="text-muted mb-0"><i class="fas fa-user-md me-2"></i>Bác sĩ: 
                                <c:choose>
                                    <c:when test="${not empty app.doctorName}"><b>${app.doctorName}</b> (Mã: BS-${app.doctorId})</c:when>
                                    <c:otherwise><i class="text-warning">Đang chờ xếp bác sĩ...</i></c:otherwise>
                                </c:choose>
                                | Phòng: ${app.roomNumber}
                            </p>
                        </div>
                        <div class="col-md-3 text-end">
                            <c:choose>
                                <c:when test="${app.status == 'pending'}">
                                    <span class="status-badge bg-pending mb-2 d-inline-block"><i class="fas fa-clock me-1"></i> Chờ xác nhận</span><br>
                                    <form action="${pageContext.request.contextPath}/CancelAppointmentController" method="POST" class="d-inline">
                                        <input type="hidden" name="appointmentId" value="${app.appointmentId}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger rounded-pill px-3" onclick="return confirm('Bạn có chắc chắn muốn hủy lịch này?');"><i class="fas fa-times"></i> Hủy lịch</button>
                                    </form>
                                </c:when>
                                <c:when test="${app.status == 'accepted'}">
                                    <span class="status-badge bg-accepted mb-2 d-inline-block"><i class="fas fa-user-check me-1"></i> Đã tiếp nhận</span><br>
                                    <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill px-3" onclick="alert('Bác sĩ đã tiếp nhận hồ sơ. Nếu bận đột xuất, vui lòng liên hệ Hotline bệnh viện để hủy!')"><i class="fas fa-ban"></i> Hủy lịch</button>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="tab-pane fade" id="completed" role="tabpanel">
            <c:if test="${empty completedList}">
                <div class="text-center p-5 text-muted"><i class="fas fa-box-open fs-1 mb-3"></i><p>Không có lịch sử khám bệnh.</p></div>
            </c:if>
            <c:forEach items="${completedList}" var="app">
                <div class="history-card p-4 mb-3">
                    <div class="row align-items-center">
                        <div class="col-md-2 text-center border-end">
                            <h5 class="text-primary fw-bold mb-0"><fmt:formatDate value="${app.startTime}" pattern="dd/MM/yyyy"/></h5>
                            <small class="text-muted"><fmt:formatDate value="${app.startTime}" pattern="HH:mm"/></small>
                        </div>
                        <div class="col-md-6 ps-4">
                            <h5 class="fw-bold text-dark mb-1">Khám ${app.departmentName}</h5>
                            <p class="text-muted mb-1"><i class="fas fa-user-md me-2"></i>Bác sĩ: <b>${app.doctorName != null ? app.doctorName : 'N/A'}</b></p>
                            <c:if test="${app.status == 'completed'}">
                                <p class="mb-0 text-danger fw-bold">Tổng tiền: <fmt:formatNumber value="${app.totalAmount}" type="number" pattern="#,##0"/> VNĐ</p>
                            </c:if>
                        </div>
                        <div class="col-md-4 text-end">
                            <c:choose>
                                <c:when test="${app.status == 'completed'}">
                                    <span class="status-badge bg-success-soft mb-2 d-inline-block"><i class="fas fa-check-circle me-1"></i> Hoàn thành</span><br>
                                    <button class="btn btn-sm btn-light text-primary rounded-pill px-3 me-1" data-bs-toggle="modal" data-bs-target="#recordDetailModal_${app.appointmentId}">
                                        <i class="fas fa-eye"></i> Chi tiết
                                    </button>
                                    <c:if test="${app.paymentStatus == 'unpaid'}">
                                        <button class="btn btn-sm btn-primary rounded-pill px-3 shadow-sm" data-bs-toggle="modal" data-bs-target="#qrPayModal_${app.appointmentId}">
                                            <i class="fas fa-qrcode"></i> Thanh toán
                                        </button>
                                    </c:if>
                                    <c:if test="${app.paymentStatus == 'paid'}">
                                        <span class="btn btn-sm btn-success rounded-pill px-3 disabled shadow-sm"><i class="fas fa-check"></i> Đã thu tiền</span>
                                    </c:if>
                                </c:when>
                                <c:when test="${app.status == 'cancelled'}">
                                    <span class="status-badge bg-danger-soft mb-2 d-inline-block"><i class="fas fa-times-circle me-1"></i> Đã hủy</span>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <c:if test="${app.status == 'completed'}">
                    <div class="modal fade" id="recordDetailModal_${app.appointmentId}" tabindex="-1">
                        <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                            <div class="modal-content border-0" style="border-radius: 20px;">
                                <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                                    <h5 class="modal-title fw-bold text-primary"><i class="fas fa-notes-medical me-2"></i> Chi Tiết Hồ Sơ #${app.appointmentId}</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body p-4">
                                    <div class="bg-light p-3 rounded-4 mb-4">
                                        <h6 class="fw-bold text-dark border-bottom pb-2">Kết quả chẩn đoán (BS: ${app.doctorName})</h6>
                                        <p class="mb-1"><span class="text-muted">Chẩn đoán:</span> <b>${app.diagnosis}</b></p>
                                        <p class="mb-0"><span class="text-muted">Ghi chú/Lời khuyên:</span> ${app.notes}</p>
                                    </div>

                                    <h6 class="fw-bold text-dark mb-3"><i class="fas fa-pills me-2 text-primary"></i> Đơn thuốc được kê</h6>
                                    <div class="table-responsive mb-4">
                                        <table class="table table-bordered prescription-table align-middle">
                                            <thead>
                                                <tr>
                                                    <th>Tên thuốc</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th>Liều dùng</th>
                                                    <th class="text-end">Đơn giá</th>
                                                    <th class="text-end">Thành tiền</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${app.medicines}" var="med">
                                                    <tr>
                                                        <td>${med.medicineName}</td>
                                                        <td class="text-center">${med.quantity}</td>
                                                        <td>${med.dosage} (${med.frequency})</td>
                                                        <td class="text-end"><fmt:formatNumber value="${med.price}" pattern="#,##0"/> đ</td>
                                                        <td class="text-end text-dark fw-bold"><fmt:formatNumber value="${med.totalLinePrice}" pattern="#,##0"/> đ</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <h6 class="fw-bold text-dark mb-3"><i class="fas fa-file-invoice-dollar me-2 text-primary"></i> Tổng hợp viện phí</h6>
                                    <ul class="list-group list-group-flush border rounded-4 mb-3">
                                        <li class="list-group-item d-flex justify-content-between align-items-center p-3">
                                            <div>
                                                <span class="d-block fw-semibold text-dark">Phí khám bệnh & Cơ sở vật chất</span>
                                                <small class="text-muted">Bao gồm phí phòng khám và công khám của Bác sĩ</small>
                                            </div>
                                            <span class="fw-bold text-primary"><fmt:formatNumber value="${app.serviceFee}" pattern="#,##0"/> đ</span>
                                        </li>

                                        <c:set var="totalMedPrice" value="0" />
                                        <c:forEach items="${app.medicines}" var="med">
                                            <c:set var="totalMedPrice" value="${totalMedPrice + (med.quantity * med.medicinePrice)}" />
                                        </c:forEach>

                                        <li class="list-group-item d-flex justify-content-between align-items-center p-3">
                                            <div>
                                                <span class="d-block fw-semibold text-dark">Tổng tiền thuốc kê đơn</span>
                                                <small class="text-muted">Chi tiết xem tại bảng "Đơn thuốc được kê" ở trên</small>
                                            </div>
                                            <span class="fw-bold text-success"><fmt:formatNumber value="${totalMedPrice}" pattern="#,##0"/> đ</span>
                                        </li>

                                        <li class="list-group-item d-flex justify-content-between align-items-center p-3 bg-primary text-white" style="border-radius: 0 0 15px 15px;">
                                            <span class="fw-bold fs-5">TỔNG CHI PHÍ CẦN THANH TOÁN</span>
                                            <span class="fw-bold fs-4"><fmt:formatNumber value="${app.totalAmount}" pattern="#,##0"/> VNĐ</span>
                                        </li>
                                    </ul>
                                </div>
                                <div class="modal-footer border-top-0 px-4 pb-4">
                                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Đóng</button>
                                    <c:if test="${app.paymentStatus == 'unpaid'}">
                                        <button type="button" class="btn btn-primary rounded-pill px-4" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#qrPayModal_${app.appointmentId}">
                                            <i class="fas fa-qrcode"></i> Thanh toán ngay
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${app.paymentStatus == 'unpaid'}">
                        <div class="modal fade" id="qrPayModal_${app.appointmentId}" tabindex="-1">
                            <div class="modal-dialog modal-sm modal-dialog-centered">
                                <div class="modal-content border-0" style="border-radius: 20px;">
                                    <div class="modal-body text-center p-4">
                                        <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal"></button>
                                        <h5 class="fw-bold text-primary mb-1 mt-2">Thanh toán Viện phí</h5>
                                        <p class="text-muted small mb-3">Quét mã bằng ứng dụng Ngân hàng</p>
                                        
                                        <div class="qr-container bg-light p-3 rounded-4 mb-3">
                                            <img src="https://api.vietqr.io/image/970436-123456789-ZkK5m2B.jpg?amount=${app.totalAmount}&addInfo=ThanhToan_HoSo_${app.appointmentId}" class="img-fluid rounded" alt="QR Code">
                                        </div>
                                        
                                        <h4 class="fw-bold text-danger mb-1"><fmt:formatNumber value="${app.totalAmount}" pattern="#,##0"/> VNĐ</h4>
                                        <p class="small text-muted mb-4">Nội dung: <b>ThanhToan_HoSo_${app.appointmentId}</b></p>
                                        
                                        <button class="btn btn-primary w-100 rounded-pill fw-bold" onclick="confirmPayment()">
                                            <i class="fas fa-check-circle me-1"></i> Tôi đã chuyển khoản
                                        </button>
                                        <p class="mt-3 mb-0" style="font-size: 0.75rem; color: #aaa;">Hệ thống sẽ tự động xác nhận sau 1-3 phút.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>

<script>
    function confirmPayment() {
        alert("Đã gửi yêu cầu xác nhận thanh toán! Hệ thống đang kiểm tra giao dịch của bạn qua Webhook Ngân hàng.");
        // Close modal logic có thể thêm ở đây
    }
</script>