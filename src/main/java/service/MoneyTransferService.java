package service;

import model.Account;
import model.MoneyTransfer;

import java.util.*;

public class MoneyTransferService {

    private int nextTransferID = 0;

    private AccountService accountService;

    private List<MoneyTransfer> moneyTransfers = new ArrayList<>();

    public MoneyTransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void addMoneyTransfer(MoneyTransfer moneyTransfer) {
        processMoneyTransfer(moneyTransfer);
        //TODO: in case of process failure, do not put the moneyTransfer in the map
        moneyTransfer.setID(nextTransferID++);
        moneyTransfers.add(moneyTransfer);
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

    private void processMoneyTransfer(MoneyTransfer moneyTransfer) {
        Account accountFrom = accountService.getAccount(moneyTransfer.getAccountFromID());
        Account accountTo = accountService.getAccount(moneyTransfer.getAccountToID());

        if (accountFrom != null) {
            accountFrom.withdraw(moneyTransfer.getTransferValue());
        }

        if (accountTo != null) {
            accountTo.deposit(moneyTransfer.getTransferValue());
        }
    }
}
