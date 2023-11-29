package vn.edu.iuh.fit.repositories;

import vn.edu.iuh.fit.connect.ConnectDB;
import vn.edu.iuh.fit.models.Account;
import vn.edu.iuh.fit.models.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    public void insert(Role role) throws Exception, SQLException {
        Connection con= ConnectDB.getInstance().getConnection();
        String sql="insert into role values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, role.getRoleId());
        ps.setString(2, role.getRoleName());
        ps.setString(3, role.getDescription());
        ps.setInt(4, role.getStatus());
        ps.executeUpdate();
    }

    public Role getRoleOfAccount(Account account) throws SQLException, ClassNotFoundException {
        Role role = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM role WHERE role_id IN (SELECT role_id FROM grant_access WHERE account_id = ?)")) {
            ps.setString(1, account.getAccountId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setRoleId(rs.getString("role_id"));
                    role.setRoleName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));
                    role.setStatus(rs.getInt("status"));
                }
            }
        }

        return role;
    }

    public Role getRoleByName(String role) throws SQLException, ClassNotFoundException {
        Role role1 = null;

        Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }
        try(
             PreparedStatement ps = con.prepareStatement("SELECT * FROM role WHERE role_name = ?")) {
            ps.setString(1, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role1 = new Role();
                    role1.setRoleId(rs.getString("role_id"));
                    role1.setRoleName(rs.getString("role_name"));
                    role1.setDescription(rs.getString("description"));
                    role1.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role1;
    }

    public Role getRoleById(String roleId) throws SQLException, ClassNotFoundException {
        Role role = null;

        Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }
        try (     PreparedStatement ps = con.prepareStatement("SELECT * FROM role WHERE role_id = ?")) {
            ps.setString(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setRoleId(rs.getString("role_id"));
                    role.setRoleName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));
                    role.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    public List<Role> findAll() {
        List<Role> roles =new ArrayList<>();

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM role")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role role = new Role();
                    role.setRoleId(rs.getString("role_id"));
                    role.setRoleName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));
                    role.setStatus(rs.getInt("status"));

                    roles.add(role);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
