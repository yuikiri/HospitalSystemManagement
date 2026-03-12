# Bước 1: Sử dụng hình ảnh Tomcat 9 chính thức làm nền
FROM tomcat:9.0-jdk11-openjdk

# Bước 2: Xóa các app mặc định của Tomcat để nhẹ máy
RUN rm -rf /usr/local/tomcat/webapps/*

# Bước 3: Copy file .war của sếp vào thư mục webapps của Tomcat
# Lưu ý: Thay 'ROOT.war' bằng tên file war của sếp hoặc để ROOT.war để nó chạy ở trang chủ
COPY dist/*.war /usr/local/tomcat/webapps/HospitalManagement.war

# Bước 4: Mở cổng 8080
EXPOSE 8080

# Bước 5: Chạy Tomcat
CMD ["catalina.sh", "run"]