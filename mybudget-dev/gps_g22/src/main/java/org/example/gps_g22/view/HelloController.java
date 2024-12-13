package org.example.gps_g22.view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.gps_g22.model.Manager;

import java.io.IOException;

public class HelloController {
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private StackPane parentContainer;

    //uma brincadeira para testar
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            loginButton.requestFocus();
        });

            // Criar o círculo de recorte
            Circle clip = new Circle();
            clip.setCenterX(imageView.getFitWidth() / 2);
            clip.setCenterY(imageView.getFitHeight() / 2);
            clip.setRadius(100); // Ajuste o raio conforme necessário

            // Aplicar o clip à ImageView
            imageView.setClip(clip);

    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String linha;
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        boolean validCredentials = false;


        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Username and password must be filled out.");
            return;
        }

        Manager.getInstance();
        if (Manager.userLogin(username, password)) {
            validCredentials = true;
        }

        if (!validCredentials) {
            showAlert("Validation Error", "Username or password does not exist.");
            return;
        }

        Parent root = FXMLLoader.load((getClass().getResource("card-view.fxml")));
        Scene loginScene = loginButton.getScene();

        root.translateYProperty().set(loginScene.getHeight());
        parentContainer.getChildren().add(root);

        // Animação para mover ambas as telas para cima
        Timeline timeline = new Timeline();
        KeyValue keyValue1 = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyValue keyValue2 = new KeyValue(anchorRoot.translateYProperty(), -loginScene.getHeight(), Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue1, keyValue2);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> parentContainer.getChildren().remove(anchorRoot));  // Remove a tela de login após a animação
        timeline.play();

    }


    @FXML
    protected void handleSignUpButton() {
        try {
            // Carrega o FXML da página de Sign Up
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
            Scene signUpScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) signUpButton.getScene().getWindow();  // Obtém a janela atual
            stage.setScene(signUpScene);  // Define a nova cena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onHelloButtonClick(ActionEvent actionEvent) {
    }
}



