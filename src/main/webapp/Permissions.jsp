<%@ page import="vn.edu.iuh.fit.models.Role" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.edu.iuh.fit.models.Account" %>
<%@ page import="vn.edu.iuh.fit.repositories.RoleRepository" %>
<%@ page import="vn.edu.iuh.fit.repositories.AccountRepository" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body class="bg-gray-100 p-4">
<div class="max-w-md mx-auto bg-white p-4 rounded shadow-lg">
  <h1 class="text-2xl font-semibold mb-4">Phân Quyền</h1>
  <form action="controller" method="post" >
    <div class="mb-4">
      <label for="role" class="block font-medium">Chọn Vai Trò:</label>
      <select name="role" id="role" class="w-full p-2 border rounded">
        <%
          RoleRepository roleRepository = new RoleRepository();
          List<Role> roleList = (List<Role>) roleRepository.findAll();
          for (Role role : roleList) {
        %>
        <option value="<%= role.getRoleId() %>"><%= role.getRoleName() %></option>
        <%
          }
        %>
      </select>
    </div>

    <div class="mb-4">
      <label for="accountID" class="block font-medium">Chọn Tài Khoản:</label>
      <select name="accountID" id="accountID" class="w-full p-5 border rounded">
        <%
          AccountRepository accountRepository = new AccountRepository();
          List<Account> accountList = (List<Account>) accountRepository.findAll();
          for (Account account : accountList) {

        %>

        <option value="<%= account.getAccountId() %>"> <%= account.getAccountId() %> - <%= account.getFullName() %></option>

        <% } %>
      </select>
    </div>
    <div class="mb-4">
      <label for="status" class="block font-medium">Chọn Trạng Thái:</label>
      <select name="status" id="status" class="w-full p-2 border rounded">
        <option value="1">ENABLE</option>
        <option value="0">DISABLE</option>
      </select>
    </div>
    <div class="mb-4">
      <label for="note" class="block font-medium">Ghi chú:</label>
      <input type="text" name="note" id="note" class="w-full p-2 border rounded">
    </div>
    <div class="text-center">
      <button type="submit" class="ui-button" value="">Cấp Quyền</button>
      <input type="hidden" name="action" value="permission">
    </div>
  </form>
</div>
</body>