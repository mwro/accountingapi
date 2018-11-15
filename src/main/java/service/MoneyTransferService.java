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

    public synchronized void addMoneyTransfer(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        processMoneyTransfer(moneyTransfer);
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

    private void processMoneyTransfer(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        Account accountFrom = getWithdrawalAccount(moneyTransfer);
        Account accountTo = getDepositAccount(moneyTransfer);

        if (accountFrom != null) {
            accountFrom.withdraw(moneyTransfer.getTransferValue());
        }

        if (accountTo != null) {
            accountTo.deposit(moneyTransfer.getTransferValue());
        }
    }

    private Account getWithdrawalAccount(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        Integer accountFromID = moneyTransfer.getAccountFromID();
        if (accountFromID == null)
            return null;

        Account accountFrom = getAccount(accountFromID);

        if (accountFrom == null) {
            throw new MoneyTransferServiceException("Account to withdraw money from does not exist");
        }
        return accountFrom;
    }

    private Account getDepositAccount(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        Integer accountToID = moneyTransfer.getAccountToID();
        if (accountToID == null)
            return null;

        Account accountTo = getAccount(accountToID);

        if (accountTo == null) {
            throw new MoneyTransferServiceException("Account to deposit money to does not exist");
        }
        return accountTo;
    }

    private Account getAccount(Integer accountID) {
        return accountService.getAccount(accountID);
    }
}
