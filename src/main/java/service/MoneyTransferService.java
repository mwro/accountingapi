package service;

import model.Account;
import model.MoneyTransfer;

import java.util.HashMap;
import java.util.Map;

public class MoneyTransferService {
    private int nextTransferID = 0;

    private AccountService accountService;

    private Map<Integer, MoneyTransfer> moneyTransfers = new HashMap<>();

    public void addTransfer(MoneyTransfer transfer)
    {
        processTransfer(transfer);
        //TODO: in case of process failure, do not put the transfer in the map
        moneyTransfers.put(nextTransferID++, transfer);
    }

    private void processTransfer(MoneyTransfer transfer) {
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId());

        if (accountFrom == null || accountTo == null){
            return;
        }

        accountFrom.withdraw(transfer.getMoneyAmount());
        accountTo.deposit(transfer.getMoneyAmount());
    }
}
