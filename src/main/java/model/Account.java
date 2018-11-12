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
    private Account() {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void withdraw(BigDecimal value) {
        balance = balance.subtract(value);
    }

    public void deposit(BigDecimal value) {
        balance = balance.add(value);
    }
}
