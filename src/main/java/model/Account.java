package model;

import json.DeserializeExclude;

import java.math.BigDecimal;

public class Account {

    @DeserializeExclude
    private int ID;

    private String name;

    @DeserializeExclude
    private BigDecimal balance;

    //override no-parameter constructor for Gson
    //to initialize balance default value
    private Account()
    {
        balance = BigDecimal.ZERO;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
