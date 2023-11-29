<%@ page import="vn.edu.iuh.fit.models.Account" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.edu.iuh.fit.repositories.RoleRepository" %>
<%@ page import="vn.edu.iuh.fit.repositories.AccountRepository" %>
<%@ page import="vn.edu.iuh.fit.repositories.GrantAccessRepository" %><%--
  Created by IntelliJ IDEA.
  User: thule
  Date: 9/17/2023
  Time: 8:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Assuming you have an instance of Account, you can set it in the request attribute
    Account account = (Account) request.getAttribute("account");
    GrantAccessRepository grantAccessRepository = new GrantAccessRepository();
    List<Account> accountsAdmin = grantAccessRepository.getAccountsByRole("ADMIN");
    List<Account> accountsUser = grantAccessRepository.getAccountsByRole("USER");
%>


<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap/dist/css/bootstrap.min.css">
    <style>
        /* Tùy chỉnh CSS bổ sung */
        body {
            background-color: #f8f9fa;
        }

        /* Sidebar */
        .sidebar {
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            width: 250px;
            background-color: #f8f9fa;
            padding-top: 20px;
        }

        .sidebar a {
            padding: 10px 15px;
            text-decoration: none;
            font-size: 13px;
            color: #000;
            display: block;
        }

        .sidebar a:hover {
            background-color: #007bff;
            color: #fff;
        }


        /* Main content */
        #main-content {
            background-color: #ffffff;
            padding: 20px;
            margin-top: 20px;
            margin-left: 250px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            max-width: 170vh;
        }


        /* Form styling */
        form {
            max-width: 400px;
            margin: 0 auto;
        }

        /* Button styling */
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

        .btn-danger {
            background-color: #dc3545;
            border-color: #dc3545;
        }

        .btn-danger:hover {
            background-color: #c82333;
            border-color: #c82333;
        }
    </style>
</head>
<body>

<div class="container-fluid">

    <div class="row">
        <!-- Sidebar -->
        <nav class="sidebar" >
            <ul class="nav flex-column">

                <li class="nav-item">
                    <a class="nav-link active-link" href="#" onclick="showAccountInfo()">
                        Thông tin tài khoản
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="showRoleAccounts()">
                        Hiển thị danh sách account theo role
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="grantPermissions()">
                        Cấp quyền cho một account
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="showLoginLogoutLog()">
                        Ghi log mỗi lần account đăng nhập, đăng xuất
                    </a>
                </li>
                <li class="nav-item">
                    <a href="controller?action=logout">Đăng xuất</a>
                </li>
            </ul>
        </nav>

        <!-- Main content -->
        <main class="main-content" id="main-content">
            <!-- Default content -->
            <h2>Chọn chức năng từ sidebar để hiển thị nội dung tương ứng ở đây</h2>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap/dist/js/bootstrap.min.js"></script>

<script>

    function showAccountInfo() {

        // Tạo form để hiển thị thông tin tài khoản
        var accountInfoForm = `
        <h2>Thông tin tài khoản</h2>
        <form action="controller" method="post" class="main-content">
            <div class="mb-3">

                <label for="fullname" class="form-label">Họ và tên</label>
                <input type="text" class="form-control" id="fullname" name="fullname" value=${account.getFullName()}>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Mật khẩu</label>
                <input type="text" class="form-control" id="password" name="password" value=${account.getPassword()}>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${account.getEmail()}">
            </div>
            <div class="mb-3">
                <label for="phone" class="form-label">Số điện thoại</label>
                <input type="tel" class="form-control" id="phone" name="phone" value="${account.getPhone()}">
            </div>
            <div class="mb-3">
                <label for="status" class="form-label">Trạng thái</label>
                <select class="form-select" id="status" name="status">
                    <option value="1" ${account.getStatus() == 1 ? 'selected' : ''}>Active</option>
                    <option value="0" ${account.getStatus() == 0 ? 'selected' : ''}>Inactive</option>
                </select>

            </div>
            <button type="submit" class="btn btn-primary" name="action" value="updateAccount">Cập nhật thông tin</button>
            <button type="submit" class="btn btn-danger" name="action" value="lockAccount">Khóa tài khoản</button>
        </form>
    `;

        document.getElementById("main-content").innerHTML = accountInfoForm;
    }

    function showRoleAccounts() {
        var accountPermissionsForm = `
        <div id="accountTable" class="main-content">
            <h2>Những account thuộc role ADMIN</h2>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Account ID</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Account acc : accountsAdmin) { %>
                            <tr>
                                <td><%= acc.getAccountId() %></td>
                                <td><%= acc.getFullName() %></td>
                                <td><%= acc.getEmail() %></td>
                                <td><%= acc.getPhone() %></td>
                                <td><%= acc.getStatus() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <h2>Những account thuộc role USER</h2>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Account ID</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Account acc : accountsUser) { %>
                            <tr>
                                <td><%= acc.getAccountId() %></td>
                                <td><%= acc.getFullName() %></td>
                                <td><%= acc.getEmail() %></td>
                                <td><%= acc.getPhone() %></td>
                                <td><%= acc.getStatus() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    `;

        // Load and display account permissions content here
        document.getElementById("main-content").innerHTML = accountPermissionsForm;
    }



    function grantPermissions() {
        fetch('Permissions.jsp')
            .then(response => response.text())
            .then(data => {
                // Update the main-content with the content of the fetched JSP file
                document.getElementById("main-content").innerHTML = data;
            })
            .catch(error => console.error('Error:', error));
    }

    function showLoginLogoutLog() {
        // Load and display login/logout log content here
        document.getElementById("main-content").innerHTML = "<h2>Ghi log mỗi lần account đăng nhập, đăng xuất</h2>";
    }
    showAccountInfo();

</script>
</body>
</html>
