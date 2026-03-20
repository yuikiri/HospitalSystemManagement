<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Kho dược | Medicare Pro</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #0d6efd 0%, #0052cc 100%);
            --soft-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            --hover-shadow: 0 15px 35px rgba(0, 0, 0, 0.08);
            --text-main: #2b3674;
            --text-muted: #a3aed0;
            --bg-main: #f4f7fe;
        }

        body { 
            background: var(--bg-main); 
            font-family: 'Plus Jakarta Sans', sans-serif; 
            color: #4a5568;
        }
        
        .admin-container { max-width: 1400px; margin: 0 auto; }

        /* Card Styling */
        .card { border: none; border-radius: 20px; box-shadow: var(--soft-shadow); background: #ffffff; transition: all 0.3s ease; }
        .card:hover { box-shadow: var(--hover-shadow); }

        /* Table Enhancements */
        .table > :not(caption) > * > * { padding: 1.1rem 1rem; border-bottom-color: #f1f5f9; }
        thead th {
            font-size: 0.75rem; text-transform: uppercase; letter-spacing: 1px;
            font-weight: 700; color: var(--text-muted); background: transparent !important;
        }
        tbody tr:hover { background-color: #fafbfc !important; transform: scale(1.001); box-shadow: inset 4px 0 0 0 #0d6efd; }

        /* Inputs */
        .search-box, .filter-select {
            border-radius: 14px; border: 1px solid #e2e8f0; background-color: #fff !important;
            padding: 10px 18px; box-shadow: 0 2px 5px rgba(0,0,0,0.02);
        }
        
        /* Action Buttons */
        .btn-action {
            border-radius: 10px; width: 34px; height: 34px; display: inline-flex;
            align-items: center; justify-content: center; transition: all 0.2s ease; border: none; background: #f8fafc;
        }
        .btn-action.edit:hover { background: #0d6efd; color: white !important; }
        .btn-action.delete:hover { background: #ef4444; color: white !important; }

        /* Badges */
        .badge-stock { border-radius: 8px; font-weight: 600; font-size: 0.75rem; }
        .pill-unit { background: #eff6ff; color: #1d4ed8; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }

        /* Modal Custom */
        .modal-content { border: none; border-radius: 24px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.2); }
        .form-control, .form-select { border-radius: 12px; padding: 10px 15px; border: 1px solid #e0e5f2; }
    </style>
</head>
<body>

<div class="container-fluid admin-container py-4 px-xl-5">

    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
        <div>
            <h3 class="fw-bold mb-1" style="color: var(--text-main);">Quản lý Kho dược</h3>
            <p class="text-muted mb-0"><i class="fa fa-pills me-2"></i>Danh mục thuốc & vật tư y tế Medicare</p>
        </div>

        <div class="d-flex gap-2">
            <a href="AdminController?action=dashboard" class="btn btn-light shadow-sm fw-bold border" style="border-radius: 12px;">
                <i class="fa fa-arrow-left me-2"></i>Dashboard
            </a>
            <button class="btn btn-primary shadow-sm" style="border-radius: 12px;" data-bs-toggle="modal" data-bs-target="#addMedicineModal">
                <i class="fa fa-plus-circle me-2"></i>Nhập thuốc mới
            </button>
        </div>
    </div>

    <div class="card p-3 mb-4">
        <form action="AdminController" method="get" class="row g-3 align-items-center">
            <input type="hidden" name="action" value="medicine">
            <div class="col-md-8">
                <div class="input-group search-box">
                    <span class="input-group-text border-0 pe-2 bg-transparent"><i class="fa fa-search text-muted"></i></span>
                    <input type="text" name="search" class="form-control border-0 shadow-none p-0" 
                           placeholder="Tìm kiếm theo tên thuốc hoặc công dụng..." value="${currentSearch}">
                </div>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100" style="border-radius: 12px; padding: 10px;">Tìm kiếm</button>
            </div>
            <div class="col-md-2">
                <a href="AdminController?action=medicineTrash" class="btn btn-outline-danger w-100 fw-bold" style="border-radius: 12px; padding: 10px;">
                    <i class="fa fa-trash-can me-2"></i>Thùng rác
                </a>
            </div>
        </form>
    </div>

    <div class="card shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th class="ps-4">ID</th>
                        <th>Tên thuốc</th>
                        <th class="text-center">Đơn vị</th>
                        <th class="text-end">Đơn giá</th>
                        <th class="text-center">Tồn kho</th>
                        <th>Mô tả / Công dụng</th>
                        <th class="text-end pe-4">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${medicineList}">
                        <tr>
                            <td class="ps-4 text-muted fw-bold">#${m.id}</td>
                            <td><span class="fw-bold" style="color: var(--text-main);">${m.name}</span></td>
                            <td class="text-center"><span class="pill-unit">${m.unit}</span></td>
                            <td class="text-end fw-bold text-primary">
                                <fmt:formatNumber value="${m.price}" pattern="#,###"/> đ
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${m.stockQuantity <= 10}">
                                        <span class="badge badge-stock bg-danger bg-opacity-10 text-danger px-3 py-2">${m.stockQuantity} (Sắp hết)</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-stock bg-success bg-opacity-10 text-success px-3 py-2">${m.stockQuantity}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="text-muted small fw-500">${m.description}</span></td>
                            <td class="text-end pe-4">
                                <div class="d-flex justify-content-end gap-2">
                                    <button class="btn-action edit text-primary" title="Sửa" 
                                            data-bs-toggle="modal" data-bs-target="#editMedicineModal"
                                            onclick="openEditMedicine('${m.id}', '${m.name}', '${m.unit}', '${m.price}', '${m.stockQuantity}', '${m.description}')">
                                        <i class="fa fa-pen-nib"></i>
                                    </button>
                                    <button class="btn-action delete text-danger" title="Xóa" onclick="deleteMedicine(${m.id})">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty medicineList}">
                        <tr>
                            <td colspan="7" class="text-center py-5">
                                <i class="fa fa-pills text-muted opacity-25 mb-3" style="font-size: 3rem;"></i>
                                <p class="text-muted fw-bold">Không tìm thấy loại thuốc nào trong kho!</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="addMedicineModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="AdminController" method="POST">
                <input type="hidden" name="action" value="addMedicine">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold"><i class="fa fa-plus-circle me-2 text-primary"></i>Nhập thuốc mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Tên thuốc <span class="text-danger">*</span></label>
                            <input type="text" name="name" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Đơn vị (Viên/Chai...)</label>
                            <input type="text" name="unit" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Giá bán (VNĐ)</label>
                            <input type="number" name="price" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Số lượng nhập</label>
                            <input type="number" name="stockQuantity" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold">Mô tả công dụng</label>
                            <textarea name="description" class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal" style="border-radius: 12px;">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4" style="border-radius: 12px;">Xác nhận nhập</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editMedicineModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="AdminController" method="POST">
                <input type="hidden" name="action" value="updateMedicine">
                <input type="hidden" name="id" id="edit_id">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold"><i class="fa fa-edit me-2 text-primary"></i>Cập nhật thông tin thuốc</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Tên thuốc</label>
                            <input type="text" name="name" id="edit_name" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Đơn vị</label>
                            <input type="text" name="unit" id="edit_unit" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Giá bán (VNĐ)</label>
                            <input type="number" name="price" id="edit_price" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Số lượng tồn kho</label>
                            <input type="number" name="stockQuantity" id="edit_stock" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold">Mô tả / Công dụng</label>
                            <textarea name="description" id="edit_desc" class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal" style="border-radius: 12px;">Đóng</button>
                    <button type="submit" class="btn btn-primary px-4" style="border-radius: 12px;">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function openEditMedicine(id, name, unit, price, stock, desc) {
        document.getElementById('edit_id').value = id;
        document.getElementById('edit_name').value = name;
        document.getElementById('edit_unit').value = unit;
        document.getElementById('edit_price').value = price;
        document.getElementById('edit_stock').value = stock;
        document.getElementById('edit_desc').value = desc;
    }

    function deleteMedicine(id) {
        if(confirm('Bạn có chắc chắn muốn xóa thuốc này vào thùng rác?')) {
            window.location.href = 'AdminController?action=deleteMedicine&id=' + id;
        }
    }
</script>
</body>
</html>