package org.example.gps_g22.view;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.Portfolio;

import java.io.IOException;
import java.util.List;

public class CardController {


    //---------------------------------------------------------------//
    //--------------------------LADO ESQUERDO-------------------------//
    //---------------------------------------------------------------//

    @FXML
    private VBox vboxRight;

    // Labels do lado direito
    @FXML
    private Label bankNameLabel;

    @FXML
    private Label accountNumberLabel;

    @FXML
    private Label cardHolderLabel;

    @FXML
    private Label expiryDateLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expensesLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addAccountButton;

    @FXML
    private Button removeAccountButton;

    @FXML
    private Button InvestementButton;

    @FXML
    private Button BudgetButton;

    @FXML
    private Button IncomeButton;

    @FXML
    private Button ExpensesButton;

    @FXML
    private Button ReportsButton;

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane anchorRoot;


    //------------INFO CARD------------//
    @FXML
    private TextField accountNameField;

    @FXML
    private TextField accountNumberField;

    @FXML
    private TextField accountYearField;

    @FXML
    private TextField accountMonthField;


    //---------------------------------//

    @FXML
    private ListView<AccountInfo> accountListView;

    private ObservableList<AccountInfo> accountList = FXCollections.observableArrayList();


    //---------------------------------------------------------------//
    //---------------------------LADO DIREITO------------------------//
    //---------------------------------------------------------------//

    @FXML
    private ListView<TransactInfo> transactionListView;

    private ObservableList<TransactInfo> transactionList = FXCollections.observableArrayList();

    private AccountInfo selectedAccount = null;

    private Manager manager = Manager.getInstance();

    private Portfolio portfolio = new Portfolio();

    //---------------------------------------------------------------//
    //---------------------------------------------------------------//
    //---------------------------------------------------------------//

    @FXML
    public void initialize() {
        loadAccounts();
    }

    private void loadAccounts() {
        accountList.clear();
        updateAccountList();
    }

    private void updateAccountList() {
        accountList.clear();
        accountList.addAll(manager.getAccounts());
        accountNameField.clear();
        accountListView.setItems(accountList);
        if (!accountList.isEmpty() && selectedAccount == null) {
            //select first by default
            manager.setCurrentAccount(accountList.get(0));
            selectedAccount = manager.getCurrentAccount();
        }
    }

    @FXML
    protected void onLogoutClick() throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("login-view.fxml")));
        Scene mainScene = logoutButton.getScene();

        root.translateYProperty().set(-mainScene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue1 = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyValue keyValue2 = new KeyValue(anchorRoot.translateYProperty(), mainScene.getHeight(), Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue1, keyValue2);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> parentContainer.getChildren().remove(anchorRoot));
        timeline.play();


    }


    //Metodo para adicionar contas
    @FXML
    protected void onSelectAddAccount() {
        boolean nameValid = true;
        String newAccountName = accountNameField.getText().trim();
        String newAccountNumber = accountNumberField.getText().trim();
        String newAccountYear = accountYearField.getText().trim();
        String newAccountMonth = accountMonthField.getText().trim();

        if (manager.nameAccountExists(newAccountName)) {
            if (!newAccountName.isEmpty() || !newAccountNumber.isEmpty() || !newAccountYear.isEmpty() || !newAccountMonth.isEmpty()) {
                long AccountNumber = Long.parseLong(accountNumberField.getText().trim());
                int AccountYear = Integer.parseInt(accountYearField.getText().trim());
                int AccountMonth = Integer.parseInt(accountMonthField.getText().trim());
                if ((manager.addCard(newAccountName, AccountNumber, AccountYear, AccountMonth).equals("Cartao adicionado com sucesso"))) {
                    updateAccountList();
                    accountNameField.clear();
                    accountNumberField.clear();
                    accountYearField.clear();
                    accountMonthField.clear();
                    System.out.println("Conta adicionada: " + newAccountName);
                } else {
                    showAlert("Validation Error", "Invalid card data");
                }
            } else {
                showAlert("Validation Error", "Name card can`t be empty");
            }
        } else {
            showAlert("Validation Error", "Name card already exists");
        }
    }

    @FXML
    protected void onSelectRemoveAccount() {
        // Verifica se há uma conta selecionada
        AccountInfo selectedAccount = accountListView.getSelectionModel().getSelectedItem();

        if (selectedAccount != null) {
            // Remove a conta do gerenciador e da lista observável

            manager.removeCard(selectedAccount.getId());
            boolean removed = true;
            accountList.remove(selectedAccount);

            clearAccountDetails();
            showAlert("Account Removed", "The account has been removed successfully.");
        } else {
            showAlert("No Account Selected", "Please select an account to remove.");
        }
    }

    private void clearAccountDetails() {
        bankNameLabel.setText("Bank Name: -");
        accountNumberLabel.setText("Account Number: -");
        cardHolderLabel.setText("Card Holder: -");
        expiryDateLabel.setText("Expiry Date: -");
    }

    //Metodo para selecionar conta
    @FXML
    protected boolean onAccountSelect() {
        AccountInfo selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            System.out.println("Conta selecionada: " + selectedAccount);
            this.selectedAccount = selectedAccount;
            manager.setCurrentAccount(selectedAccount);
            showAccountDetails(selectedAccount);
            loadTransactions(selectedAccount);
            return true;
        }
        return false;
    }

    private void showAccountDetails(AccountInfo accountSelected) {
        // Atualizamos as labels com os dados da conta selecionada
        bankNameLabel.setText("Bank Name: World Bank");
        accountNumberLabel.setText("Number: " + selectedAccount.getAccNumber());
        cardHolderLabel.setText("Card Holder: " + manager.getData().getCurrentUser().getName());
        expiryDateLabel.setText("Expiry Date: " + selectedAccount.getValidade());

        // Atualize outras informações conforme necessário
        System.out.println("Conta selecionada: " + selectedAccount);
    }

    private void loadTransactions(AccountInfo accountSelected) {
        transactionList.clear();

        List<TransactInfo> transactions = manager.getTransactions(accountSelected.getId(), 10);
        transactionList.addAll(transactions);

        transactionListView.setItems(transactionList);
    }

    //Cash Flow statement
    @FXML
    protected void onSelectIncomeMoreInfo() {
        if (onAccountSelect()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("incomes-view.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = IncomeButton.getScene();

                root.translateXProperty().set(scene.getWidth());
                parentContainer.getChildren().add(root);

                IncomesView incomesViewManager = fxmlLoader.getController();
                incomesViewManager.setManager(manager);
                incomesViewManager.updateListaIncomes();

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event -> {
                    parentContainer.getChildren().remove(anchorRoot);
                });
                timeline.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Account Selected", "Please select an account to more info.");
        }
    }

    @FXML
    protected void onSelectExpensesMoreInfo() {
        if (onAccountSelect()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("expenses-view.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = ExpensesButton.getScene();

                root.translateXProperty().set(scene.getWidth());
                parentContainer.getChildren().add(root);

                ExpensesView expensesViewManager = fxmlLoader.getController();
                expensesViewManager.setManager(manager);
                expensesViewManager.updateListaExpenses();

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event -> {
                    parentContainer.getChildren().remove(anchorRoot);
                });
                timeline.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Account Selected", "Please select an account to more info.");
        }
    }

    //Accounts details
    @FXML
    protected void onSelectInvestManagement() {
        if (onAccountSelect()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("portfolio-view.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = InvestementButton.getScene();

                root.translateXProperty().set(scene.getWidth());
                parentContainer.getChildren().add(root);

                PortfolioView portfolioViewManager = fxmlLoader.getController();
                portfolioViewManager.setManager(manager);
                portfolioViewManager.updateListViewStocks();

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event -> {
                    parentContainer.getChildren().remove(anchorRoot);
                });
                timeline.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Account Selected", "Please select an account to more info.");
        }
    }

    @FXML
    protected void onSelectBudgetManagemenent() {
       if (onAccountSelect()) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SpendingGoals-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = BudgetButton.getScene();

            root.translateXProperty().set(scene.getWidth());
            parentContainer.getChildren().add(root);

            SpendingGoalsView spendingGoalsViewManager = fxmlLoader.getController();
            spendingGoalsViewManager.setManager(manager);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(event -> {
                parentContainer.getChildren().remove(anchorRoot);
            });
            timeline.play();


        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Budget Management view.");
        }
       } else {
           showAlert("No Account Selected", "Please select an account to more info.");
       }
    }

    @FXML
    protected void onSelectReports() {
        if (onAccountSelect()) {
            System.out.println("Reports selecionado");
            try {

                Parent root = FXMLLoader.load((getClass().getResource("reports-view.fxml")));
                Scene cardScene = ReportsButton.getScene();

                root.translateXProperty().set(cardScene.getWidth());
                parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event ->
                        parentContainer.getChildren().remove(anchorRoot));
                timeline.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Account Selected", "Please select an account to more info.");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
