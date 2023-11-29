package vn.edu.iuh.fit;

import vn.edu.iuh.fit.models.*;
import vn.edu.iuh.fit.repositories.AccountRepository;
import vn.edu.iuh.fit.repositories.GrantAccessRepository;
import vn.edu.iuh.fit.repositories.RoleRepository;

public class Init {
    public static void main(String[] args) throws Exception {
        AccountRepository accountRepository = new AccountRepository();
        RoleRepository roleRepository = new RoleRepository();
        GrantAccessRepository grantAccessRepository = new GrantAccessRepository();
        Role role1 = new Role("1", "ADMIN", "", Status.ACTIVE.getNumber());
        Role role2 = new Role("2", "USER", "", Status.ACTIVE.getNumber());
        Account account1 = new Account("1","Le Anh Thu","123456","thuleanh543@gmail.com","0868084056",Status.ACTIVE.getNumber());
        roleRepository.insert(role1);
        Account account2 = new Account("2","Pham Ha","123456",
                "ha@gmail.com","0123456789",Status.ACTIVE.getNumber());
        GrantAccess grantAccess1 = new GrantAccess(role1, account1, Grant.ENABLE.getValue(), "");
        GrantAccess grantAccess2 = new GrantAccess(role2, account2, Grant.ENABLE.getValue(), "");

        roleRepository.insert(role2);


        accountRepository.insert(account1);
        accountRepository.insert(account2);

        grantAccessRepository.insert(grantAccess1);
        grantAccessRepository.insert(grantAccess2);





    }
}
