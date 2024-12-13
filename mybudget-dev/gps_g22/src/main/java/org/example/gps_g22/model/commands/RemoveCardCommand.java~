package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.Account;
import org.example.gps_g22.model.data.DataModel;

public class RemoveCardCommand extends AbstractCommand{



    private final int accountId;
    private Account account;


    public RemoveCardCommand(DataModel model, int accountId) {
        super(model);
        this.accountId = accountId;
    }

    @Override
    public boolean execute() {
        account = model.removeAccount(accountId);
        return account == null;
    }

    @Override
    public boolean undo() {
        if(account == null)
            return false;

        return model.createAccount(account) != null;
    }
}
