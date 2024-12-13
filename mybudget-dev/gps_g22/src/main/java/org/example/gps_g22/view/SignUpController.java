package org.example.gps_g22.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.gps_g22.model.Manager;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button backButton;

    @FXML
    protected void handleBackButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();  // Obtém a janela atual
            stage.setScene(loginScene);  // Define a nova cena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSignUpAction() {
        String linha;
        String username = fullNameField.getText().trim();
        String password = passwordField.getText().trim();
        boolean validCredentials = false;

        if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Validation Error", "Passwords do not match.");
            return;
        }

        Manager.getInstance();
        boolean userExists = Manager.userCreate(username, password, emailField.getText().trim());
        if (!userExists) {
            showAlert("Validation Error", "Username already exists.");
        }
//        if (!userExists) {
//            //User user = new User(username, password);
//            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
//                bw.write(username + "," + password);
//                bw.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//                showAlert("Error", "Could not save user data.");
//                return;
//            }

        showMsg("Sign Up Successful", "You have successfully signed up!");
        redirecionaLogin();

    }

    private void redirecionaLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMsg(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
