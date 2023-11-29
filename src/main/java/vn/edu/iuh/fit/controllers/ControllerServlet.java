package vn.edu.iuh.fit.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.iuh.fit.models.*;
import vn.edu.iuh.fit.repositories.AccountRepository;
import vn.edu.iuh.fit.repositories.GrantAccessRepository;
import vn.edu.iuh.fit.repositories.LogRepository;
import vn.edu.iuh.fit.repositories.RoleRepository;
import java.time.Instant;
import java.sql.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/controller")
public class ControllerServlet extends HttpServlet {
    AccountRepository accountRepository = new AccountRepository();
    RoleRepository roleRepository = new RoleRepository();
    LogRepository logRepository = new LogRepository();
    GrantAccessRepository grantAccessRepository = new GrantAccessRepository();
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String action = request.getParameter("action");

            switch (action) {
                case "login":
                    try {
                        performLogin(request, response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "register":
                    performRegister(request, response);
                    break;

                case "logout": {
                    try {
                        performLogout(request, response);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                }
                case "updateAccount":{
                    try {
                        updateAccount(request, response);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;}
                case "lockAccount":
                    try {
                        lockAccount(request, response);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "permission":
                    try {
                        handleManagerRolePost(request, response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                default:

                    break;
            }
        }

    private void lockAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        Account account = (Account) request.getSession().getAttribute("account");
        accountRepository.lockAccount(account.getAccountId());
        request.getSession().setAttribute("message", "Account locked successfully");
        request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        String fullName = request.getParameter("fullname");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int status = Integer.parseInt(request.getParameter("status"));

        // Retrieve the Account object from the session
        Account updatedAccount = (Account) request.getSession().getAttribute("account");

        // Check if the account is null
        if (updatedAccount == null) {
            // Handle the case where the account is not found
            request.getSession().setAttribute("message", "Account not found. Please log in again.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return; // Exit the method
        }

        // Construct an Account object with the updated information
        updatedAccount.setFullName(fullName);
        updatedAccount.setPassword(password);
        updatedAccount.setEmail(email);
        updatedAccount.setPhone(phone);
        updatedAccount.setStatus(status);

        try {
            accountRepository.update(updatedAccount);
            request.getSession().setAttribute("message", "Account information updated successfully");
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "viewDashboard":
                viewDashboard(request, response);
                break;

            case "viewPersonalPage":
                viewPersonalPage(request, response);
                break;
            case "logout":
                try {
                    performLogout(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "lockAccount":
                try {
                    lockAccount(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
                case "updateAccount":{
                    try {
                        updateAccount(request, response);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;}


            default:
                response.getWriter().write("Invalid action for doGet");
                break;
        }
    }



    private void viewDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
    }

    private void viewPersonalPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("PersonalPage.jsp").forward(request, response);
    }

    private void performLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Optional<Account> optionalAccount = accountRepository.logon(email, password);
            if (optionalAccount.isPresent() && optionalAccount.get().getStatus() == Status.ACTIVE.getNumber()) {
                Account account = optionalAccount.get();
                Role role = roleRepository.getRoleOfAccount(account);
                if (role.getRoleName().equals("ADMIN")) {
                    request.getSession().setAttribute("account", account);
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                } else {
                    request.getSession().setAttribute("account", account);
                    request.getRequestDispatcher("PersonalPage.jsp").forward(request, response);
                }
                Log loginLog = new Log();
                loginLog.setAccount(account);
                loginLog.setLoginTime(new Date(System.currentTimeMillis()));
                loginLog.setLogoutTime(new Date(System.currentTimeMillis()));
                loginLog.setNote("login");

                logRepository.insert(loginLog);
            } else {
                request.getSession().setAttribute("message", "Login failed. Please check your credentials.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("message", "An error occurred during login. Please try again later.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void performLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            LogRepository logRepository = new LogRepository();
            Log lastLoginLog = logRepository.getLastLoginLog(account.getAccountId());

            if (lastLoginLog != null) {
                lastLoginLog.setLogoutTime(new Date(System.currentTimeMillis()));
                lastLoginLog.setNote("logout");
                logRepository.update(lastLoginLog);
            }

            session.removeAttribute("account");
            session.invalidate();  // Explicitly invalidate the session

            // Redirect to the login page
            response.sendRedirect("index.jsp");
        } else {
            // Handle the case where the account is not found
            request.getSession().setAttribute("message", "User not logged in.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }




    private void performRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");

            Account account = new Account();
        try {
            account.setAccountId(accountRepository.getAll().size() + 1 + "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        account.setEmail(email);
            account.setPassword(password);
            account.setFullName(fullName);
            account.setPhone(phone);
            account.setStatus(Status.ACTIVE.getNumber());
            try {
                Role role=roleRepository.getRoleByName("USER");
                GrantAccess grantAccess = new GrantAccess(role, account, Grant.ENABLE.getValue(), "");
                accountRepository.insert(account);
                grantAccessRepository.insert(grantAccess);
                request.getSession().setAttribute("message", "Register successfully. Please login to continue.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (SQLException e) {
                request.getSession().setAttribute("message", "An error occurred during registration. Please try again later.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        request.getRequestDispatcher("index.jsp").forward(request, response);
        }

    private void handleManagerRolePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String accountId = req.getParameter("accountID");
        String roleId = req.getParameter("role");
        String note = req.getParameter("note");
        String status = req.getParameter("status");

        // Check if the record exists before deleting it
        if (grantAccessRepository.exists(accountId, roleId)) {
            grantAccessRepository.delete(accountId, roleId);
            System.out.println("Deleted");
        }else {
            Account account = accountRepository.getAccountById(accountId);
            Role role = roleRepository.getRoleByName(roleId);
            if (status.equals("1")) {
                grantAccessRepository.insert(new GrantAccess(role, account, Grant.ENABLE.getValue(), note));
            } else {
                grantAccessRepository.insert(new GrantAccess(role, account, Grant.DISABLE.getValue(), note));
            }

        }
    }


}
