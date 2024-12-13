package org.example.gps_g22;

import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.commands.AddCardCommand;
import org.example.gps_g22.model.commands.CommandManager;
import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.SpendingGoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetTest {

    DataModel dataModel;
    CommandManager cmdManager;

    static String accountName = "TestName";

    @BeforeEach
    public void setup() {
        long cardNum = 1234567890123456L;
        int ano = 2024;
        int m = 12;
        dataModel = new DataModel("TestUser", "Pass");
        cmdManager = new CommandManager();
        cmdManager.invokeCommand(new AddCardCommand(dataModel, accountName, cardNum, m, ano));
    }

    // TODO
    @Test
    public void testBudget() {

        AccountInfo account = dataModel.getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(accountName))
                .toList().get(0);

        var spendingGoal = new SpendingGoal("name", 10.01, 5.5);

        assertTrue(dataModel.addSpendingGoal(spendingGoal, account.getId()));

        List<SpendingGoal> goals = dataModel.getSpendingGoal(  account.getId());

        assertEquals(1, goals.size());
        assertEquals(10.01, goals.get(0).getGoalAmount());
        assertEquals(5.5, goals.get(0).getSpentAmount());

    }
}
