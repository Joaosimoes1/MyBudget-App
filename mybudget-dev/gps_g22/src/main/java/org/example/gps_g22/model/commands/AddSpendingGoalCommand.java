package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.SpendingGoal;
import org.example.gps_g22.model.data.transaction.Transaction;

public class AddSpendingGoalCommand extends AbstractCommand{

    private final String name;
    private final double amountGoal;
    private final double spendGoal;
    private SpendingGoal spendingGoal;
    private final int accountId;

    public AddSpendingGoalCommand(DataModel model,int accountId, String name, Double amountGoal, Double spendGoal){
        super(model);
        this.name = name;
        this.accountId = accountId;
        this.amountGoal = amountGoal;
        this.spendGoal = spendGoal;
    }

    @Override
    public boolean execute() {
        if (spendingGoal == null) {
            spendingGoal = new SpendingGoal(name, amountGoal, spendGoal);
        }
        return model.addSpendingGoal(spendingGoal, accountId);
    }

    @Override
    public boolean undo() {
        SpendingGoal aux = model.removeSpendingGoal(spendingGoal.getId(), accountId);

        // same reference => same object => correct elimination
        return aux == spendingGoal;
    }
}
