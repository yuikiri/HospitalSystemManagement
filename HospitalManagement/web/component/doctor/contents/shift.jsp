<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="content-section fade-in">
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
        <h4 class="fw-bold text-primary mb-0"><i class="bi bi-calendar-week me-2"></i> Lịch Phân Công Ca Trực</h4>
        <button class="btn btn-primary rounded-pill px-4 shadow-sm fw-semibold">
            <i class="bi bi-printer me-2"></i> In lịch trực
        </button>
    </div>

    <div class="row g-4 mt-2">
        <c:if test="${empty shiftList}">
            <div class="col-12">
                <div class="text-center p-5 text-muted bg-white rounded-4 shadow-sm border border-dashed">
                    <i class="bi bi-calendar-x fs-1 mb-3 text-secondary d-block"></i>
                    <h5 class="fw-bold text-dark">Chưa có lịch trực</h5>
                    <p class="mb-0">Bác sĩ chưa được phân công ca trực nào trong thời gian tới.</p>
                </div>
            </div>
        </c:if>

        <c:forEach items="${shiftList}" var="shift">
            <div class="col-md-6 col-xl-4">
                <div class="profile-card p-4 shadow-sm bg-white h-100 border-start border-5 border-primary rounded-4 transition-hover">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <span class="badge bg-primary bg-opacity-10 text-primary rounded-pill px-3 py-2 fw-bold border border-primary border-opacity-25">
                            <i class="bi bi-person-badge me-1"></i> ${shift.role != null ? shift.role : 'Trực chính'}
                        </span>
                        
                        <div class="text-end">
                            <span class="badge bg-light text-dark border px-2 py-1 shadow-sm d-block mb-1">
                                <i class="bi bi-door-open text-primary"></i> P.${shift.roomNumber}
                            </span>
                            <small class="text-muted fw-bold" style="font-size: 0.75rem;">${shift.departmentName}</small>
                        </div>
                    </div>
                    
                    <hr class="text-muted opacity-25">
                    
                    <h5 class="fw-bold text-dark mb-2 mt-3">
                        <i class="bi bi-calendar2-check text-success me-2"></i>
                        <fmt:formatDate value="${shift.startTime}" pattern="dd/MM/yyyy"/>
                    </h5>
                    <p class="text-primary fw-bold mb-0 ms-4 ps-1 bg-primary bg-opacity-10 d-inline-block px-3 py-2 rounded-3 border border-primary border-opacity-10">
                        <i class="bi bi-clock-history me-1"></i> 
                        <fmt:formatDate value="${shift.startTime}" pattern="HH:mm"/> - <fmt:formatDate value="${shift.endTime}" pattern="HH:mm"/>
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<style>
    .border-dashed { border-style: dashed !important; border-width: 2px !important; }
    .transition-hover { transition: all 0.3s ease; }
    .transition-hover:hover { transform: translateY(-5px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important; }
</style>