package org.example.gps_g22.model.data;

import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.data.stock.Stock;
import org.example.gps_g22.model.data.stock.StockType;
import org.example.gps_g22.model.data.transaction.Transaction;
import org.example.gps_g22.model.data.transaction.TransactionType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DataModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final User currentUser;
    private final Portfolio portfolio;


//    public DataModel(){};

    // Método para criar um novo usuário e definir como usuário atual
    public DataModel(String name, String password) {
        this.currentUser = new User(name, password);
        this.portfolio = new Portfolio();
    }

    public boolean addTransaction(Transaction transact, int accountNum) {
        Account account = currentUser.getAccountById(accountNum);
        if (account == null) return false;

        return account.addTransaction(transact);
    }

    public Transaction removeTransaction(int transactId, int accountNum, TransactionType type) {
        Account account = currentUser.getAccountById(accountNum);
        if (account == null) return null;

        return account.removeTransaction(transactId, type);
    }

    public boolean addSpendingGoal(SpendingGoal spendingGoal, int accountNum) {
        Account account = currentUser.getAccountById(accountNum);
        if (account == null) {
            System.err.println("Conta não encontrada para o ID: " + accountNum);
            return false;
        }

        boolean added = account.addSpendingGoal(spendingGoal);
        if (!added) {
            System.err.println("Falha ao adicionar meta de gastos na conta com ID: " + accountNum);
        }
        return added;
    }

    public SpendingGoal removeSpendingGoal(int goalId, int accountNum) {
        Account account = currentUser.getAccountById(accountNum);
        if (account == null) return null;

        return account.removeSpendingGoal(goalId);
    }

    public Account createAccount(Account account) {
        return currentUser.createAccount(account);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Account removeAccount(int accountId) {
        return currentUser.removeAccount(accountId);
    }

    public List<AccountInfo> getAccounts() {
        return currentUser.getAccounts();
    }

    public boolean accoutNameAvailable(String nameAccount) {
        return currentUser.accountNameAvailable(nameAccount);
    }

    public List<TransactInfo> getTransactions(int selectedCard) {
        return currentUser.getTransactions(selectedCard);
    }

    public List<TransactInfo> getTransactions(int selectedCard, int maxItems) {
        List<TransactInfo> list = currentUser.getTransactions(selectedCard);
        if (list == null) {
            return List.of();
        }

        return list.stream().limit(maxItems).toList();
    }

    public List<TransactInfo> getIncomeTransact(int selectedCard) {
        return currentUser.getIncomeTransact(selectedCard);
    }

    public List<TransactInfo> getExpenseTransactions(int selectedCard) {
        return currentUser.getExpenseTransactions(selectedCard);
    }

    public List<SpendingGoal> getSpendingGoal(int selectCard) {
        return currentUser.getSpendingGoals(selectCard);
    }


    public List<TransactInfo> getIncomeTransact(int selectedCard, String orderBy) throws IllegalArgumentException {
        return orderTransactions(currentUser.getIncomeTransact(selectedCard), orderBy);
    }

    public List<TransactInfo> getExpenseTransactions(int selectedCard, String orderBy) throws IllegalArgumentException {
        return orderTransactions(currentUser.getExpenseTransactions(selectedCard), orderBy);
    }

    public List<TransactInfo> orderTransactions(List<TransactInfo> list, String orderBy) throws IllegalArgumentException {
        if (list == null) {
            return List.of();
        }

        if (orderBy.equalsIgnoreCase("date")) {
            return list.stream().sorted(Comparator.comparing(TransactInfo::getDateOfTransaction)).toList();
        } else if (orderBy.equalsIgnoreCase("category")) {
            return list.stream().sorted(Comparator.comparing(TransactInfo::getType)).toList();
        } else {
            throw new IllegalArgumentException("not a valid ordering parameter");
        }
    }

    public void buyStock(double amount, String nameText, double price, StockType stockType, Date selectedDate) {
        portfolio.buyStock(amount, nameText, price, stockType, selectedDate);
    }

    public List<String> showStocks() {
        return portfolio.showStocks();
    }

    public void sellStock(String nameText, double amount, Date selectedDate) {
        portfolio.sellStock(nameText, amount, selectedDate);
    }

    public List<Stock> getStockHistoryList() {
        return portfolio.getStockHistoryList();
    }

    public List<Stock> getOrdinariesHistoryList() {
        return portfolio.getOrdinariesHistoryList();
    }

    public List<Stock> getPreferredHistoryList() {
        return portfolio.getPreferredHistoryList();
    }

    public List<Stock> getSaleHistoryList() {
        return portfolio.getSaleHistoryList();
    }
}
