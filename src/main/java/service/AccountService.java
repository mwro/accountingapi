package service;

import model.Account;

import java.util.*;

public class AccountService {

    private int nextAccountID = 0;
    private List<Account> accounts = new ArrayList<>();

    public synchronized void addAccount(Account account) {
        if (account == null)
            return;

        account.setID(nextAccountID++);
        accounts.add(account);
    }

    public Account getAccount(Integer accountID) {
        if (accountID == null) {
            return null;
        }

        Optional<Account> optionalAccount = accounts.stream().
                filter(a -> accountID.equals(a.getID())).
                findFirst();

        return optionalAccount.orElse(null);
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

}
