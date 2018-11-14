package model;

import json.DeserializeExclude;
import service.MoneyTransferServiceException;

import java.math.BigDecimal;

public class Account {

    @DeserializeExclude
    private int ID;

    private String name;

    @DeserializeExclude
    private BigDecimal balance;

    //override no-parameter constructor for Gson
    //to initialize balance default value
    public Account() {
        balance = BigDecimal.ZERO;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void withdraw(BigDecimal value) throws MoneyTransferServiceException {
        BigDecimal result = balance.subtract(value);
        if (bigDecimalLowerThanZero(result)) {
            throw new MoneyTransferServiceException("No sufficient funds to withdraw");
        }
        balance = result;
    }

    private boolean bigDecimalLowerThanZero(BigDecimal result) {
        return result.compareTo(BigDecimal.ZERO) < 0;
    }

    public void deposit(BigDecimal value) {
        balance = balance.add(value);
    }
}
