package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        Account existing = accountDAO.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existing != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        return accountDAO.findByUsernameAndPassword(username, password);
    }
}
