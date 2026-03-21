<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.AppointmentDAO"%>
<%@page import="dao.AppointmentDTO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    AppointmentDAO appDao = new AppointmentDAO();
%>

<div class="content-section fade-in">
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
        <h4 class="fw-bold text-primary mb-0"><i class="fas fa-history me-2"></i> Lịch Sử Khám Bệnh</h4>
    </div>
    
    <div class="bg-white p-4 rounded-4 shadow-sm border">
        <c:if test="${empty historyList}">
            <div class="text-center p-5 text-muted">
                <i class="fas fa-folder-open fs-1 mb-3 text-secondary" style="opacity: 0.5;"></i>
                <h5 class="fw-bold text-dark">Chưa có dữ liệu</h5>
                <p class="mb-0">Bác sĩ chưa hoàn thành ca khám nào.</p>
            </div>
        </c:if>

        <c:if test="${not empty historyList}">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light text-muted">
                        <tr>
                            <th class="fw-semibold pb-3">Ngày & Giờ khám</th>
                            <th class="fw-semibold pb-3">Tên bệnh nhân</th>
                            <th class="fw-semibold pb-3">Phòng khám</th>
                            <th class="fw-semibold pb-3">Chẩn đoán (Bệnh án)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="app" items="${historyList}">
                            <%
                                AppointmentDTO currentApp = (AppointmentDTO) pageContext.getAttribute("app");
                                String[] details = appDao.getMedicalRecordDetail(currentApp.getId());
                                String diagnosis = (details != null && details[0] != null) ? details[0] : "Chưa có chẩn đoán";
                                pageContext.setAttribute("diagnosis", diagnosis);
                            %>
                            <tr>
                                <td>
                                    <div class="d-flex flex-column">
                                        <span class="fw-bold text-dark">
                                            <fmt:formatDate value="${app.startTime}" pattern="dd/MM/yyyy"/>
                                        </span>
                                        <small class="text-muted mt-1">
                                            <i class="far fa-clock me-1"></i>
                                            <fmt:formatDate value="${app.startTime}" pattern="HH:mm"/>
                                        </small>
                                    </div>
                                </td>
                                
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3 shadow-sm fw-bold" style="width: 40px; height: 40px; font-size: 1rem;">
                                            ${app.patientName.substring(0, 1).toUpperCase()}
                                        </div>
                                        <span class="fw-bold text-primary">${app.patientName}</span>
                                    </div>
                                </td>
                                
                                <td>
                                    <span class="badge bg-light text-dark border px-3 py-2 rounded-pill">
                                        <i class="fas fa-door-open me-1 text-muted"></i> P.${app.roomNumber}
                                    </span>
                                </td>
                                
                                <td>
                                    <span class="text-truncate d-inline-block text-secondary fw-medium bg-light px-3 py-2 rounded-3 border" style="max-width: 350px;" title="${diagnosis}">
                                        ${diagnosis}
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