package org.example.gps_g22;

import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.commands.*;
import org.example.gps_g22.model.data.Account;
import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.transaction.Transaction;
import org.example.gps_g22.model.data.transaction.TransactionSource;
import org.example.gps_g22.model.data.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {

    DataModel dataModel;
    CommandManager cmdManager;

    static String accountName = "TestName";
    static long cardNum = 1234567890123456L;
    static String validade = "12/2024";

    @BeforeEach
//reset between tests
    public void setup() {
        dataModel = new DataModel("TestUser", "Pass");
        cmdManager = new CommandManager();
    }

    @Test
    public void testCardAddOperation() {
        int ano = 2024;
        int m = 12;

        cmdManager.invokeCommand(new AddCardCommand(dataModel, accountName, cardNum, m, ano));

        assertFalse(dataModel.getCurrentUser().accountNameAvailable(accountName));// account exists

        cmdManager.undo();

        assertTrue(dataModel.getCurrentUser().accountNameAvailable(accountName));// account doesn't exist

    }

    @Test
    public void testCardRemoveOperation() {

        // Arrange //
        Account account = new Account(accountName, validade, cardNum);
        dataModel.createAccount(account);


        cmdManager.invokeCommand(new RemoveCardCommand(dataModel, account.getAccountID()));

        assertNull(dataModel.getCurrentUser().getAccountById(account.getAccountID()));

    }

    @Test
    public void testTransactAddOperation() {

        // Arrange //
        Account account = new Account(accountName, validade, cardNum);
        dataModel.createAccount(account);

        TransactionSource payTheBank = new TransactionSource("Bank - rent", 123345L, TransactionType.EXPENSE);
        TransactionSource passOnGoGet2000 = new TransactionSource("Bank - benefits", 123345L, TransactionType.INCOME);

        Date dateTimeNow = new Date(Instant.now().toEpochMilli());

        Date dateTimeLastMonth = new Date(Instant.now().minus(Duration.ofDays(30)).toEpochMilli());

        Date dateTimeNextMonth = new Date(Instant.now().plus(Duration.ofDays(30)).toEpochMilli());

        // Act //

        cmdManager.invokeCommand(new AddTransactionCommand(dataModel, account.getAccountID(), payTheBank, 999.99,
                dateTimeLastMonth));

        cmdManager.invokeCommand(new AddTransactionCommand(dataModel, account.getAccountID(), passOnGoGet2000, 2000,
                dateTimeNextMonth));

        cmdManager.invokeCommand(new AddTransactionCommand(dataModel, account.getAccountID(), payTheBank, 9000.01,
                dateTimeNow));


        // Assert //
        List<TransactInfo> transacts = dataModel.getTransactions(account.getAccountID(), 3);

        assertEquals(3, transacts.size());
        assertEquals(999.99, transacts.get(0).getAmount());
        assertEquals(2000, transacts.get(1).getAmount());
        assertEquals(9000.01, transacts.get(2).getAmount());
    }

    @Test
    public void testTransactRemoveOperation() {

        // Arrange //

        Account account = new Account(accountName, validade, cardNum);
        dataModel.createAccount(account);

        TransactionSource povertyWages = new TransactionSource("Job", 54321L, TransactionType.INCOME);

        Date dateTimeNow = new Date(Instant.now().toEpochMilli());
        Date dateTimeLastMonth = new Date(Instant.now().minus(Duration.ofDays(30)).toEpochMilli());
        Date dateTimeMonthBefore = new Date(Instant.now().minus(Duration.ofDays(30 * 2)).toEpochMilli());

        Transaction t1 = new Transaction(povertyWages, 1.1, dateTimeMonthBefore);
        Transaction t2 = new Transaction(povertyWages, 1.2, dateTimeLastMonth);
        Transaction t3 = new Transaction(povertyWages, 1.3, dateTimeNow);

        account.addTransaction(t1);
        account.addTransaction(t2);
        account.addTransaction(t3);
        // Act //

        cmdManager.invokeCommand(new RemoveTransactionCommand(dataModel, t2.getTransactId(), account.getAccountID(),
                TransactionType.INCOME));

        // Assert //
        List<TransactInfo> transacts = dataModel.getTransactions(account.getAccountID(), 3);

        assertEquals(2, transacts.size());
        assertEquals(1.1, transacts.get(0).getAmount());
        assertEquals(1.3, transacts.get(1).getAmount());

    }
}
