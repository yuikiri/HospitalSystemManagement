# Sử dụng đúng Tomcat 9 và Java 8 khớp 100% với máy sếp
FROM tomcat:9.0-jdk8

# Bước 2: Xóa các app mặc định của Tomcat để nhẹ máy
RUN rm -rf /usr/local/tomcat/webapps/*

# Bước 3: Copy file .war của sếp vào thư mục webapps của Tomcat
# Lưu ý: Thay 'ROOT.war' bằng tên file war của sếp hoặc để ROOT.war để nó chạy ở trang chủ
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war

# Bước 4: Mở cổng 8080
EXPOSE 8080

# Bước 5: Chạy Tomcat
CMD ["catalina.sh", "run"]