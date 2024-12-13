package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.transaction.Transaction;
import org.example.gps_g22.model.data.transaction.TransactionType;

public class RemoveTransactionCommand extends AbstractCommand {
    private final int transactId;
    private final int accountId;
    private Transaction transact;
    private TransactionType type;

    public RemoveTransactionCommand(DataModel model, int transactId, int accountId, TransactionType type) {
        super(model);
        this.transactId = transactId;
        this.accountId = accountId;
        this.type = type;
    }

    @Override
    public boolean execute() {
        transact = model.removeTransaction(transactId, accountId, type);
        return transact != null;
    }

    @Override
    public boolean undo() {
        if (transact == null)
            return false;

        return model.addTransaction(transact, accountId);
    }
}
