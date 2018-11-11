package service;

import model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    private int nextAccountID = 0;
    private Map<Integer, Account> accounts = new HashMap<>();

    public int createAccount(String name)
    {
        Account account = new Account(name);
        accounts.put(nextAccountID, account);

        return nextAccountID++;
    }

    public Account getAccount(int accountID)
    {
        return accounts.get(accountID);
    }

    public List<Account> getAccounts()
    {
        return new ArrayList<>(accounts.values());
    }

}
