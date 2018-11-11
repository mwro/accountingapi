package service;

import model.Account;

import java.util.*;

public class AccountService {

    private int nextAccountID = 0;
    private Map<Integer, Account> accounts = new HashMap<>();

    public int createAccount(String name) {
        Account account = new Account(name);
        accounts.put(nextAccountID, account);

        return nextAccountID++;
    }

    public Account getAccount(int accountID) {
        return accounts.get(accountID);
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

}
