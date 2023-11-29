<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Đăng nhập</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap/dist/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f4f4f4;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100vh;
    }
    .login-container {
      max-width: 400px;
      background-color: #ffffff;
      padding: 20px;
      box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body>
<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-6 login-container">
      <h2 class="text-center">Đăng nhập</h2>
      <c:if test="${not empty message}">
        <div id="loginAlert" class="alert ${message == 'Register successfully. Please login to continue.' ? 'alert-success' : 'alert-danger'} d-none" role="alert">
            ${message}
        </div>
      </c:if>



      <form action="controller" method="post">
        <input type="hidden" name="action" value="login">
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="text" class="form-control" id="email" name="email" required value="thuleanh543@gmail.com">
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Mật khẩu</label>
          <input type="password" class="form-control" id="password" name="password" required value="123456">
        </div>
        <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
      </form>

      <p class="mt-3 text-center">Chưa có tài khoản? <a href="Register.jsp">Đăng ký</a></p>
    </div>
  </div>
</div>
<script>
  // JavaScript to toggle the display of the alert based on the message
  document.addEventListener("DOMContentLoaded", function () {
    var loginAlert = document.getElementById("loginAlert");
    if (loginAlert.innerHTML.trim() !== "") {
      loginAlert.classList.remove("d-none");
    }
  });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap/dist/js/bootstrap.min.js"></script>



</body>
</html>