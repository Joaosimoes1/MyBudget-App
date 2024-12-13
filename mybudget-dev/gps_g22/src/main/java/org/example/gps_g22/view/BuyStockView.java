package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.stock.StockType;

import java.time.LocalDate;
import java.util.Date;

public class BuyStockView {
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> TypeComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox sourceComboBox;


    private Manager manager;
//    private Portfolio portfolio;

    @FXML
    public void initialize() {
        TypeComboBox.getItems().addAll("ORDINARIES", "PREFERRED");
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        sourceComboBox.getItems().setAll(manager.getAccounts());
    }

//    public void setPortfolio(Portfolio p) {
//        portfolio = p;
//        TypeComboBox.getItems().addAll("ORDINARIES","PREFERRED");
//    }

    @FXML
    private void handleSave() {
        // Get the selected account, amount, and date
        AccountInfo selectedAccount = (AccountInfo) sourceComboBox.getValue();
        String nameText = nameField.getText().trim();
        String amountText = amountField.getText().trim();
        String priceText = priceField.getText().trim();
        String type = (String) TypeComboBox.getValue();
        LocalDate localDate = datePicker.getValue();
        Date selectedDate;
        if (localDate != null) {
            selectedDate = java.sql.Date.valueOf(localDate);
        } else {
            showAlert("Missing Data", "Please fill in all fields.");
            return;
        }

        if (selectedAccount == null || amountText.isEmpty()) {
            showAlert("Missing Data", "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            double price = Double.parseDouble(priceText);
            StockType stockType = StockType.valueOf(type.toUpperCase());

            // buy the stock to the portfolio
//            portfolio.buyStock(amount, nameText, price, stockType,selectedDate);
            manager.buyStock(amount, nameText, price, stockType, selectedDate);

            // Close the dialog after saving
            closeDialog();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Amount must be a valid number.");
        }
    }

    private void closeDialog() {
        // Code to close the dialog window
        ((javafx.stage.Stage) saveButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
