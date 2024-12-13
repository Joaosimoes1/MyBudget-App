package org.example.gps_g22.model.data.transaction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static int transactCounter = 1;
    private final int transactId;
    private final TransactionSource source;
    private final double ammount;

    private final Date dateOfTranasction;

    public Transaction(TransactionSource source, double ammount, Date dateOfTransaction) {
        this.source = source;
        this.ammount = ammount;
        this.dateOfTranasction = dateOfTransaction;

        transactId = transactCounter;
        transactCounter++;
    }

    public Transaction(Transaction original) {
        this.source = original.source;
        this.ammount = original.ammount;
        this.dateOfTranasction = original.dateOfTranasction;
        transactId = original.transactId;
    }

    public int getTransactId() {
        return transactId;
    }

    public TransactionSource getSource() {
        return source;
    }

    public double getAmount() {
        return ammount;
    }

    public Date getDateOfTransaction() {
        return dateOfTranasction;
    }
}
