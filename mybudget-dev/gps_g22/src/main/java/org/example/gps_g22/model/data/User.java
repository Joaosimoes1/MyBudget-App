package org.example.gps_g22.model.data;

import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.dto.TransactInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Account> cards;
    private final String userName;
    private final String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.cards = new ArrayList<>();
    }

    public List<Account> getListCards() {
        return cards.stream().toList();
    }

    public Account getAccountById(int accountNumber) {
        for (Account a : cards) {
            if (a.getAccountID() == accountNumber) {
                return a;
            }
        }
        return null;
    }

    public Account createAccount(Account a) {
        if (a == null) {
            return null;
        }
        for (Account account : cards) {
            if (account.getAccountName().equals(a.getAccountName())) { //Nome já existe
                return null;
            }
        }

        Account account = new Account(a);
        cards.add(account);
        return account;
    }

    public Account removeAccount(int id) {
        return cards.remove(getIndexOfAccount(id));

    }

    public int getIndexOfAccount(int id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getAccountID() == id) {
                return i;
            }
        }
        return -1;
    }

    public String getName() {
        return this.userName;
    }

    public Boolean checkPassword(String pass) {
        return password.equalsIgnoreCase(pass.trim());
    }

    public List<AccountInfo> getAccounts() {
        List<AccountInfo> displayList = new ArrayList<>();
        for (Account a : cards) {
            displayList.add(new AccountInfo(a));
        }
        return displayList;
    }

    public boolean accountNameAvailable(String nameAccount) {
        for (Account account : cards) {
            if (nameAccount.equalsIgnoreCase(account.getAccountName())) {
                return false; // Se existir retorna falso, portanto o nome não está disponível
            }
        }
        return true; //Se não existir retorna true, portano o nome está disponível
    }

    public List<TransactInfo> getTransactions(int selectedCard){
      Account account = getAccountById(selectedCard);
      if(account == null){ return List.of();}

      return account.getTransactions();
    }

    public List<TransactInfo> getIncomeTransact(int selectedCard) {      Account account = getAccountById(selectedCard);
        if(account == null){ return List.of();}

        return account.getIncomeTransact();
    }

    public List<TransactInfo> getExpenseTransactions(int selectedCard) {      Account account = getAccountById(selectedCard);
        if(account == null){ return List.of();}

        return account.getExpenseTransactions();
    }

    public List<SpendingGoal> getSpendingGoals(int selectCard) {
        Account account = getAccountById(selectCard);
        if(account == null){return null;}

        return account.getSpendingGoals();
    }
}
