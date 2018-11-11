package model;

import java.math.BigDecimal;

public class Account {

    private final String name;
    private BigDecimal balance = BigDecimal.ZERO;

    public Account(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Name: " + name + ", balance: " + balance;
    }
}
