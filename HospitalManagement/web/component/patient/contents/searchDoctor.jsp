<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="dao.DoctorDAO"%>
<%@page import="dao.DoctorDTO"%>
<%@page import="dao.DepartmentDAO"%>
<%@page import="dao.DepartmentDTO"%>
<%@page import="dao.RoomDAO"%>
<%@page import="dao.RoomDTO"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // 1. TẢI SẴN TOÀN BỘ DỮ LIỆU TỪ DATABASE LÊN
    DoctorDAO docDao = new DoctorDAO();
    // Chuyền chuỗi rỗng để lấy TẤT CẢ bác sĩ lên, Javascript sẽ lo phần lọc
    List<DoctorDTO> doctorList = docDao.searchDoctors(""); 

    DepartmentDAO deptDao = new DepartmentDAO();
    List<DepartmentDTO> deptList = deptDao.getClinicalDepartments(); 

    RoomDAO roomDao = new RoomDAO();
    List<RoomDTO> rawRooms = roomDao.getAllRoomsForAdmin();
    Map<String, RoomDTO> uniquePrices = new HashMap<>();
    
    if (rawRooms != null) {
        for (RoomDTO r : rawRooms) {
            if (r.getPrice() > 0 && r.getIsActive() == 1 && !uniquePrices.containsKey(r.getRoomTypeName())) {
                uniquePrices.put(r.getRoomTypeName(), r);
            }
        }
    }

    DecimalFormat df = new DecimalFormat("#,###");
%>

<style>
    .search-col { height: calc(100vh - 180px); overflow-y: auto; padding-right: 15px; }
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
            
            <div class="input-group mb-3 shadow-sm rounded-pill">
                <input type="text" id="searchInput" onkeyup="filterDoctors()" class="form-control border-end-0 rounded-start-pill bg-light ps-3" placeholder="Nhập từ khóa...">
                
                <select id="searchType" class="form-select bg-light text-primary border-start-0 rounded-end-pill fw-bold" style="max-width: 140px; cursor: pointer;" onchange="filterDoctors()">
                    <option value="ten">Lọc theo Tên</option>
                    <option value="ho">Lọc theo Họ</option>
                </select>
            </div>

            <div class="search-col" id="doctorListContainer">
                <% 
                    if (doctorList != null && !doctorList.isEmpty()) {
                        for (DoctorDTO doc : doctorList) {
                            String genderIcon = (doc.getGender() == 1) 
                                ? "<i class='fas fa-mars text-primary'></i> Nam" 
                                : "<i class='fas fa-venus text-danger'></i> Nữ";
                            String avatarName = URLEncoder.encode(doc.getName(), "UTF-8");
                %>
                <div class="doc-card d-flex align-items-center">
                    <img src="https://ui-avatars.com/api/?name=<%= avatarName %>&background=random" class="rounded-circle me-3" width="50" height="50">
                    <div>
                        <h6 class="mb-0 fw-bold doc-name"><%= doc.getName() %></h6>
                        <small class="text-muted"><%= genderIcon %> | <%= doc.getPosition() %></small>
                    </div>
                </div>
                <% 
                        } 
                    } 
                %>
                
                <div id="noResultMsg" class="text-center text-muted mt-4 p-3 bg-light rounded-3 d-none">
                    <i class="fas fa-user-slash fs-3 mb-2"></i><p class="mb-0">Không tìm thấy bác sĩ.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-4 col-md-12 mb-4">
        <div class="bg-white p-3 rounded-4 shadow-sm h-100">
            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3"><i class="fas fa-hospital text-success me-2"></i> Chuyên Khoa</h6>
            <div class="search-col">
                <% 
                    if (deptList != null && !deptList.isEmpty()) {
                        for (DepartmentDTO dept : deptList) {
                %>
                <div class="dept-card">
                    <h6 class="fw-bold text-primary mb-1"><%= dept.getName() %></h6>
                    <p class="text-muted small mb-0">
                        <%= (dept.getDescription() != null && !dept.getDescription().isEmpty()) ? dept.getDescription() : "Đang cập nhật thông tin." %>
                    </p>
                </div>
                <% 
                        } 
                    } 
                %>
            </div>
        </div>
    </div>

    <div class="col-lg-4 col-md-12 mb-4">
        <div class="bg-white p-3 rounded-4 shadow-sm h-100">
            <h6 class="fw-bold text-dark border-bottom pb-2 mb-3"><i class="fas fa-bed text-warning me-2"></i> Bảng giá Phòng & Dịch vụ</h6>
            <div class="search-col">
                <% 
                    if (!uniquePrices.isEmpty()) {
                        for (RoomDTO room : uniquePrices.values()) {
                %>
                <div class="room-card">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="fw-bold mb-0"><%= room.getRoomTypeName() %></h6>
                        <span class="badge bg-success">Đang hoạt động</span>
                    </div>
                    <h5 class="text-danger fw-bold mb-0">
                        <%= df.format(room.getPrice()) %> đ 
                        <small class="text-muted fs-6">/lần</small>
                    </h5>
                </div>
                <% 
                        } 
                    } else {
                %>
                    <div class="text-center text-muted mt-4"><p>Hệ thống đang cập nhật bảng giá.</p></div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script>
    function filterDoctors() {
        let input = document.getElementById("searchInput").value.toLowerCase().trim();
        let searchType = document.getElementById("searchType").value; // Lấy giá trị 'ten' hoặc 'ho'
        
        let cards = document.querySelectorAll(".doc-card");
        let hasResult = false;

        cards.forEach(card => {
            // Chỉ lấy duy nhất dòng tên bác sĩ (Bỏ qua tên khoa)
            let fullName = card.querySelector(".doc-name").innerText.toLowerCase().trim();
            
            // Cắt họ tên thành các chữ (Ví dụ: "Nguyễn", "Bá", "Tĩnh")
            let words = fullName.split(" ");
            let ho = words[0]; // Họ luôn là chữ đầu tiên
            let ten = words[words.length - 1]; // Tên luôn là chữ cuối cùng
            
            let isMatch = false;

            if (input === "") {
                isMatch = true; // Nếu chưa gõ gì thì hiện hết
            } else {
                if (searchType === "ho") {
                    // Nếu chọn Lọc theo Họ: So sánh ô tìm kiếm với biến 'ho'
                    isMatch = ho.includes(input);
                } else if (searchType === "ten") {
                    // Nếu chọn Lọc theo Tên: So sánh ô tìm kiếm với biến 'ten'
                    isMatch = ten.includes(input);
                }
            }

            // Hiển thị hoặc Giấu thẻ
            if (isMatch) {
                card.style.setProperty("display", "flex", "important");
                hasResult = true;
            } else {
                card.style.setProperty("display", "none", "important");
            }
        });

        // Hiển thị thông báo khi không có ai
        let noResultMsg = document.getElementById("noResultMsg");
        if (hasResult) {
            noResultMsg.classList.add("d-none");
        } else {
            noResultMsg.classList.remove("d-none");
        }
    }
</script>