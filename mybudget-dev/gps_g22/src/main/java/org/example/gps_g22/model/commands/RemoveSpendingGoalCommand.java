package org.example.gps_g22.model.commands;

import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.SpendingGoal;

public class RemoveSpendingGoalCommand extends AbstractCommand{

    private final int accountId;
    private SpendingGoal spendingGoal;
    private int goalId;


    public RemoveSpendingGoalCommand(DataModel model, int accountId, int goalId) {
        super(model);
         this.accountId = accountId;
         this.goalId = goalId;
    }

    @Override
    public boolean execute() {
        spendingGoal = model.removeSpendingGoal(goalId, accountId);

        return spendingGoal != null;

    }

    @Override
    public boolean undo() {
        if(spendingGoal == null)
            return false;

        return model.addSpendingGoal(spendingGoal, accountId);
    }
}
