package org.example.gps_g22.dto;

import org.example.gps_g22.model.data.Account;

public class AccountInfo {

    private final int id;
    private final String name;
    private final double balance;
    private final long accNumber;

    private final String validade;

    private AccountInfo() {
        this.id = -1;
        this.name = "No Account Selected";
        this.balance = 0;
        this.accNumber = 0;
        this.validade = " ";
    }


    public AccountInfo(Account original) {
        this.id = original.getAccountID();
        this.name = original.getAccountName();
        this.balance = original.getCurrentBalance();
        this.accNumber = original.getAccountNumber();
        this.validade = original.getValidade();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getValidade() {
        return validade;
    }

    public long getAccNumber() {
        return accNumber;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static AccountInfo getDummyAccount() {
        return new AccountInfo();
    }

}
