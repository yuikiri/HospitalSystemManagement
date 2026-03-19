<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="content-section">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold text-primary"><i class="bi bi-calendar-week me-2"></i> Lịch Phân Công Ca Trực</h4>
        <button class="btn btn-outline-primary rounded-pill px-4 shadow-sm"><i class="bi bi-printer me-1"></i> In lịch trực</button>
    </div>

    <div class="row g-4">
        <c:if test="${empty shiftList}">
            <div class="col-12 text-center p-5 text-muted bg-white rounded-4 shadow-sm border">
                <i class="bi bi-calendar-x fs-1 mb-3 text-secondary"></i>
                <h5 class="fw-bold">Chưa có lịch trực</h5>
                <p>Bác sĩ chưa được phân công ca trực nào trong tuần này.</p>
            </div>
        </c:if>

        <c:forEach items="${shiftList}" var="shift">
            <div class="col-md-6 col-xl-4">
                <div class="profile-card p-4 shadow-sm h-100 border-start border-5 border-primary">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <span class="badge bg-primary bg-opacity-10 text-primary rounded-pill px-3 py-2 fw-bold border border-primary border-opacity-25">
                            ${shift.role}
                        </span>
                        <span class="badge bg-light text-dark border px-2 py-1 shadow-sm">
                            <i class="bi bi-door-open text-primary"></i> P.${shift.roomNumber}
                        </span>
                    </div>
                    
                    <h5 class="fw-bold text-dark mb-2">
                        <i class="bi bi-calendar2-check text-muted me-2"></i>
                        <fmt:formatDate value="${shift.startTime}" pattern="dd/MM/yyyy"/>
                    </h5>
                    <p class="text-muted fw-medium mb-0 ms-4 ps-1 bg-light d-inline-block px-2 py-1 rounded border">
                        <i class="bi bi-clock text-warning me-1"></i> 
                        <fmt:formatDate value="${shift.startTime}" pattern="HH:mm"/> - <fmt:formatDate value="${shift.endTime}" pattern="HH:mm"/>
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>