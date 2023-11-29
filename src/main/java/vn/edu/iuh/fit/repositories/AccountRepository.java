package vn.edu.iuh.fit.repositories;



import vn.edu.iuh.fit.connect.ConnectDB;
import vn.edu.iuh.fit.models.Account;
import vn.edu.iuh.fit.models.Grant;
import vn.edu.iuh.fit.models.Role;
import vn.edu.iuh.fit.models.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {
    RoleRepository roleRepository = new RoleRepository();
    GrantAccessRepository grantRepository = new GrantAccessRepository();
    public void insert(Account account) throws Exception,SQLException{
        Connection con=ConnectDB.getInstance().getConnection();
        String sql="insert into account values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,account.getAccountId());
        ps.setString(2,account.getFullName());
        ps.setString(3,account.getPassword());
        ps.setString(4,account.getEmail());
        ps.setString(5,account.getPhone());
        ps.setInt(6, account.getStatus());
        ps.executeUpdate();
    }
    public Optional<Account> logon(String email, String password) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM account WHERE email=? AND password=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getString("account_id"));
                account.setFullName(rs.getString("full_name"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setPhone(rs.getString("phone"));
                account.setStatus(rs.getInt("status"));

                return Optional.of(account);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Account> getAll() throws SQLException, ClassNotFoundException {
        List<Account> list = new ArrayList<>();
        Connection con= ConnectDB.getInstance().getConnection();
        String sql="select * from account";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            Account account=new Account();
            account.setAccountId(rs.getString("account_id"));
            account.setFullName(rs.getString("full_name"));
            account.setEmail(rs.getString("email"));
            account.setPassword(rs.getString("password"));
            account.setPhone(rs.getString("phone"));
            account.setStatus(rs.getInt("status"));
            list.add(account);
        }
        return list;
    }



    public Account getAccountById(String accountId) throws SQLException, ClassNotFoundException {
        Account account = null;

       Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }
          try (    PreparedStatement ps = con.prepareStatement("SELECT * FROM account WHERE account_id = ?")) {
            ps.setString(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setAccountId(rs.getString("account_id"));
                    account.setFullName(rs.getString("full_name"));
                    account.setEmail(rs.getString("email"));
                    account.setPassword(rs.getString("password"));
                    account.setPhone(rs.getString("phone"));
                    account.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    public void update(Account updatedAccount) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.getInstance().getConnection();

        // Check if the connection is closed
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }
        String sql = "UPDATE account SET full_name = ?, password = ?, email = ?, phone = ?, status = ? WHERE account_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, updatedAccount.getFullName());
            ps.setString(2, updatedAccount.getPassword());
            ps.setString(3, updatedAccount.getEmail());
            ps.setString(4, updatedAccount.getPhone());
            ps.setInt(5, updatedAccount.getStatus());
            ps.setString(6, updatedAccount.getAccountId());
            ps.executeUpdate();
        }

    }

    public void lockAccount(String accountId) throws SQLException, ClassNotFoundException {
        Connection con = ConnectDB.getInstance().getConnection();

        // Check if the connection is closed
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
        }

        String sql = "UPDATE account SET status = ? WHERE account_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Status.UNACTIVE.getNumber());
            ps.setString(2, accountId);
            ps.executeUpdate();
        }
    }

    public List<Account> findAll() throws SQLException, ClassNotFoundException {
        List<Account> accounts = new ArrayList<>();


        Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect();
        }// Example: Reconnect method (not a standard method)
         try (    PreparedStatement ps = con.prepareStatement("SELECT * FROM account")) {
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
}
