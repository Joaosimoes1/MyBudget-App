package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.gps_g22.model.data.SpendingGoal;

public class EditSpendingGoalsView {

    @FXML
    public Button saveButton;
    @FXML
    private TextField goalAmountField;

    @FXML
    private TextField spentAmountField;

    private SpendingGoal spendingGoal;

    public void setSpendingGoal(SpendingGoal spendingGoal) {
        this.spendingGoal = spendingGoal;

        // Preenche os campos com os valores atuais
        goalAmountField.setText(String.valueOf(spendingGoal.getGoalAmount()));
        spentAmountField.setText(String.valueOf(spendingGoal.getSpentAmount()));
    }

    @FXML
    private void handleSave() {
        try {
            // Obtém os novos valores digitados pelo usuário
            double newGoalAmount = Double.parseDouble(goalAmountField.getText());
            double newSpentAmount = Double.parseDouble(spentAmountField.getText());

            // Atualiza os valores do SpendingGoal
            spendingGoal.setGoalAmount(newGoalAmount);
            spendingGoal.setSpentAmount(newSpentAmount);

            if (newSpentAmount > newGoalAmount) {
                showAlert("Warning", "You have exceeded the spending goal!");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for the amounts.");
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
