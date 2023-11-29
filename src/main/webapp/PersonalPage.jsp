<%@ page import="vn.edu.iuh.fit.models.Account" %><%--
  Created by IntelliJ IDEA.
  User: thule
  Date: 11/27/2023
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thông tin cá nhân</title>
</head>
<body>
<%--    hiển thị thông tin cá nhân của user hiện tại đã đăng nhập--%>
    <div>
        <% Account a=(Account) request.getAttribute("account"); %>
        <p>Họ tên: <%=a.getFullName()%></p>
        <p>Số điện thoại: <%=a.getPhone()%></p>
        <p>Email: <%=a.getEmail()%></p>
        <p>Status: <%=a.getStatus()%></p>
    </div>
<a href="controller?action=logout">Đăng xuất</a>
</body>
</html>
