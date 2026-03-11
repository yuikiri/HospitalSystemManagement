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
