package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.Account;
import org.example.gps_g22.model.data.DataModel;


public class AddCardCommand extends AbstractCommand {

    private final String accountName;
    private Account account;
    private final long card;
    private final String val;


    public AddCardCommand(DataModel model, String accountName, long cardNum, int m, int ano) {
        super(model);
        this.accountName = accountName;
        this.card = cardNum;
        this.val = m + "/" + ano;
    }

    @Override
    public boolean execute() {
        if (account == null) {
            account = model.createAccount(new Account(accountName, val, card));
        }

        return account != null;
    }

    @Override
    public boolean undo() {
        if (account == null)
            return false;

        return model.removeAccount(account.getAccountID()) != null;
    }
}
