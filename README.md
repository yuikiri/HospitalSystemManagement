Requirements

- JDK 8
- NetBeans 13
- Tomcat 9
- SQL Server
- Add lib
  sqljdbc42.jar

# PrescriptionDetailDTO là gì?

Nó sinh ra để phục vụ cho Giao diện (Màn hình Web / Máy in).

Ví dụ thực tế: Khi bệnh nhân ra quầy lấy thuốc, họ cần 1 tờ giấy in ra trọn vẹn cả thông tin của họ lẫn danh sách các loại thuốc cần uống.

Tác dụng của code: Thay vì Server phải ném cho giao diện 2-3 biến rời rạc bắt giao diện tự ghép lại, thì Server chỉ cần ném đúng 1 cục PrescriptionDetailDTO này ra. Giao diện (HTML/JSP) cứ thế mà lôi dữ liệu ra in thành 1 cái hóa đơn/đơn thuốc hoàn chỉnh cực kỳ nhàn hạ.

Chốt câu: DB thì lưu rời rạc thành nhiều bảng cho nhẹ, nhưng khi đưa lên màn hình cho con người xem thì phải "gom" lại thành 1 cục. Cái DetailDTO chính là "cục" đó!

# doctor

- thông tin cá nhân
- xem ca trực(dạng thời khóa biểu, có thể chọn ngày, tháng, năm để sem lịch hôm đó hơm sau và quá khứ)
- lấy thông tin ca trực (ví dụ 2-5h,..) thì sẽ tự động hiện ra các cuộc hẹn có thời gian tương ứng và khoa tương ứng với khoa của bác sĩ đó, ấn vào sẽ hiển thị ra thông tin bệnh nhân, thời gian, phòng khám. có phần bác sĩ ghi đc đó là các chuẩn đón, thuốc,...., đặc biêt có thời gian để tính tiền phòng(ví dụ x2 phòng... thì tiền phòng đó 1 ngày sẽ x2), có phần thêm thuốc, sẽ hiện ra bảng các loại thuốc, (tìm kiếm tên thuốc đó, tick chọn vào và +- đc số lượng), sau đó mới có thể ấn hoàn thành, nếu chưa thì chỉ có thể cancle.
- lịch sử khám bệnh sẽ có các hồ sơ mà bác sĩ đó đã duyệt đc, bác sĩ nào ấn hoàn thành ở trên sẽ lưu id vào cuộc hẹn đã nhắc để null bên zalo, lịch sử theo bác sĩ đã ấn hoàn thành cuộc hẹn đó. trong lịch sử có bản tóm tắt đầy đủ ko thể chỉnh sửa đc, và có giá tiền, bao gồm tiền phòng, tiền thuốc, total, và có chữ trạng thái thanh toán.

## mô tả staff

### có 3 chức năng:

- xem thông tin cá nhân, có thể tự chỉnh sửa(coi kĩ thông tin nào đc chỉnh),
- xem ca trực,
- lịch sử ca trực trong vòng 6 tháng gần đây khi ấn vào nút "chọn tuần và tháng và năm" (như xem thời khóa biểu của trường)

## admind:

- có các tab quản lý role: patient, doctor, staff;
  - trong từng role đó có: tìm kiếm theo email(filter), có nút lọc isActive(0,1), thêm tài khoản, xóa tài khoản(xóa mềm, chỉnh isActive), chỉnh sửa role, và các thông tin mềm và cứng(ấn vào hiện ra bảng điền thông tin, chỉ cần nhập những thông tin notnull bên db là đủ r)
- các tab quản lý phần cứng: department, appointment, room, shift, medicine
  - department: sửa khoa, thêm khoa mới, ẩn khoa, tìm kiếm theo tên khoa(filter), cái này bao gồm khoa của bác sĩ và nhân viên. trước hết phải hiển thị toàn bộ phòng, có thể dùng lazy loading để xử lý quá tải dữ liệu.(nó hiển thị tên phòng, loại phòng, giá,....)
  - appointment: sửa lịch hẹn, thêm lịch hẹn, ẩn lịch hẹn, tìm kiếm theo ngày(filter), tìm kiếm theo bác sĩ(filter), tìm kiếm theo phòng(filter), phải chọn ngày trước.
  - room: sửa phòng, thêm phòng mới, ẩn phòng, tìm kiếm theo tên phòng(filter). trước hết phải hiển thị toàn bộ phòng, có thể dùng lazy loading để xử lý quá tải dữ liệu.(nó hiển thị tên phòng, loại phòng, giá,....)
  - shift: (ca trực 1 - 8, mỗi ca trực 2 tiếng). trước hết phải hiển thị 2 nút để tìm kiếm theo email của bác sĩ và nhân viên(chia trên dưới) khi ấn vào thì sẽ chỉnh sửa ca trực của bác sĩ, nv đó, hiện ra ca trực hiện tại (tuần này) của bác sĩ và nhân viên(doctor ở trên 1 bảng thời khóa biểu 2-cn, 6h-22h, staff ở dưới 1 bảng thời khóa biểu 2-cn, 6h-22h), và có thể chọn từ hôm nay tới 4 tuần để thêm mới cho từng người.
  - Prescription: (lưu ý medicine là các loại thuốc, Prescription là đơn thuốc có các chuẩn đoán,.., PrescriptionItem là toa thuốc có nhưng thuốc j trong cái đơn đó): sem đc tổng hợp 3 cái, thông tin chuẩn đoán, thông có những thuốc j trong đơn đó, và thuốc đó bao nhiêu tiền, có tổng tiền trong đó. có thể thay đổi ẩn, và thêm thuốc mới vào đơn đó, nếu đổi hoặc thêm thì tổng tiền cũng cần đc cập nhập theo.
  - medicine: sửa thuốc, thêm thuốc mới, ẩn thuốc, tìm kiếm theo tên thuốc(filter). trước hết phải hiển thị toàn bộ thuốc, có thể dùng lazy loading để xử lý quá tải dữ liệu.(nó hiển thị tên thuốc, công dụng, giá,....)
- cac tab quản lý toàn cục:
  - tổng tiền của từng tháng trong năm, có thể chọn tháng và năm., khi ấn vào có tiền hóa đơn chi tiết của từng lịch khám trong lịch sử khám. trong lịch sử khám là có tiền khám bệnh và cả tiền thuốc().






đây là fonrt index trang thông tin và chức đăng nhập đăng kí, hãy tạo 1 trang thật dẹp dùng các entity frameword để tạo nhưng vẫn giữ đúng các trường dữ liệu để tránh sai sót. và có thanh header nhỏ có 2 tab là home là trang đang ở và about us hiện các thông tin.

index  khi login hãy nhả ra lỗi mà ko out cái tab đăng xuất ra:

<%@page import="entity.User"%>

<%@page import="config.HospitalConfig"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>



<%

    String loginErr = (String) request.getAttribute("message");

    String regErr = (String) request.getAttribute("regError");

    String regSuccess = (String) request.getAttribute("regSuccess");



    User currentUser = (User) session.getAttribute("user");



// Nếu là staff thì chuyển qua staff dashboard

    if (session.getAttribute("staff") != null) {

        response.sendRedirect("component/staff/staffDashboard.jsp");

        return;

    }



// Nếu là patient thì chuyển qua patient dashboard

    if (session.getAttribute("patient") != null) {

        response.sendRedirect("component/patient/patientDashboard.jsp");

        return;

    }

%>



<!DOCTYPE html>



<html lang="en">

    <head>

        <meta charset="UTF-8">

        <title><%= HospitalConfig.HOSPITAL_NAME%> | Home</title>



        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">



        <style>

            :root{

                --med-blue:#007bff;

                --med-dark:#343a40;

            }

            body{

                font-family:'Segoe UI',sans-serif;

            }



            .navbar-brand{

                font-weight:800;

                color:var(--med-blue);

            }



            .hero-section{

                background:linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)),

                    url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d');

                background-size:cover;

                background-position:center;

                color:white;

                padding:120px 0;

            }



            .service-card{

                transition:0.3s;

                border:none;

                border-radius:15px;

                box-shadow:0 4px 15px rgba(0,0,0,0.1);

            }



            .service-card:hover{

                transform:translateY(-10px);

            }



            .icon-box{

                font-size:3rem;

                color:var(--med-blue);

                margin-bottom:15px;

            }



            footer{

                background:var(--med-dark);

                color:#ccc;

                padding:40px 0;

            }

        </style>



    </head>



    <body>





        <nav class="navbar navbar-light bg-white py-3">

            <div class="container">



                <a class="navbar-brand fs-3" href="index.jsp">

                    <i class="fas fa-hand-holding-medical"></i>

                    <%= HospitalConfig.HOSPITAL_NAME%>

                </a>



                <div class="ms-auto d-flex gap-2">



                    <% if (currentUser == null) { %>



                    <button class="btn btn-outline-primary px-4"

                            data-bs-toggle="modal"

                            data-bs-target="#loginModal">

                        Login </button>



                    <button class="btn btn-primary px-4"

                            data-bs-toggle="modal"

                            data-bs-target="#registerModal">

                        Register </button>



                    <% } else {%>



                    <span class="navbar-text fw-bold text-primary me-3">

                        Xin chào <%= currentUser.getUserName()%>

                    </span>



                    <a href="LogoutController"

                       class="btn btn-outline-danger">

                        Logout </a>



                    <% } %>



                </div>

            </div>

            <div class="collapse navbar-collapse" id="mainNav">

                <ul class="navbar-nav mx-auto text-uppercase">

                    <li class="nav-item"><a class="nav-link active px-3" href="index.jsp">Home</a></li>

                    <li class="nav-item"><a class="nav-link px-3" href="#" data-bs-toggle="modal" data-bs-target="#aboutModal">About Us</a></li>

                </ul>

            </div>

        </nav>



        <section class="hero-section text-center">



            <div class="container">



                <h1 class="display-3 fw-bold">

                    Your Health, Our Mission

                </h1>



                <p class="lead mb-4 fs-4">

                    Advanced care for a better life

                </p>



                <% if (currentUser == null) { %>



                <button class="btn btn-primary btn-lg px-5"

                        data-bs-toggle="modal"

                        data-bs-target="#registerModal">

                    Join Us Now </button>



                <% }%>



            </div>



        </section>



        <section class="container my-5 py-5">



            <div class="row g-4 text-center">



                <div class="col-md-4">

                    <div class="card h-100 p-4 service-card">



                        <div class="icon-box">

                            <i class="fas fa-user-md"></i>

                        </div>



                        <h4>Expert Doctors</h4>

                        <p class="text-muted">

                            Access to world-class specialists

                        </p>



                    </div>

                </div>



                <div class="col-md-4">

                    <div class="card h-100 p-4 service-card">



                        <div class="icon-box">

                            <i class="fas fa-clock"></i>

                        </div>



                        <h4>24/7 Support</h4>

                        <p class="text-muted">

                            Emergency services anytime

                        </p>



                    </div>

                </div>



                <div class="col-md-4">

                    <div class="card h-100 p-4 service-card">



                        <div class="icon-box">

                            <i class="fas fa-notes-medical"></i>

                        </div>



                        <h4>Online Records</h4>

                        <p class="text-muted">

                            View medical history online

                        </p>



                    </div>

                </div>



            </div>

        </section>



        <footer>



            <div class="container text-center">



                <h5 class="text-white">

                    <%= HospitalConfig.HOSPITAL_NAME%>

                </h5>



                <p>Email: <%= HospitalConfig.EMAIL%></p>

                <p>Phone: <%= HospitalConfig.PHONE_NUMBER%></p>

                <p>Address: <%= HospitalConfig.ADDRESS%></p>



            </div>



        </footer>



        <div class="modal fade" id="loginModal">



            <div class="modal-dialog modal-dialog-centered">



                <div class="modal-content">



                    <div class="modal-header bg-primary text-white">

                        <h5 class="modal-title">Login</h5>

                        <button class="btn-close btn-close-white" data-bs-dismiss="modal"></button>

                    </div>



                    <div class="modal-body">



                        <form action="LoginController" method="post">



                            <% if (loginErr != null) {%>



                            <div class="alert alert-danger">

                                <%= loginErr%>

                            </div>

                            <% } %>



                            <div class="mb-3">

                                <label>Email</label>

                                <input type="email" name="txtEmail"

                                       class="form-control" required>

                            </div>



                            <div class="mb-3">

                                <label>Password</label>

                                <input type="password" name="txtPassword"

                                       class="form-control" required>

                            </div>



                            <button class="btn btn-primary w-100">

                                Login

                            </button>



                        </form>



                    </div>



                </div>

            </div>

        </div>



        <div class="modal fade" id="registerModal">



            <div class="modal-dialog modal-dialog-centered">



                <div class="modal-content">



                    <div class="modal-header bg-primary text-white">

                        <h5 class="modal-title">Register</h5>

                        <button class="btn-close btn-close-white" data-bs-dismiss="modal"></button>

                    </div>



                    <div class="modal-body">



                        <form action="RegisterController" method="post">



                            <% if (regErr != null) {%>



                            <div class="alert alert-danger">

                                <%= regErr%>

                            </div>

                            <% } %>

                            <% if (regSuccess != null) {%>

                            <div class="alert alert-success">

                                <%= regSuccess%>

                            </div>

                            <% } %>



                            <div class="mb-3">

                                <label>Name</label>

                                <input type="text" name="username"

                                       class="form-control" required>

                            </div>



                            <div class="mb-3">

                                <label>Email</label>

                                <input type="email" name="email"

                                       class="form-control" required>

                            </div>



                            <div class="mb-3">

                                <label>Password</label>

                                <input type="password" name="password"

                                       class="form-control" required>

                            </div>



                            <div class="mb-3">

                                <label>Phone</label>

                                <input type="text" name="phone"

                                       class="form-control" required>

                            </div>

                            <div class="mb-3">

                                <label>Address</label>

                                <input type="text" name="address"

                                       class="form-control" required>

                            </div>



                            <button class="btn btn-primary w-100">

                                Register

                            </button>



                        </form>



                    </div>



                </div>

            </div>

        </div>



        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>



            <% if (regSuccess != null || regErr != null) { %>



            var registerModal = new bootstrap.Modal(document.getElementById('registerModal'));

            registerModal.show();



            <% }%>



        </script>

    </body>

</html>

.và đây là infor để bỏ vào about us :

/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

 */

package config;



/**

 *

 * @author Yuikiri

 */

public class HospitalConfig {

    public static final String HOSPITAL_NAME = "MEDI-CARE HOSPITAL";

    public static final String PHONE_NUMBER = "+84 123 456 789";

    public static final String EMAIL = "contact@medicare.com";

    public static final String ADDRESS = "123 Healthcare Ave, Medical District, Ho Chi Minh City";

    public static final String WORKING_HOURS = "24/7 Emergency Services";

    public static final String DESCRIPTION = "Hệ thống y tế hàng đầu với đội ngũ chuyên gia tận tâm, mang đến dịch vụ chăm sóc sức khỏe tốt nhất cho bạn và gia đình.";

}