package org.example.gps_g22.dto;

import org.example.gps_g22.model.data.transaction.Transaction;

import java.io.Serializable;
import java.util.Date;

public class TransactInfo implements Serializable {

    private final int transactId;
    private final String nameSource;
    private final String descriptionSource;
    private final long nifSource;
    private final double amount;
    private final Date dateOfTransaction;
    private final String type;

    public TransactInfo(int transactId, String nameSource, String descriptionSource, long nifSource, double ammount, Date dateOfTransaction, String type) {
        this.transactId = transactId;
        this.nameSource = nameSource;
        this.descriptionSource = descriptionSource;
        this.nifSource = nifSource;
        this.amount = ammount;
        this.dateOfTransaction = dateOfTransaction;
        this.type = type;
    }

    public TransactInfo(Transaction original) {
        this.nameSource = original.getSource().getName();
        this.descriptionSource = original.getSource().getDescription();
        this.nifSource = original.getSource().getNif();
        this.type = original.getSource().getType().toString();
        this.amount = original.getAmount();
        this.dateOfTransaction = original.getDateOfTransaction();
        transactId = original.getTransactId();
    }

    public int getTransactId() {
        return transactId;
    }

    public String getName() {
        return nameSource;
    }

    public long getNif() {
        return nifSource;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return descriptionSource;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    @Override
    public String toString() {
        //", Description: " + descriptionSource +
        return String.format(//"ID: " + transactId +
                "Account: %s, NIF: %d, Amount: %s, Date: %s, Type: %s".formatted(nameSource, nifSource, amount, dateOfTransaction, type));
    }

    public String toCsvItem() {
        //", Description: " + descriptionSource +
        return String.format(//"ID: " + transactId +
                "%s, %d, %s, %s, %s".formatted(nameSource, nifSource, amount, dateOfTransaction, type));
    }
}


