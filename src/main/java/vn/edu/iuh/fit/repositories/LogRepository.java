package vn.edu.iuh.fit.repositories;

import vn.edu.iuh.fit.connect.ConnectDB;
import vn.edu.iuh.fit.models.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class LogRepository {
    public void insert(Log log) throws Exception, SQLException {
        Connection con= ConnectDB.getInstance().getConnection();
        String sql="insert into log (account_id, login_time, logout_time, notes) values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, log.getAccount().getAccountId());
        ps.setDate(2, Date.valueOf(LocalDate.now()));
        ps.setDate(3, log.getLogoutTime());
        ps.setString(4, log.getNote());
        ps.executeUpdate();
    }

    public Log getLastLoginLog(String accountId) {
        Log log = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM log WHERE account_id = ? AND logout_time IS NOT NULL ORDER BY login_time DESC LIMIT 1")) {
            ps.setString(1, accountId);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    log = new Log();
                    log.setId(rs.getString("id"));
                    log.setAccount(
                            new AccountRepository().getAccountById(rs.getString("account_id"))
                    );
                    log.setLoginTime(rs.getDate("login_time"));
                    log.setLogoutTime(rs.getDate("logout_time"));
                    log.setNote(rs.getString("notes"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return log;
    }

    public void update(Log lastLoginLog) throws SQLException, ClassNotFoundException {
       Connection con = ConnectDB.getInstance().getConnection();
        if (con.isClosed()) {
            // Reinitialize the connection or handle appropriately
            con = ConnectDB.getInstance().reconnect(); // Example: Reconnect method (not a standard method)
}
          try (    PreparedStatement ps = con.prepareStatement("UPDATE log SET logout_time = ?, notes=? WHERE id = ?")) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setString(2, lastLoginLog.getNote());
            ps.setString(3, lastLoginLog.getId());


            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
