package org.example.gps_g22.model.data;

import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.data.transaction.Transaction;
import org.example.gps_g22.model.data.transaction.TransactionType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static int accountCounter= 1;
    private final int accountID;
    private final long accountNumber;
    private final List<Transaction> transact; // Lista de transações para mostrar no card controller
    private final List<SpendingGoal> spendingGoals; // Lista de SpendingGoals para mostrar no card Budget Managment

    private String accountName;
    private final String validade;


    public Account(String name, String validade, long accountNumber) {
        accountID = accountCounter;
        accountCounter++;
        accountName = name;
        this.validade = validade;
        transact = new ArrayList<>();
        this.accountNumber = accountNumber;
        spendingGoals = new ArrayList<>();

    }

    public Account(Account account) {
        //construtor por copia
        this.accountID = account.getAccountID();
        this.accountName = account.accountName;
        this.transact = account.transact;
        this.accountNumber = account.accountNumber;
        this.validade = account.validade;
        this.spendingGoals = account.spendingGoals;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getValidade() {
        return validade;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountID() {
        return accountID;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public boolean addTransaction(Transaction t) {
        if (t == null || transact.contains(t)) {
            return false;
        }

        return this.transact.add(new Transaction(t));
    }


    public boolean addSpendingGoal(SpendingGoal sG) {
        if (sG == null) {
            System.err.println("SpendingGoal é nulo!");
            return false;
        }
        if (spendingGoals == null) {
            System.err.println("Lista spendingGoals não foi inicializada!");
            return false;
        }
        return this.spendingGoals.add(new SpendingGoal(sG));
    }


    public SpendingGoal removeSpendingGoal(int goalId) {
        for (SpendingGoal sg : spendingGoals) {
            if (sg.getId() == goalId) {
                this.spendingGoals.remove(sg);
                return sg;
            }
        }
        return null;
    }

    public Transaction removeTransaction(int tId, TransactionType type) {
        Transaction transactionToRemove = getTransactionByIdAndType(tId,type);

        if (transactionToRemove == null) {
            return null;
        }

        boolean removeFromTransact = this.transact.remove(transactionToRemove);

        if (removeFromTransact) {
            return transactionToRemove;
        }

        return null;
    }

    public int getTransactionIndex(int tId) {
        for (int i = 0; i < this.transact.size(); i++) {
            if (this.transact.get(i).getTransactId() == tId) {
                return i;
            }
        }
        return -1;
    }

    public Transaction getTransactionByIdAndType(int tId, TransactionType type) {
        for (Transaction t : transact) {
            if (t.getTransactId() == tId && t.getSource().getType()==type) {
                return t;
            }
        }
        return null;
    }

    public double getCurrentBalance() {
        double total = 0;

        for (Transaction t :
                this.transact) {
            if (t.getSource().getType() == TransactionType.INCOME) {
                total += t.getAmount();
            } else if (t.getSource().getType() == TransactionType.EXPENSE) {
                total -= t.getAmount();
            }
        }

        return total;
    }

    public List<TransactInfo> getTransactions() {
        return transact.stream().map(TransactInfo::new).toList();
    }

    public List<TransactInfo> getIncomeTransactions() {
        return transact.stream().map(TransactInfo::new).toList();
    }

    public List<TransactInfo> getExpensesTransactions() {
        return transact.stream().map(TransactInfo::new).toList();
    }

    public List<TransactInfo> getIncomeTransact() {
        return transact.stream().filter(t -> t.getSource().getType() == TransactionType.INCOME)
                .map(TransactInfo::new).toList();
    }

    public List<TransactInfo> getExpenseTransactions() {
        return transact.stream().filter(t -> t.getSource().getType() == TransactionType.EXPENSE)
                .map(TransactInfo::new).toList();
    }

    public List<SpendingGoal> getSpendingGoals() {
        if (spendingGoals == null) {
            return new ArrayList<>(); // Retorna uma lista vazia para evitar null
        }
        return spendingGoals.stream().toList();
    }

}
