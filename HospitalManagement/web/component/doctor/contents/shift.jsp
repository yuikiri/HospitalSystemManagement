<%-- 
    Document   : shift
    Created on : Mar 15, 2026, 8:42:29 PM
    Author     : Dang Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="content-section">
    <div class="profile-card p-4">
        <h5 class="text-primary fw-bold mb-4">Lịch trực của tôi</h5>
        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Thời gian</th>
                        <th>Phòng</th>
                        <th>Khoa</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>08:00 - 12:00</td>
                        <td>Phòng 102</td>
                        <td>Nội tổng quát</td>
                        <td><button class="btn btn-sm btn-primary px-3 rounded-pill" onclick="startExam()">Vào khám</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
