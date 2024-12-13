package org.example.gps_g22.model;


import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.commands.*;
import org.example.gps_g22.model.data.DataModel;
import org.example.gps_g22.model.data.ReportGenerator;
import org.example.gps_g22.model.data.SpendingGoal;
import org.example.gps_g22.model.data.stock.Stock;
import org.example.gps_g22.model.data.stock.StockType;
import org.example.gps_g22.model.data.transaction.TransactionSource;
import org.example.gps_g22.model.data.transaction.TransactionType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manager {

    //TODO move file to resources
//    private static final String pathUserFile = "gps_g22/src/main/java/org/example/gps_g22/view/users.txt";
    private static final String pathUserFile = "users.txt";
    private final PropertyChangeSupport pcs;

    private DataModel data;
    private CommandManager cmdManager;

    private static Manager instance;
    private final File saveFile;

    private AccountInfo currentAccount = null;

    private Manager(String userName, String password) {
        data = new DataModel(userName, password);
        cmdManager = new CommandManager();
        saveFile = new File(userName);
        pcs = new PropertyChangeSupport(this);
    }

    private Manager(String username) {
        load(getSaveFileName(username));
        cmdManager = new CommandManager();
        this.saveFile = new File(getSaveFileName(username));
        pcs = new PropertyChangeSupport(this);
    }

    public DataModel getData() {
        return data;
    }

    public static Manager getInstance() {
        return instance;
    }

    public static void logOut() {
        if (instance != null) {
            instance.updateSaveFile();
            instance = null;
        }
    }

    public static boolean userLogin(String userName, String password) {
        File credentials = new File(pathUserFile);

        if(!credentials.exists()){
            try {
                credentials.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        String linha;

        String storedPassword = null;

        try (BufferedReader br = new BufferedReader(new FileReader(credentials))) {
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                String existingUsername = partes[0].trim();
                if (userName.equals(existingUsername)) {
                    storedPassword = partes[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (storedPassword == null || !storedPassword.trim().equalsIgnoreCase(password)) {
            return false;
        }

        File save = new File(getSaveFileName(userName));

        if (save.exists()) {
            instance = new Manager(userName);
        } else {
            System.out.println("Save not found");
            instance = new Manager(userName, password);
        }

        return true;

    }

    public static boolean userCreate(String username, String password, String email) {
        File credentials = new File(pathUserFile);

        if (!credentials.exists()) {
            try {
                credentials.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        boolean userExists = false;
        String linha;

        try (BufferedReader br = new BufferedReader(new FileReader(credentials))) {
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                String existingUsername = partes[0].trim();
                if (username.equals(existingUsername)) {

                    userExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (userExists) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(credentials, true))) {
            bw.write(username + "," + password);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        instance = new Manager(username, password);
        instance.saveState(getSaveFileName(instance.data.getCurrentUser().getName()));

        return true;
    }

    public void setCurrentAccount(AccountInfo currentAccount) {
        this.currentAccount = currentAccount;
    }

    public void removeSpendingGoal(SpendingGoal selectedGoal) {

    }

    public boolean addSpendingGoal(int accountId, SpendingGoal selectedGoal) {
        /*cmdManager.invokeCommand(new AddSpendingGoalCommand(data, accountId, selectedGoal.getName(), selectedGoal.getGoalAmount(), selectedGoal.getSpentAmount()));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);*/

        boolean success = data.addSpendingGoal(selectedGoal, accountId);
        if (success) {
            System.out.println("Meta de gastos adicionada com sucesso: " + selectedGoal);
        } else {
            System.out.println("Falha ao adicionar meta de gastos.");
        }

        updateSaveFile();
        return success;
    }

    public void removeSpendingGoal(int accountId, SpendingGoal selectedGoal) {
        cmdManager.invokeCommand(new RemoveSpendingGoalCommand(data, accountId, selectedGoal.getId()));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);
    }


    public void addTransact(int accountId, TransactionSource source, double ammount,
                            Date dateOfTransaction) {
        cmdManager.invokeCommand(new AddTransactionCommand(data, accountId, source, ammount, dateOfTransaction));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);
    }

    public void removeTransact(int accountId, int transactId, TransactionType type) {
        cmdManager.invokeCommand(new RemoveTransactionCommand(data, transactId, accountId, type));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);
    }

    public String addCard(String accountName, long cardNum, int ano, int m) {
        String valLength = String.valueOf(cardNum);

        if (valLength.length() != 16) {
            return "Numero de cartao invalido, o numero do cartao tem que ter 16 algarismos";
        }

        if ((LocalDate.now().getYear() > ano && (m < 1 || m > 12)) ||
                (LocalDate.now().getYear() == ano && LocalDate.now().getMonth().getValue() > m)) {
            return "O seu cartao ja expirou impossivel adicionar";
        }

        cmdManager.invokeCommand(new AddCardCommand(data, accountName, cardNum, m, ano));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);

        return "Cartao adicionado com sucesso";
    }

    public boolean nameAccountExists(String nameAccount) {
        return data.accoutNameAvailable(nameAccount);
    }

    public void removeCard(int accountId) {
        cmdManager.invokeCommand(new RemoveCardCommand(data, accountId));
        updateSaveFile();
        pcs.firePropertyChange(null, null, null);
    }

    // Método auxiliar para ler o arquivo de usuários
    private static List<String> readUserFile() {
        List<String> lines = new ArrayList<>();
        File credentials = new File(pathUserFile);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(credentials))) {
            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo 'users.txt' não encontrado.");
            return List.of();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo 'users.txt'.");
            return List.of();
        }
        return lines;
    }

    private static String getSaveFileName(String username) {
        return username + "_data.ser";
    }

    private boolean updateSaveFile() {
        return saveState(getSaveFileName(data.getCurrentUser().getName()));
    }

    // Salvando o estado do DataModel
    public boolean saveState(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this.data);
        } catch (Exception e) {
            System.err.println("Error writing saving to file");
            return false;
        }
        return true;

    }

    private boolean load(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.err.println("File doesn't exist");
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            data = (DataModel) ois.readObject();
            cmdManager = new CommandManager();
        } catch (Exception e) {
            System.err.println("Error loading File");
            return false;
        }
        return true;
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public List<AccountInfo> getAccounts() {
        return data.getAccounts();
    }

    public AccountInfo getCurrentAccount() {
        return currentAccount;
    }

    public List<TransactInfo> getTransactions(int selectedCard) {
        return data.getTransactions(selectedCard);
    }

    public List<TransactInfo> getTransactions(int selectedCard, int maxItems) {
        return data.getTransactions(selectedCard, maxItems);
    }

    public List<TransactInfo> getIncomeTransact(int selectedCard) {
        return data.getIncomeTransact(selectedCard);
    }

    public List<TransactInfo> getExpenseTransactions(int selectedCard) {
        return data.getExpenseTransactions(selectedCard);
    }

    public List<TransactInfo> getIncomeTransact(int selectedCard, String orderBy) throws IllegalArgumentException {
        return data.getIncomeTransact(selectedCard, orderBy);
    }

    public List<TransactInfo> getExpenseTransactions(int selectedCard, String orderBy) throws IllegalArgumentException {
        return data.getExpenseTransactions(selectedCard, orderBy);
    }

    public List<SpendingGoal> getSpendingGoals(int selectedCard) {

        return data.getSpendingGoal(selectedCard);
    }

    public boolean saveReportAsPDF(List<Object> reportsList, String typeReport,  String timeFrameText) {
        return ReportGenerator.downloadPDF(reportsList, typeReport,  timeFrameText);
    }

    public boolean saveReportAsCSV(List<Object> reportsList, String typeReport,  String timeFrameText) {
       return ReportGenerator.downloadCSV(reportsList, typeReport,  timeFrameText);
    }

    public void buyStock(double amount, String nameText, double price, StockType stockType, Date selectedDate) {
        data.buyStock(amount, nameText, price, stockType, selectedDate);
        updateSaveFile();
    }

    public List<String> showStocks() {
        return data.showStocks();
    }

    public void sellStock(String nameText, double amount, Date selectedDate) {
        data.sellStock(nameText, amount, selectedDate);
    }

    public List<Stock> getStockHistoryList() {
        return data.getStockHistoryList();
    }

    public List<Stock> getOrdinariesHistoryList() {
        return data.getOrdinariesHistoryList();
    }

    public List<Stock> getPreferredHistoryList() {
        return data.getPreferredHistoryList();
    }

    public List<Stock> getSaleHistoryList() {
        return data.getSaleHistoryList();
    }
}
