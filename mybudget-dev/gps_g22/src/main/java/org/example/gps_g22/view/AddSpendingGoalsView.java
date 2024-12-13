package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.SpendingGoal;

public class AddSpendingGoalsView {

    @FXML
    private ComboBox<AccountInfo> sourceComboBox;
    @FXML
    private TextField nameField;
    @FXML
    public TextField amountGoalField;
    @FXML
    private TextField amountSpentField;
    @FXML
    private Button saveButton;

    private Manager manager;



    public void setManager(Manager manager) {
        this.manager = manager;
        sourceComboBox.getItems().setAll(manager.getCurrentAccount());
    }




    @FXML
    private void handleSave() {
        // Get the selected account, amount, and date
        AccountInfo selectedAccount = sourceComboBox.getValue();
        String nameText = nameField.getText().trim();
        String amountGoalText = amountGoalField.getText().trim();
        String amountSpentText = amountSpentField.getText().trim();

        if (selectedAccount == null || nameText.isEmpty() || amountGoalText.isEmpty() || amountSpentText.isEmpty() ) {
            showAlert("Missing Data", "Please fill in all fields.");
            return;
        }

        try {
            double amountGoal = Double.parseDouble(amountGoalText);
            double amountSpent = Double.parseDouble(amountSpentText);

            if (amountSpent > amountGoal) {
                showAlert("Warning", "You have exceeded the spending goal!");
                return;  // Impede a criação do SpendingGoal se ultrapassar a meta
            }

            //Transaction newTransaction = new Transaction(source, -(amount), selectedDate);
            SpendingGoal newSpendingGoal = new SpendingGoal(nameText, amountGoal, amountSpent);

            //manager.addSpendingGoal(selectedAccount.getID(), newSpendingGoal);
            boolean added = manager.addSpendingGoal(selectedAccount.getId(), newSpendingGoal);
            if (added) {
                System.out.println("SpendingGoal adicionada: " + newSpendingGoal);
            } else {
                System.out.println("Falha ao adicionar a SpendingGoal.");
            }


            // Close the dialog after saving
            closeDialog();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Amount must be a valid number.");
        }
    }

    private void closeDialog() {
        ((javafx.stage.Stage) saveButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
