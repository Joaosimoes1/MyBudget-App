package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.Manager;

import java.time.LocalDate;
import java.util.Date;

public class SellStockView {
    @FXML
    private Button saveButton;
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

    public void setManager(Manager manager) {
        this.manager = manager;
        sourceComboBox.getItems().setAll(manager.getAccounts());
    }

//    public void setPortfolio(Portfolio p) {
//        portfolio = p;
//    }

    @FXML
    private void handleSave() {
        // Get the selected account, amount, and date
        AccountInfo selectedAccount = (AccountInfo) sourceComboBox.getValue();
        String nameText = nameField.getText().trim();
        String amountText = amountField.getText().trim();
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

            // sell the stock to the portfolio
            manager.sellStock(nameText, amount, selectedDate);

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
