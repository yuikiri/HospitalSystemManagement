<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="content-section">
    <h4 class="fw-bold mb-4 text-primary"><i class="bi bi-journal-medical me-2"></i> Bệnh Nhân Đã Khám</h4>
    
    <div class="profile-card p-4 shadow-sm border">
        <c:if test="${empty historyList}">
            <div class="text-center p-5 text-muted">
                <i class="bi bi-folder-x fs-1 mb-3 text-secondary"></i>
                <h5 class="fw-bold">Chưa có dữ liệu</h5>
                <p>Bác sĩ chưa hoàn thành ca khám nào.</p>
            </div>
        </c:if>

        <c:if test="${not empty historyList}">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light text-muted">
                        <tr>
                            <th class="fw-semibold">Thời gian khám</th>
                            <th class="fw-semibold">Tên bệnh nhân</th>
                            <th class="fw-semibold">Phòng</th>
                            <th class="fw-semibold">Chẩn đoán (Bệnh án)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${historyList}" var="app">
                            <tr>
                                <td>
                                    <span class="fw-bold text-dark d-block"><fmt:formatDate value="${app.startTime}" pattern="dd/MM/yyyy"/></span>
                                    <small class="text-muted"><fmt:formatDate value="${app.startTime}" pattern="HH:mm"/></small>
                                </td>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2 shadow-sm fw-bold" style="width: 35px; height: 35px; font-size: 0.9rem;">
                                            ${app.patientName.substring(0, 1).toUpperCase()}
                                        </div>
                                        <span class="fw-bold text-primary">${app.patientName}</span>
                                    </div>
                                </td>
                                <td><span class="badge bg-light text-dark border px-2 py-1"><i class="bi bi-door-closed me-1 text-muted"></i> P.${app.roomNumber}</span></td>
                                <td>
                                    <span class="text-truncate d-inline-block text-secondary fw-medium bg-light px-2 py-1 rounded border" style="max-width: 300px;" title="${app.diagnosis}">
                                        ${app.diagnosis}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>