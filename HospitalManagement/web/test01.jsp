<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Test Dữ Liệu</title>
        <style>
            table { width: 80%; margin: 20px auto; border-collapse: collapse; font-family: sans-serif; }
            th, td { border: 1px solid black; padding: 10px; text-align: center; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body style="text-align: center;">
        
        <h1 style="color: red;">KẾT QUẢ TEST MÓC DỮ LIỆU THUỐC</h1>
        
        <h2>Số lượng thuốc tìm thấy: <span style="color: blue;">${testList != null ? testList.size() : "TRỐNG TRƠN (NULL)"}</span></h2>
        
        <a href="index.jsp" style="display: inline-block; padding: 10px 20px; background: black; color: white; text-decoration: none;">Quay lại Trang Chủ</a>

        <c:if test="${not empty testList}">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Tên Thuốc</th>
                    <th>Đơn vị</th>
                    <th>Giá</th>
                    <th>Tồn kho</th>
                </tr>
                <c:forEach items="${testList}" var="med">
                    <tr>
                        <td>${med.id}</td>
                        <td><strong>${med.name}</strong></td>
                        <td>${med.unit}</td>
                        <td>${med.price}</td>
                        <td>${med.stockQuantity}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

    </body>
</html>