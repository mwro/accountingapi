package service;

import model.Account;

import java.util.*;

public class AccountService {

    private int nextAccountID = 0;
    private List<Account> accounts = new ArrayList<>();

    public int createAccount(String name) {
        Account account = new Account(name);
        account.setID(nextAccountID);
        accounts.add(account);

        return nextAccountID++;
    }

    public Account getAccount(int accountID) {
        Optional<Account> optionalAccount = accounts.stream().
                filter(a -> a.getID() == accountID).
                findFirst();

        return optionalAccount.orElse(null);
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

}
