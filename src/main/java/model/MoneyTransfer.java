package model;

import java.math.BigDecimal;
import java.util.Date;

public class MoneyTransfer {

    private Date date;
    private int accountFromId;
    private int accountToId;
    private BigDecimal moneyAmount;

    public MoneyTransfer(Date date, int accountFromId, int accountToId, BigDecimal moneyAmount) {
        this.date = date;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.moneyAmount = moneyAmount;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }
}
