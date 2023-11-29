package vn.edu.iuh.fit.repositories;

import vn.edu.iuh.fit.connect.ConnectDB;
import vn.edu.iuh.fit.models.Account;
import vn.edu.iuh.fit.models.GrantAccess;
import vn.edu.iuh.fit.models.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrantAccessRepository {

    public void insert(GrantAccess grantAccess) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            con = ConnectDB.getInstance().reconnect();
        }
        String sql = "insert into grant_access values(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grantAccess.getAccount().getAccountId());

            // Kiểm tra xem Role có tồn tại không trước khi thêm vào grant_access
            Role role = grantAccess.getRole();
            if (role != null && role.getRoleId() != null) {
                ps.setString(2, role.getRoleId());
            } else {
                // Xử lý khi Role là null hoặc không có RoleId
                // Có thể là thông báo lỗi hoặc xử lý khác tùy thuộc vào yêu cầu của bạn
                throw new RuntimeException("Role is null or RoleId is null");
            }

            ps.setString(3, grantAccess.getIsGrant());
            ps.setString(4, grantAccess.getNote());
            ps.executeUpdate();
        }
    }

    public boolean grantPermission(String accountId, String roleIds, String grantType, String note) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement ps = null;
        //get list roles frome roleIds string ","
        String[] arrRoleIds = roleIds.split(",");
        try {
            String sql = "INSERT INTO grant_access VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            for (String roleId : arrRoleIds) {
                ps.setString(1, accountId);
                ps.setString(2, roleId);
                ps.setString(3, grantType);
                ps.setString(4, note);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Account> getAccountsByRole(String role) throws SQLException, ClassNotFoundException {
        List<Account> accounts = new ArrayList<>(); // Initialize the list
        Role roleObject = new RoleRepository().getRoleByName(role);
        Connection con = ConnectDB.getInstance().getConnection();

        if(con == null) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM account WHERE account_id IN (SELECT account_id FROM grant_access WHERE role_id = ?)")) {
            ps.setString(1, roleObject.getRoleId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getString("account_id"));
                    account.setFullName(rs.getString("full_name"));
                    account.setEmail(rs.getString("email"));
                    account.setPassword(rs.getString("password"));
                    account.setPhone(rs.getString("phone"));
                    account.setStatus(rs.getInt("status"));

                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }



    public void delete(String accountId, String roleId) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM grant_access WHERE account_id = ? AND role_id = ?")) {
            ps.setString(1, accountId);
            ps.setString(2, roleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String accountId, String roleId) {
        boolean exists = false;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM grant_access WHERE account_id = ? AND role_id = ?")) {
            ps.setString(1, accountId);
            ps.setString(2, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return exists;
    }
}
