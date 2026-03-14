<%-- 
    Document   : medicalHistory
    Created on : Mar 14, 2026, 2:35:39 AM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .history-card { border-radius: 16px; border: 1px solid #edf2f9; transition: all 0.3s; background: #fff; box-shadow: 0 2px 12px rgba(0,0,0,0.02); }
    .history-card:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(67, 97, 238, 0.1); border-color: var(--primary-light); }
    .status-badge { padding: 6px 12px; border-radius: 20px; font-weight: 500; font-size: 0.85rem; }
    .bg-pending { background-color: #fff3cd; color: #856404; }
    .bg-success-soft { background-color: #d1e7dd; color: #0f5132; }
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
            <div class="history-card p-4 mb-3">
                <div class="row align-items-center">
                    <div class="col-md-2 text-center border-end">
                        <h5 class="text-primary fw-bold mb-0">18/03</h5>
                        <small class="text-muted">08:00 - 09:00</small>
                    </div>
                    <div class="col-md-7 ps-4">
                        <h5 class="fw-bold text-dark mb-1">Khám Nội Tổng Hợp</h5>
                        <p class="text-muted mb-0"><i class="fas fa-user-md me-2"></i>Bác sĩ: <b>Phạm Mỹ Linh</b> | Phòng: 105</p>
                    </div>
                    <div class="col-md-3 text-end">
                        <span class="status-badge bg-pending mb-2 d-inline-block"><i class="fas fa-clock me-1"></i> Chờ xác nhận</span>
                        <br>
                        <button class="btn btn-sm btn-outline-danger rounded-pill px-3"><i class="fas fa-times"></i> Hủy lịch</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="tab-pane fade" id="completed" role="tabpanel">
            <div class="history-card p-4 mb-3">
                <div class="row align-items-center">
                    <div class="col-md-2 text-center border-end">
                        <h5 class="text-primary fw-bold mb-0">12/03</h5>
                        <small class="text-muted">08:00 - 08:30</small>
                    </div>
                    <div class="col-md-6 ps-4">
                        <h5 class="fw-bold text-dark mb-1">Khám Tim Mạch</h5>
                        <p class="text-muted mb-1"><i class="fas fa-user-md me-2"></i>Bác sĩ: <b>Nguyễn Bá Tĩnh</b></p>
                        <p class="mb-0 text-danger fw-bold">Tổng tiền: 210,000 VNĐ</p>
                    </div>
                    <div class="col-md-4 text-end">
                        <span class="status-badge bg-success-soft mb-2 d-inline-block"><i class="fas fa-check-circle me-1"></i> Hoàn thành</span>
                        <br>
                        <button class="btn btn-sm btn-light text-primary rounded-pill px-3 me-1" data-bs-toggle="modal" data-bs-target="#recordDetailModal">
                            <i class="fas fa-eye"></i> Chi tiết
                        </button>
                        <button class="btn btn-sm btn-primary rounded-pill px-3 shadow-sm" data-bs-toggle="modal" data-bs-target="#qrPayModal">
                            <i class="fas fa-qrcode"></i> Thanh toán
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="recordDetailModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-header border-bottom-0 pb-0 mt-3 px-4">
                <h5 class="modal-title fw-bold text-primary"><i class="fas fa-notes-medical me-2"></i> Chi Tiết Hồ Sơ Bệnh Án</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <div class="bg-light p-3 rounded-4 mb-4">
                    <h6 class="fw-bold text-dark border-bottom pb-2">Kết quả chẩn đoán (Bác sĩ Nguyễn Bá Tĩnh)</h6>
                    <p class="mb-1"><span class="text-muted">Chẩn đoán:</span> <b>Rối loạn nhịp tim nhẹ</b></p>
                    <p class="mb-0"><span class="text-muted">Ghi chú/Lời khuyên:</span> Cần theo dõi thêm 1 tuần, hạn chế thức khuya.</p>
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
                            <tr>
                                <td>Amlodipine 5mg</td>
                                <td class="text-center">30 Viên</td>
                                <td>1 viên/Sáng</td>
                                <td class="text-end">3,000 đ</td>
                                <td class="text-end text-dark fw-bold">90,000 đ</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <h6 class="fw-bold text-dark mb-3"><i class="fas fa-file-invoice-dollar me-2 text-primary"></i> Tổng hợp viện phí</h6>
                <ul class="list-group list-group-flush border rounded-4 mb-3">
                    <li class="list-group-item d-flex justify-content-between align-items-center p-3">
                        <div>
                            <span class="d-block fw-semibold text-dark">Phí khám phòng: Khám Thường Nội Khoa</span>
                            <small class="text-muted">Đơn giá: 150,000 đ/lần x 1 lần</small>
                        </div>
                        <span class="fw-bold">150,000 đ</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center p-3">
                        <span class="fw-semibold text-dark">Tiền thuốc</span>
                        <span class="fw-bold">90,000 đ</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center p-3 bg-primary text-white" style="border-radius: 0 0 15px 15px;">
                        <span class="fw-bold fs-5">TỔNG CỘNG CẦN THANH TOÁN</span>
                        <span class="fw-bold fs-4">240,000 VNĐ</span>
                    </li>
                </ul>
            </div>
            <div class="modal-footer border-top-0 px-4 pb-4">
                <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-primary rounded-pill px-4" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#qrPayModal">
                    <i class="fas fa-qrcode"></i> Thanh toán ngay
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="qrPayModal" tabindex="-1">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content border-0" style="border-radius: 20px;">
            <div class="modal-body text-center p-4">
                <button type="button" class="btn-close position-absolute top-0 end-0 m-3" data-bs-dismiss="modal"></button>
                <h5 class="fw-bold text-primary mb-1 mt-2">Thanh toán Viện phí</h5>
                <p class="text-muted small mb-3">Quét mã bằng ứng dụng Ngân hàng</p>
                
                <div class="qr-container bg-light p-3 rounded-4 mb-3">
                    <img src="https://api.vietqr.io/image/970436-123456789-ZkK5m2B.jpg?amount=240000&addInfo=ThanhToanVienPhi_BenhNhan_BN88329" class="img-fluid rounded" alt="QR Code">
                </div>
                
                <h4 class="fw-bold text-danger mb-1">240,000 VNĐ</h4>
                <p class="small text-muted mb-4">Nội dung: <b>ThanhToanVienPhi_BN88329</b></p>
                
                <button class="btn btn-primary w-100 rounded-pill fw-bold" onclick="confirmPayment()">
                    <i class="fas fa-check-circle me-1"></i> Tôi đã chuyển khoản
                </button>
                <p class="mt-3 mb-0" style="font-size: 0.75rem; color: #aaa;">
                    Hệ thống sẽ tự động xác nhận sau 1-3 phút. Nếu chưa thanh toán, hệ thống sẽ gửi Email nhắc nhở sau 1 tuần.
                </p>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmPayment() {
        alert("Đã gửi yêu cầu xác nhận thanh toán! Hệ thống đang kiểm tra giao dịch của bạn qua Webhook Ngân hàng.");
        var myModalEl = document.getElementById('qrPayModal');
        var modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
</script>