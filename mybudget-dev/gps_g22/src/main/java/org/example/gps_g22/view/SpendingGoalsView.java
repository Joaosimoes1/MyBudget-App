/*
package org.example.gps_g22.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.SpendingGoal;

import java.io.IOException;
import java.util.List;

public class SpendingGoalsView {

    @FXML
    private Button previousButton;
    @FXML
    private Button AddSpendingGoal;

    @FXML
    private Button RemoveSpendingGoal;

    @FXML
    private TableView<SpendingGoal> spendingGoalsTableView;

    @FXML

    private ObservableList<SpendingGoal> spendingGoalsList = FXCollections.observableArrayList();

//    @FXML
//    private TableColumn<SpendingGoal, String> profitColumn;
//
//    @FXML
//    private TableColumn<SpendingGoal, String> costCeilingColumn;

    @FXML
    private TableColumn<SpendingGoal, String> nameColumn;

    @FXML
    private TableColumn<SpendingGoal, Double> goalAmountColumn;

    @FXML
    private TableColumn<SpendingGoal, Double> spentAmountColumn;

    private AccountInfo selectedAccount = null;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
        this.selectedAccount = manager.getCurrentAccount();
    }



    @FXML
    private void handlePreviousButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card-view.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddSpendingGoal() {

        System.out.println("Add Spending Goal");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addSpendingGoals.fxml"));

            GridPane gridPane = loader.load();
            AddSpendingGoalsView controller = loader.getController();
            controller.setManager(manager);


            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Spending Goal");

            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            dialog.showAndWait();

           //spendingGoalsTableView.setItems(FXCollections.observableArrayList(manager.getSpendingGoals(selectedAccount.getID())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveSpendingGoal() {
        SpendingGoal selectedGoal = spendingGoalsTableView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null) {
            manager.removeSpendingGoal(selectedGoal);
            spendingGoalsTableView.setItems(FXCollections.observableArrayList(manager.getSpendingGoals(selectedAccount.getID())));
        } else {
            showAlert("No Selection", "Please select a spending goal to remove.");
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
*/



package org.example.gps_g22.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.gps_g22.dto.AccountInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.SpendingGoal;

import java.io.IOException;
import java.util.List;

public class SpendingGoalsView {

    @FXML
    private StackPane parentContainer;
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private Button previousButton;
    @FXML
    private TableView<SpendingGoal> spendingGoalsTableView;
    @FXML
    private TableColumn<SpendingGoal, String> nameColumn;
    @FXML
    private TableColumn<SpendingGoal, Double> goalAmountColumn;
    @FXML
    private TableColumn<SpendingGoal, Double> spentAmountColumn;

    private ObservableList<SpendingGoal> spendingGoalsList = FXCollections.observableArrayList();
    private AccountInfo selectedAccount = null;
    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
        this.selectedAccount = manager.getCurrentAccount();
        initializeTable();
        loadSpendingGoals();
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        goalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("goalAmount"));
        spentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("spentAmount"));
        spendingGoalsTableView.setItems(spendingGoalsList);
    }

    private void initializeTable() {
        if (manager != null && selectedAccount != null) {
            List<SpendingGoal> goals = manager.getSpendingGoals(selectedAccount.getId());
            spendingGoalsList.setAll(goals);
        }
    }

    private void loadSpendingGoals() {
        if (manager != null && selectedAccount != null) {
            spendingGoalsList.setAll(manager.getSpendingGoals(selectedAccount.getId()));
        }
    }

    @FXML
    private void handlePreviousButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = previousButton.getScene();

            root.translateXProperty().set(-scene.getWidth());
            parentContainer.getChildren().add(root);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyValue kv2 = new KeyValue(anchorRoot.translateXProperty(), scene.getWidth(), Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kv,kv2);
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
    private void handleAddSpendingGoal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addSpendingGoals.fxml"));
            GridPane gridPane = loader.load();
            AddSpendingGoalsView controller = loader.getController();
            controller.setManager(manager);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Spending Goal");

            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            dialog.showAndWait();

            loadSpendingGoals();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveSpendingGoal() {
        SpendingGoal selectedGoal = spendingGoalsTableView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null) {
            manager.removeSpendingGoal(selectedAccount.getId(), selectedGoal);
            loadSpendingGoals(); // Atualiza os dados da tabela
        } else {
            showAlert("No Selection", "Please select a spending goal to remove.");
        }
    }

    @FXML
    private void handleEditSpendingGoal() {
        SpendingGoal selectedGoal = spendingGoalsTableView.getSelectionModel().getSelectedItem();
        if (selectedGoal == null) {
            showAlert("No Selection", "Please select a spending goal to edit.");
            return;
        }

        try {
            // Carrega o layout do diálogo de edição
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editSpendingGoals.fxml"));
            GridPane gridPane = loader.load();

            // Passa a SpendingGoal selecionada para o controlador de edição
            EditSpendingGoalsView controller = loader.getController();
            controller.setSpendingGoal(selectedGoal);

            // Cria o diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Spending Goal");
            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Atualiza a tabela com os novos valores
                    spendingGoalsTableView.refresh();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



