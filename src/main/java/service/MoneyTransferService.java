package service;

import model.Account;
import model.MoneyTransfer;

import java.math.BigDecimal;
import java.util.*;

public class MoneyTransferService {

    private int nextTransferID = 0;

    private AccountService accountService;

    private List<MoneyTransfer> moneyTransfers = new ArrayList<>();

    public MoneyTransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public int addTransfer(int accountFromID, int accountToId, BigDecimal moneyAmount) {
        MoneyTransfer transfer = new MoneyTransfer(new Date(), accountFromID, accountToId, moneyAmount);
        processTransfer(transfer);
        //TODO: in case of process failure, do not put the transfer in the map
        transfer.setID(nextTransferID);
        moneyTransfers.add(transfer);

        return nextTransferID++;
    }

    public Collection<MoneyTransfer> getMoneyTransfers() {
        return moneyTransfers;
    }

    public MoneyTransfer getMoneyTransfer(int moneyTransferID) {
        Optional<MoneyTransfer> matchingObject = moneyTransfers.stream().
                filter(mt -> mt.getID() == moneyTransferID).
                findFirst();

        return matchingObject.orElse(null);
    }

    private void processTransfer(MoneyTransfer transfer) {
        Account accountFrom = accountService.getAccount(transfer.getAccountFromID());
        Account accountTo = accountService.getAccount(transfer.getAccountToID());

        if (accountFrom == null || accountTo == null) {
            return;
        }

        accountFrom.withdraw(transfer.getMoneyAmount());
        accountTo.deposit(transfer.getMoneyAmount());
    }
}
