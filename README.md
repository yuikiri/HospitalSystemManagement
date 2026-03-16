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
