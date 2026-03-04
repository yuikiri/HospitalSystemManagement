<%-- 
    Document   : indexPatient
    Created on : Mar 4, 2026, 2:37:42 PM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="./style/hearder.css"/>
        <link rel="stylesheet" href="./style/sideBar.css"/>
    </head>
    <body>
        <jsp:include page="hearder.jsp" />
    <div class="main-container">
        <jsp:include page="sideBar.jsp" />
        <main class="content">
            <h1>dashboard content</h1>
        </main>
    </div>
    <script>
        lucide.createIcons();
    </script>
    </body>
</html>
