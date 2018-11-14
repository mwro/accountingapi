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

    public void addMoneyTransfer(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
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
        processWithdrawal(moneyTransfer);
        processDeposit(moneyTransfer);
    }

    private void processWithdrawal(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        Integer accountFromID = moneyTransfer.getAccountFromID();
        if (accountFromID != null) {
            Account accountFrom = getAccount(accountFromID);

            if (accountFrom == null) {
                throw new MoneyTransferServiceException("Account to withdraw money from does not exist");
            }

            accountFrom.withdraw(moneyTransfer.getTransferValue());
        }
    }

    private void processDeposit(MoneyTransfer moneyTransfer) throws MoneyTransferServiceException {
        Integer accountToID = moneyTransfer.getAccountToID();
        if (accountToID != null) {
            Account accountTo = getAccount(accountToID);

            if (accountTo == null) {
                throw new MoneyTransferServiceException("Account to deposit money to does not exist");
            }

            accountTo.deposit(moneyTransfer.getTransferValue());
        }
    }

    private Account getAccount(Integer accountID) {
        return accountService.getAccount(accountID);
    }
}
