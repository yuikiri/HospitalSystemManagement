<%-- 
    Document   : searchDoctor
    Created on : Mar 14, 2026, 2:35:47 AM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .search-col { height: calc(100vh - 180px); overflow-y: auto; padding-right: 15px; }
    /* Thanh cuộn tàng hình cho đẹp */
    .search-col::-webkit-scrollbar { width: 4px; }
    .search-col::-webkit-scrollbar-thumb { background-color: #e2e8f0; border-radius: 10px; }
    
    .doc-card, .dept-card, .room-card { background: #fff; border-radius: 12px; padding: 15px; margin-bottom: 15px; border: 1px solid #edf2f9; transition: 0.2s; }
    .doc-card:hover, .dept-card:hover, .room-card:hover { border-color: var(--primary-light); box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
</style>

<div class="row fade-in">
    <div class="col-12 mb-3">
        <h4 class="fw-bold text-primary"><i class="fas fa-search me-2"></i> Tra Cứu Thông Tin Dịch Vụ</h4>
    </div>

    <div class="col-lg-4 col-md-12 mb-4">
        <div class="bg-white p-3 rounded-4 shadow-sm h-100">
            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3"><i class="fas fa-user-md text-primary me-2"></i> Đội ngũ Bác sĩ</h6>
            
            <div class="input-group mb-3">
                <input type="text" class="form-control rounded-start-pill border-end-0 bg-light" placeholder="Tìm tên bác sĩ...">
                <button class="btn btn-light border border-start-0 rounded-end-pill text-primary"><i class="fas fa-filter"></i> Lọc</button>
            </div>

            <div class="search-col">
                <div class="doc-card d-flex align-items-center">
                    <img src="https://ui-avatars.com/api/?name=NT&background=random" class="rounded-circle me-3" width="50" height="50">
                    <div>
                        <h6 class="mb-0 fw-bold">Nguyễn Bá Tĩnh</h6>
                        <small class="text-muted"><i class="fas fa-mars text-primary"></i> Nam | Khoa Tim Mạch</small>
                    </div>
                </div>
                <div class="doc-card d-flex align-items-center">
                    <img src="https://ui-avatars.com/api/?name=LH&background=random" class="rounded-circle me-3" width="50" height="50">
                    <div>
                        <h6 class="mb-0 fw-bold">Lê Thị Hương</h6>
                        <small class="text-muted"><i class="fas fa-venus text-danger"></i> Nữ | Khoa Tim Mạch</small>
                    </div>
                </div>
                </div>
            
            <nav class="mt-3">
                <ul class="pagination pagination-sm justify-content-center mb-0">
                    <li class="page-item disabled"><a class="page-link rounded-start-pill" href="#">Trang trước</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link rounded-end-pill" href="#">Tiếp</a></li>
                </ul>
            </nav>
        </div>
    </div>

    <div class="col-lg-4 col-md-12 mb-4">
        <div class="bg-white p-3 rounded-4 shadow-sm h-100">
            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3"><i class="fas fa-hospital text-success me-2"></i> Chuyên Khoa</h6>
            <div class="search-col">
                <div class="dept-card">
                    <h6 class="fw-bold text-primary mb-1">Khoa Tim Mạch</h6>
                    <p class="text-muted small mb-0">Chuyên khám và điều trị các bệnh lý liên quan đến tim mạch, huyết áp.</p>
                </div>
                <div class="dept-card">
                    <h6 class="fw-bold text-primary mb-1">Khoa Nhi</h6>
                    <p class="text-muted small mb-0">Chăm sóc sức khỏe trẻ sơ sinh và trẻ nhỏ với đội ngũ bác sĩ tận tâm.</p>
                </div>
                <div class="dept-card">
                    <h6 class="fw-bold text-primary mb-1">Khoa Ngoại Thần Kinh</h6>
                    <p class="text-muted small mb-0">Phẫu thuật chấn thương sọ não, cột sống bằng công nghệ cao.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-4 col-md-12 mb-4">
        <div class="bg-white p-3 rounded-4 shadow-sm h-100">
            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3"><i class="fas fa-bed text-warning me-2"></i> Bảng giá Phòng & Dịch vụ</h6>
            <div class="search-col">
                <div class="room-card">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="fw-bold mb-0">Khám Thường Nội Khoa</h6>
                        <span class="badge bg-success">Đang hoạt động</span>
                    </div>
                    <h5 class="text-danger fw-bold mb-0">150,000 đ <small class="text-muted fs-6">/lần</small></h5>
                </div>
                <div class="room-card">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="fw-bold mb-0">Phòng Lưu Bệnh VIP (1 Giường)</h6>
                        <span class="badge bg-success">Đang hoạt động</span>
                    </div>
                    <h5 class="text-danger fw-bold mb-0">1,200,000 đ <small class="text-muted fs-6">/ngày</small></h5>
                </div>
                <div class="room-card">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="fw-bold mb-0">Phòng Phẫu Thuật Lớn</h6>
                        <span class="badge bg-secondary">Bảo trì</span>
                    </div>
                    <h5 class="text-danger fw-bold mb-0">5,000,000 đ <small class="text-muted fs-6">/ca</small></h5>
                </div>
            </div>
        </div>
    </div>
</div>