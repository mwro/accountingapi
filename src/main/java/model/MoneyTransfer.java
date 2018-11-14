package model;


import json.DeserializeExclude;

import java.math.BigDecimal;
import java.util.Date;

public class MoneyTransfer {

    @DeserializeExclude
    private int ID;
    private Integer accountFromID;
    private Integer accountToID;
    private BigDecimal transferValue;
    @DeserializeExclude
    private Date date;

    //override no-parameter constructor for Gson
    //to initialize date default value
    public MoneyTransfer() {
        date = new Date();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Integer getAccountFromID() {
        return accountFromID;
    }

    public void setAccountFromID(Integer accountFromID) {
        this.accountFromID = accountFromID;
    }

    public Integer getAccountToID() {
        return accountToID;
    }

    public void setAccountToID(Integer accountToID) {
        this.accountToID = accountToID;
    }

    public BigDecimal getTransferValue() {
        return transferValue;
    }

    public void setTransferValue(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public Date getDate() {
        return date;
    }
}
