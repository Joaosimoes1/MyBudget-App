package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.transaction.Transaction;
import org.example.gps_g22.model.data.transaction.TransactionSource;

import java.util.Date;

public class AddTransactionCommand extends AbstractCommand {

    private final TransactionSource source;
    private final double ammount;
    private final Date dateOfTranasction;
    private final int accountId;
    private Transaction transact;


    public AddTransactionCommand(DataModel model, int accountId, TransactionSource source, double ammount,
                                 Date dateOfTranasction) {
        super(model);
        this.accountId = accountId;
        this.source = source;
        this.ammount = ammount;
        this.dateOfTranasction = dateOfTranasction;
    }

    @Override
    public boolean execute() {
        if (transact == null) {
            transact = new Transaction(source, ammount, dateOfTranasction);
        }
        return model.addTransaction(transact, accountId);
    }

    @Override
    public boolean undo() {
        Transaction aux = model.removeTransaction(transact.getTransactId(), accountId, transact.getSource().getType());

        // same reference => same object => correct elimination
        return aux == transact;
    }

}
