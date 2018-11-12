package model;


import java.math.BigDecimal;
import java.util.Date;

public class MoneyTransfer {

    private int ID;
    private Date date;
    private int accountFromID;
    private int accountToID;
    private BigDecimal transferValue;

    public MoneyTransfer(Date date, int accountFromID, int accountToID, BigDecimal transferValue) {
        this.date = date;
        this.accountFromID = accountFromID;
        this.accountToID = accountToID;
        this.transferValue = transferValue;
    }

    public int getAccountFromID() {
        return accountFromID;
    }

    public int getAccountToID() {
        return accountToID;
    }

    public BigDecimal getTransferValue() {
        return transferValue;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "From account with ID: " + accountFromID + ", to account with ID: " + accountToID
                + ", transfer value: " + transferValue + ", date: " + date + "\n";
    }
}
