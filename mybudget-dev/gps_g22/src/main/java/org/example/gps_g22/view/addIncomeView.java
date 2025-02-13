package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.data.transaction.TransactionSource;
import org.example.gps_g22.model.data.transaction.TransactionType;

import java.time.LocalDate;
import java.util.Date;

public class addIncomeView {

    @FXML
    private ComboBox<AccountInfo> sourceComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button saveButton;

    private Manager manager;

    // Set the manager (this will be called by the parent view)
    public void setManager(Manager manager) {
        this.manager = manager;
        // Populating the ComboBox with the accounts from the manager
        //sourceComboBox.getItems().setAll(manager.getCurrentAccount());
        sourceComboBox.setValue(manager.getCurrentAccount());
        sourceComboBox.setDisable(true);
    }

    @FXML
    private void handleSave() {
        // Get the selected account, amount, and date
        AccountInfo selectedAccount = sourceComboBox.getValue();
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

            // Create the TransactionSource using data from selectedAccount
            // Assuming nif is the account number and TransactionType is set to RENT
            TransactionSource source = new TransactionSource(
                    selectedAccount.getName(),
                    selectedAccount.getAccNumber(),
                    TransactionType.INCOME
            );

            // Add the transaction (this should call a method from Manager or another class)
            manager.addTransact(selectedAccount.getId(), source, amount, selectedDate);

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
