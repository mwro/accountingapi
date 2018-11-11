package service;

import model.Account;
import model.MoneyTransfer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransferService {

    private int nextTransferID = 0;

    private AccountService accountService;

    private Map<Integer, MoneyTransfer> moneyTransfers = new HashMap<>();

    public MoneyTransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public int addTransfer(int accountFromID, int accountToId, BigDecimal moneyAmount)
    {
        MoneyTransfer transfer = new MoneyTransfer(new Date(), accountFromID, accountToId, moneyAmount);
        processTransfer(transfer);
        //TODO: in case of process failure, do not put the transfer in the map
        moneyTransfers.put(nextTransferID, transfer);

        return nextTransferID++;
    }

    public Collection<MoneyTransfer> getMoneyTransfers(){
        return moneyTransfers.values();
    }

    public MoneyTransfer getMoneyTransfer(int moneyTransferID) {
        return moneyTransfers.get(moneyTransferID);
    }

    private void processTransfer(MoneyTransfer transfer){
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId());

        if (accountFrom == null || accountTo == null){
            return;
        }

        accountFrom.withdraw(transfer.getMoneyAmount());
        accountTo.deposit(transfer.getMoneyAmount());
    }
}
