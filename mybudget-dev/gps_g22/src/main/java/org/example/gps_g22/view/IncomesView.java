package org.example.gps_g22.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.transaction.TransactionType;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IncomesView {

    @FXML
    private Button orderByButton;
    @FXML
    private Label totalLabel;
    @FXML
    private VBox ListaIncomes;

    @FXML
    private Button previousButton;

    @FXML
    private Button AddIncome;

    @FXML
    public Button RemoveIncome;

    @FXML
    private ListView<TransactInfo> transactionsListIncomeView;

    @FXML
    private ComboBox<AccountInfo> sourceComboBox;

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane anchorRoot;

    private ObservableList<TransactInfo> transactionList = FXCollections.observableArrayList();

    private Manager manager;

    private AccountInfo selectedAccount = null;

    TransactInfo selectTransact = null;


    // Método para definir o Manager
    public void setManager(Manager manager) {
        this.manager = manager;
        this.selectedAccount = manager.getCurrentAccount();
    }

    @FXML
    protected void handlePreviousButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = previousButton.getScene();

            root.translateXProperty().set(-scene.getWidth());
            parentContainer.getChildren().add(root);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyValue kv2 = new KeyValue(anchorRoot.translateXProperty(), scene.getWidth(), Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kv, kv2);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(event -> {
                parentContainer.getChildren().remove(anchorRoot);
            });
            timeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddIncome() {
        System.out.println("add income");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addIncome-view.fxml"));

            GridPane gridPane = fxmlLoader.load();  // GridPane ou o tipo correspondente do layout
            addIncomeView controller = fxmlLoader.getController();
            controller.setManager(manager);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Income");

            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL); // Adicionando o botão de cancelar

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListaIncomes();//dar update à lista
    }

    @FXML
    public void onRemoveIncome(ActionEvent actionEvent) {
        // Implementação do botão de remoção, caso necessário
        TransactInfo selected = transactionsListIncomeView.getSelectionModel().getSelectedItem();

        if (selectTransact == null) {
            ViewsUtils.showAlert("Error", "No Account Selected");
            return;
        }


        if (!ViewsUtils.ConfirmationPopup("Comfirm Deletion", "Are you sure you want to delete this income entry?", selectTransact.toString().replace(',', '\n'))) {
            return;
        }


        manager.removeTransact(manager.getCurrentAccount().getId(), selectTransact.getTransactId(), TransactionType.INCOME);
        updateListaIncomes();
    }

    @FXML
    public boolean onTransactSelect(MouseEvent mouseEvent) {
        TransactInfo selectedTransact = transactionsListIncomeView.getSelectionModel().getSelectedItem();
        if (selectedTransact != null) {
            System.out.println("Transação selecionada: " + selectedTransact);
            this.selectTransact = selectedTransact;
            return true;
        }
        return false;
    }

    @FXML
    protected void updateListaIncomes() {
        String buttonText = orderByButton.getText();
        boolean ascending1 = buttonText.equals("Order by Date ↓");
        boolean ascending2 = buttonText.equals("Order by Amount ↓");

        // Alternar texto do botão para as várias opções
        if (buttonText.equals("Order by Date ↓")) {
            orderByButton.setText("Order by Date ↑");
        } else if (buttonText.equals("Order by Date ↑")) {
            orderByButton.setText("Order by Amount ↓");
        } else if (buttonText.equals("Order by Amount ↓")) {
            orderByButton.setText("Order by Amount ↑");
        } else {
            orderByButton.setText("Order by Date ↓");
        }

        // Limpar a lista
        transactionList.clear();
        double totalValue = 0;
        ListaIncomes.getChildren().clear();
        ListaIncomes.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;-fx-min-height: 300;-fx-border-radius: 5;");

        // Obter transações
        List<TransactInfo> transactions = manager.getIncomeTransact(manager.getCurrentAccount().getId());
        transactionList.addAll(transactions);

        // Ordenar conforme o critério selecionado
        Collections.sort(transactionList, new Comparator<TransactInfo>() {
            @Override
            public int compare(TransactInfo o1, TransactInfo o2) {
                if (orderByButton.getText().equals("Order by Date ↑") || orderByButton.getText().equals("Order by Date ↓")) {
                    // Ordenação por data
                    return ascending1 ? o1.getDateOfTransaction().compareTo(o2.getDateOfTransaction()) : o2.getDateOfTransaction().compareTo(o1.getDateOfTransaction());
                } else if (orderByButton.getText().equals("Order by Amount ↓") || orderByButton.getText().equals("Order by Amount ↑")) {
                    // Ordenação por valor (amount)
                    return ascending2 ? Double.compare(o1.getAmount(), o2.getAmount()) : Double.compare(o2.getAmount(), o1.getAmount());
                }
                return 0;
            }
        });

        // Atualizar ListView
        transactionsListIncomeView.setItems(transactionList);
        ListaIncomes.getChildren().add(transactionsListIncomeView);

        // Calcular total
        for (TransactInfo t : manager.getIncomeTransact(manager.getCurrentAccount().getId())) {
            if (t.getAmount() > 0) {
                totalValue += t.getAmount();
            }
        }

        // Atualizar label de total
        totalLabel.setText(String.format("%.2f", totalValue));  // Exibir com duas casas decimais
        if (totalValue != 0) {
            totalLabel.setTextFill(Color.DARKGREEN);
        } else {
            totalLabel.setTextFill(Color.BLACK);
        }
    }
}
