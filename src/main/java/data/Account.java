package data;

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

    public void withdraw(BigDecimal amount)
    {
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount)
    {
        balance = balance.add(amount);
    }
}
