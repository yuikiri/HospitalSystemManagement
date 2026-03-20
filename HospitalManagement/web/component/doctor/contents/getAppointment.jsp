<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="content-section fade-in">
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
        <h4 class="fw-bold text-primary mb-0"><i class="bi bi-clipboard-pulse me-2"></i> Tiếp Nhận Bệnh Nhân</h4>
    </div>

    <ul class="nav nav-pills mb-4 gap-2" id="appointmentTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active fw-bold rounded-pill px-4" id="pending-tab" data-bs-toggle="pill" data-bs-target="#pending" type="button" role="tab">
                <i class="bi bi-hourglass-split me-1"></i> Chờ Tiếp Nhận
                <span class="badge bg-danger ms-2 rounded-pill">${pendingList != null ? pendingList.size() : 0}</span>
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link fw-bold rounded-pill px-4" id="accepted-tab" data-bs-toggle="pill" data-bs-target="#accepted" type="button" role="tab">
                <i class="bi bi-person-check me-1"></i> Ca Đang Khám
                <span class="badge bg-success ms-2 rounded-pill">${acceptedList != null ? acceptedList.size() : 0}</span>
            </button>
        </li>
    </ul>

    <div class="tab-content" id="appointmentTabsContent">
        
        <div class="tab-pane fade show active" id="pending" role="tabpanel">
            <div class="row g-4">
                <c:if test="${empty pendingList}">
                    <div class="col-12 text-center p-5 text-muted bg-white rounded-4 shadow-sm border border-dashed">
                        <i class="bi bi-inbox fs-1 mb-3 text-secondary d-block"></i>
                        <h5 class="fw-bold">Không có lịch hẹn chờ</h5>
                        <p class="mb-0">Khoa của Bác sĩ hiện chưa có bệnh nhân nào đặt lịch mới.</p>
                    </div>
                </c:if>
                <c:forEach items="${pendingList}" var="app">
                    <div class="col-md-6 col-xl-4">
                        <div class="profile-card p-4 shadow-sm bg-white border-start border-5 border-warning rounded-4 h-100 transition-hover">
                            <h5 class="fw-bold text-dark mb-1">${app.patientName}</h5>
                            <p class="text-muted fw-semibold mb-3 border-bottom pb-2">
                                <i class="bi bi-door-open text-primary me-1"></i> Phòng ${app.roomNumber} <small class="text-secondary ms-1">(${app.departmentName})</small>
                            </p>
                            <p class="mb-2 text-dark"><i class="bi bi-calendar-event text-success me-2"></i><fmt:formatDate value="${app.startTime}" pattern="dd/MM/yyyy"/></p>
                            <p class="mb-4 text-dark"><i class="bi bi-clock text-warning me-2"></i><fmt:formatDate value="${app.startTime}" pattern="HH:mm"/> - <fmt:formatDate value="${app.endTime}" pattern="HH:mm"/></p>
                            
                            <form action="${pageContext.request.contextPath}/MainController" method="POST">
                                <input type="hidden" name="action" value="AcceptAppointment">
                                <input type="hidden" name="appointmentId" value="${app.id}">
                                <button type="submit" class="btn btn-warning text-dark w-100 fw-bold rounded-pill shadow-sm">
                                    <i class="bi bi-check2-circle me-1"></i> Nhận Ca Khám Này
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="tab-pane fade" id="accepted" role="tabpanel">
            <div class="row g-4">
                <c:if test="${empty acceptedList}">
                    <div class="col-12 text-center p-5 text-muted bg-white rounded-4 shadow-sm border border-dashed">
                        <i class="bi bi-emoji-smile fs-1 mb-3 text-secondary d-block"></i>
                        <h5 class="fw-bold">Không có ca đang khám</h5>
                        <p class="mb-0">Bác sĩ chưa tiếp nhận ca khám nào từ danh sách chờ.</p>
                    </div>
                </c:if>
                <c:forEach items="${acceptedList}" var="app">
                    <div class="col-md-6 col-xl-4">
                        <div class="profile-card p-4 shadow-sm bg-white border-start border-5 border-success rounded-4 h-100 transition-hover">
                            <h5 class="fw-bold text-dark mb-1">${app.patientName}</h5>
                            <p class="text-muted fw-semibold mb-3 border-bottom pb-2">
                                <i class="bi bi-door-open text-primary me-1"></i> Phòng ${app.roomNumber} <small class="text-secondary ms-1">(${app.departmentName})</small>
                            </p>
                            <p class="mb-2 text-dark"><i class="bi bi-calendar-event text-success me-2"></i><fmt:formatDate value="${app.startTime}" pattern="dd/MM/yyyy"/></p>
                            <p class="mb-4 text-dark"><i class="bi bi-clock text-warning me-2"></i><fmt:formatDate value="${app.startTime}" pattern="HH:mm"/> - <fmt:formatDate value="${app.endTime}" pattern="HH:mm"/></p>
                            
                            <button type="button" class="btn btn-success w-100 fw-bold rounded-pill shadow-sm" onclick="window.openPrescriptionModal('${app.id}', '${app.patientName}')">
                                <i class="bi bi-pencil-square me-1"></i> Khám & Kê Đơn
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<div id="hiddenMedicineStorage" style="display: none;">
    <c:forEach items="${medicineList}" var="med">
        <div class="medicine-data" 
             data-id="${med.id}" 
             data-name="${med.name}" 
             data-unit="${med.unit}" 
             data-price="${med.price}" 
             data-stock="${med.stockQuantity}">
        </div>
    </c:forEach>
</div>

<div class="modal fade" id="prescriptionModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
            <div class="modal-header bg-success text-white rounded-top-4">
                <h5 class="modal-title fw-bold"><i class="bi bi-file-earmark-medical me-2"></i> Hồ Sơ Khám Bệnh - <span id="modalPatientName" class="text-warning"></span></h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            
            <form action="${pageContext.request.contextPath}/MainController" method="POST" id="prescriptionForm">
                <div class="modal-body p-4 bg-light">
                    <input type="hidden" name="action" value="CompleteAppointment">
                    <input type="hidden" name="appointmentId" id="modalAppointmentId">

                    <div class="card border-0 shadow-sm mb-4 rounded-4">
                        <div class="card-header bg-white border-0 pt-4 pb-0">
                            <h6 class="fw-bold text-primary"><i class="bi bi-clipboard2-pulse me-2"></i>Chẩn đoán & Phí dịch vụ</h6>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-8">
                                    <label class="form-label fw-semibold text-muted small">Chẩn đoán bệnh <span class="text-danger">*</span></label>
                                    <textarea class="form-control rounded-3" name="diagnosis" rows="2" placeholder="Ví dụ: Viêm họng hạt cấp tính..." required></textarea>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold text-muted small">Lời dặn bác sĩ</label>
                                    <textarea class="form-control rounded-3" name="notes" rows="2" placeholder="Ví dụ: Kiêng nước đá..."></textarea>
                                </div>
                                
                                <div class="col-md-6 mt-3">
                                    <label class="form-label fw-semibold text-muted small">Tiền công khám (VNĐ) <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="doctorFee" id="inputDoctorFee" value="150000" min="0" onchange="window.calculateTotal()" required>
                                </div>
                                <div class="col-md-6 mt-3">
                                    <label class="form-label fw-semibold text-muted small">Số ngày lưu bệnh (Phòng) <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="roomDays" id="inputRoomDays" value="0" min="0" onchange="window.calculateTotal()" required>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card border-0 shadow-sm rounded-4">
                        <div class="card-header bg-white border-0 pt-4 pb-0">
                            <h6 class="fw-bold text-success mb-3"><i class="bi bi-capsule me-2"></i>Kê Đơn Thuốc</h6>
                            
                            <div class="row g-2 mb-3">
                                <div class="col-md-5">
                                    <div class="input-group">
                                        <input type="text" class="form-control border-success" id="searchMedicine" placeholder="Gõ tên thuốc để tìm...">
                                        <button class="btn btn-success" type="button" onclick="window.filterMedicines()"><i class="bi bi-search"></i> Lọc</button>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <select class="form-select border-success" id="medicineSelect">
                                        <option value="" selected disabled>-- Chọn thuốc từ danh sách --</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-success fw-bold w-100" type="button" onclick="window.addMedicine()"><i class="bi bi-plus-lg"></i> Thêm</button>
                                </div>
                            </div>
                        </div>
                        
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle border" id="prescriptionTable">
                                    <thead class="table-light">
                                        <tr>
                                            <th width="25%">Tên Thuốc</th>
                                            <th width="15%">Số lượng</th>
                                            <th width="15%">Đơn giá</th>
                                            <th width="15%">Liều dùng</th>
                                            <th width="15%">Tần suất</th>
                                            <th width="10%">Số ngày</th>
                                            <th width="5%"></th>
                                        </tr>
                                    </thead>
                                    <tbody id="medicineListBody">
                                        </tbody>
                                </table>
                            </div>
                            <div id="emptyMedicineWarning" class="text-center text-muted my-3 small">
                                <em>Chưa kê loại thuốc nào. Bệnh nhân sẽ chỉ tính tiền công khám.</em>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="modal-footer bg-white border-top-0 pt-3 pb-4 px-4 d-flex justify-content-between">
                    <div>
                        <h5 class="fw-bold text-danger mb-0">Tổng tiền dự kiến: <span id="displayTotal">150,000</span> VNĐ</h5>
                        <small class="text-muted">(Đã cộng tiền khám, phòng và thuốc)</small>
                    </div>
                    <div>
                        <button type="button" class="btn btn-light rounded-pill px-4 border" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-success rounded-pill px-5 fw-bold shadow-sm"><i class="bi bi-check-circle me-2"></i> Khám Xong & Xuất HĐ</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<style>
    .border-dashed { border-style: dashed !important; border-width: 2px !important; }
    .transition-hover { transition: all 0.3s ease; }
    .transition-hover:hover { transform: translateY(-5px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important; }
    .nav-pills .nav-link.active { background-color: var(--primary-color) !important; color: #fff !important; }
    .nav-pills .nav-link { background-color: #fff; color: var(--text-muted); border: 1px solid #e0eafc; }
</style>

<script>
    // Khai báo biến toàn cục
    window.ROOM_PRICE_PER_DAY = 200000; 

    // 1. HÀM TẠO DANH SÁCH THUỐC TỪ KHO ẨN
    window.renderMedicineDropdown = function(filterText) {
        filterText = filterText || "";
        let select = document.getElementById("medicineSelect");
        let storage = document.querySelectorAll('#hiddenMedicineStorage .medicine-data');
        
        let html = '<option value="" selected disabled>-- Vui lòng chọn thuốc --</option>';
        let count = 0;
        let searchKeyword = filterText.toLowerCase().trim();

        storage.forEach(function(el) {
            let name = el.getAttribute('data-name');
            if (name.toLowerCase().includes(searchKeyword)) {
                let id = el.getAttribute('data-id');
                let unit = el.getAttribute('data-unit');
                let price = parseFloat(el.getAttribute('data-price'));
                let stock = el.getAttribute('data-stock');
                let formattedPrice = price.toLocaleString('vi-VN');
                
                // Dùng chuỗi thường, KHÔNG dùng backtick ` `
                html += '<option value="' + id + '" data-name="' + name + '" data-unit="' + unit + '" data-price="' + price + '" data-stock="' + stock + '">' + name + ' (' + unit + ') - Tồn: ' + stock + ' - Giá: ' + formattedPrice + ' đ</option>';
                count++;
            }
        });

        select.innerHTML = html;
        if (filterText !== "" && count > 0) select.selectedIndex = 1;
    };

    // 2. MỞ MODAL
    window.openPrescriptionModal = function(appId, patientName) {
        document.getElementById('modalAppointmentId').value = appId;
        document.getElementById('modalPatientName').innerText = patientName;
        document.getElementById('medicineListBody').innerHTML = '';
        document.getElementById('emptyMedicineWarning').style.display = 'block';
        
        document.getElementById('prescriptionForm').reset();
        document.getElementById('inputDoctorFee').value = 150000;
        document.getElementById('inputRoomDays').value = 0;
        document.getElementById('searchMedicine').value = '';
        
        window.renderMedicineDropdown(""); 
        window.calculateTotal(); 
        new bootstrap.Modal(document.getElementById('prescriptionModal')).show();
    };

    // 3. TÌM KIẾM
    window.filterMedicines = function() {
        let input = document.getElementById("searchMedicine").value;
        window.renderMedicineDropdown(input);
    };

    // 4. THÊM THUỐC VÀO BẢNG (ĐÃ FIX LỖI MẤT BIẾN JSP)
    window.addMedicine = function() {
        const select = document.getElementById('medicineSelect');
        if (select.selectedIndex <= 0) {
            alert("Sếp chưa chọn thuốc mà!");
            return;
        }
        
        const option = select.options[select.selectedIndex];
        const medId = option.value;
        const medName = option.getAttribute('data-name');
        const medUnit = option.getAttribute('data-unit');
        const medPrice = parseFloat(option.getAttribute('data-price')) || 0;
        const medStock = parseInt(option.getAttribute('data-stock')) || 0;
        
        if (medStock <= 0) {
            alert('Thuốc này hết sạch trong kho rồi sếp!'); return;
        }

        if (document.getElementById('row_med_' + medId)) {
            alert('Thuốc này có trong đơn rồi, sếp bấm nút (+) để tăng số lượng nhé!'); return;
        }

        document.getElementById('emptyMedicineWarning').style.display = 'none';
        const tbody = document.getElementById('medicineListBody');
        const tr = document.createElement('tr');
        tr.id = 'row_med_' + medId;
        tr.className = 'med-row'; 
        tr.setAttribute('data-price', medPrice); 
        
        // ĐÂY NÀY SẾP! Đã chuyển sang nối chuỗi truyền thống để JSP không phá code JS nữa
        tr.innerHTML = 
            '<td class="fw-semibold text-primary">' + 
                medName + ' <br><small class="text-muted">Kho: ' + medStock + '</small>' +
                '<input type="hidden" name="medIds" value="' + medId + '">' +
            '</td>' +
            '<td>' +
                '<div class="input-group input-group-sm" style="width: 110px;">' +
                    '<button class="btn btn-outline-secondary" type="button" onclick="window.changeQty(\'' + medId + '\', -1, ' + medStock + ')"><i class="bi bi-dash"></i></button>' +
                    '<input type="number" class="form-control text-center med-qty" name="medQty_' + medId + '" id="qty_' + medId + '" value="1" readonly>' +
                    '<button class="btn btn-outline-secondary" type="button" onclick="window.changeQty(\'' + medId + '\', 1, ' + medStock + ')"><i class="bi bi-plus"></i></button>' +
                '</div>' +
            '</td>' +
            '<td class="text-danger fw-bold">' + medPrice.toLocaleString('vi-VN') + ' đ</td>' +
            '<td><input type="text" class="form-control form-control-sm" name="medDosage_' + medId + '" placeholder="1 viên" required></td>' +
            '<td><input type="text" class="form-control form-control-sm" name="medFreq_' + medId + '" placeholder="Sáng, Tối" required></td>' +
            '<td><input type="number" class="form-control form-control-sm text-center" name="medDuration_' + medId + '" placeholder="Ngày" min="1" required></td>' +
            '<td class="text-center">' +
                '<button type="button" class="btn btn-sm btn-outline-danger border-0" onclick="window.removeMedicine(\'' + medId + '\')"><i class="bi bi-trash3"></i></button>' +
            '</td>';

        tbody.appendChild(tr);
        window.calculateTotal();
    };

    // 5. TĂNG GIẢM SỐ LƯỢNG
    window.changeQty = function(medId, change, maxStock) {
        let input = document.getElementById('qty_' + medId);
        let currentVal = parseInt(input.value) || 1;
        let newVal = currentVal + change;
        
        if (newVal >= 1 && newVal <= maxStock) {
            input.value = newVal;
            window.calculateTotal(); 
        } else if (newVal > maxStock) {
            alert("Trong kho chỉ còn " + maxStock + " thôi sếp ơi!");
        }
    };

    // 6. XÓA DÒNG THUỐC
    window.removeMedicine = function(medId) {
        const row = document.getElementById('row_med_' + medId);
        if (row) {
            row.remove();
            const tbody = document.getElementById('medicineListBody');
            if (tbody.rows.length === 0) {
                document.getElementById('emptyMedicineWarning').style.display = 'block';
            }
            window.calculateTotal();
        }
    };

    // 7. TÍNH TỔNG TIỀN REAL-TIME
    window.calculateTotal = function() {
        let docFee = parseFloat(document.getElementById('inputDoctorFee').value) || 0;
        let roomDays = parseInt(document.getElementById('inputRoomDays').value) || 0;
        let total = docFee + (roomDays * window.ROOM_PRICE_PER_DAY);
        
        let rows = document.querySelectorAll('.med-row');
        rows.forEach(row => {
            let price = parseFloat(row.getAttribute('data-price')) || 0;
            let qty = parseInt(row.querySelector('.med-qty').value) || 0;
            total += (price * qty);
        });
        
        document.getElementById('displayTotal').innerText = total.toLocaleString('vi-VN');
    };
</script>
