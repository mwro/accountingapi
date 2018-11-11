package data;

import java.util.*;

public class DataStore {

    private int nextAccountID = 0;
    private int nextTransferID = 0;

    private Map<Integer, Account> accounts = new HashMap<>();
    private Map<Integer, MoneyTransfer> moneyTransfers = new HashMap<>();

    public void addAccount(Account account)
    {
        accounts.put(nextAccountID++, account);
    }

    public void addTransfer(MoneyTransfer transfer)
    {
        processTransfer(transfer);
        //TODO: in case of process failure, do not put the transfer in the map
        moneyTransfers.put(nextTransferID++, transfer);
    }

    private void processTransfer(MoneyTransfer transfer) {
        Account accountFrom = accounts.get(transfer.getAccountFromId());
        Account accountTo = accounts.get(transfer.getAccountToId());

        if (accountFrom == null || accountTo == null){
            return;
        }

        accountFrom.withdraw(transfer.getMoneyAmount());
        accountTo.deposit(transfer.getMoneyAmount());
    }
}
