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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.Portfolio;
import org.example.gps_g22.model.data.stock.Stock;

import java.io.IOException;
import java.util.List;

public class PortfolioView {

    @FXML
    private Button calculateValorization;

    @FXML
    private Button seeHistory;

    @FXML
    private Button buyStocks;

    @FXML
    private Button sellStocks;

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane anchorRoot;

    //    private Portfolio portfolio = new Portfolio();
    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;

    }

//    public void setPortfolio(Portfolio p) {
//        this.portfolio = p;
//    }

    private ObservableList<String> StockList = FXCollections.observableArrayList();


    @FXML
    private VBox ListaStocks;
    @FXML
    private ListView<String> StockListView;
    @FXML
    private Button previousButton;

    @FXML
    protected void handlePreviousButton(ActionEvent actionEvent) {
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
    protected void updateListViewStocks() {
        StockList.clear();

        ListaStocks.getChildren().clear();
        ListaStocks.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;-fx-min-height: 300;-fx-border-radius: 5;");

        List<String> stocks = manager.showStocks();

        if (stocks.isEmpty()) {
            StockList.add("No stocks");
        } else
            StockList.addAll(stocks);

        StockListView.setItems(StockList);
        ListaStocks.getChildren().add(StockListView);
    }

    @FXML
    protected void handleBuyStocks(ActionEvent actionEvent) {
        System.out.println("Buy Stocks");

        try {
            // Carregar o FXML corretamente
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("buyStock-view.fxml"));

            // Certifique-se de carregar o conteúdo correto do FXML
            GridPane gridPane = fxmlLoader.load();  // GridPane ou o tipo correspondente do layout
            BuyStockView controller = fxmlLoader.getController();
//            controller.setPortfolio(portfolio);
            controller.setManager(manager);

            // Criar o diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Buy Stocks");

            // Configurar o DialogPane com o conteúdo
            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL); // Adicionando o botão de cancelar

            // Exibir o diálogo
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListViewStocks();//dar update à lista
    }

    @FXML
    public void handleSellStocks(ActionEvent actionEvent) {
        System.out.println("Sell Stocks");

        try {
            // Carregar o FXML corretamente
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sellStock-view.fxml"));

            // Certifique-se de carregar o conteúdo correto do FXML
            GridPane gridPane = fxmlLoader.load();  // GridPane ou o tipo correspondente do layout
            SellStockView controller = fxmlLoader.getController();
//            controller.setPortfolio(portfolio);
            controller.setManager(manager);

            // Criar o diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Sell Stocks");

            // Configurar o DialogPane com o conteúdo
            dialog.getDialogPane().setContent(gridPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL); // Adicionando o botão de cancelar

            // Exibir o diálogo
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListViewStocks();//dar update à lista
    }

    @FXML
    protected void handleSeeHistory(ActionEvent actionEvent) {
        System.out.println("Sell Stocks");

        try {
            // Carregar o FXML corretamente
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seeHistory-view.fxml"));

            // Certifique-se de carregar o conteúdo correto do FXML
            VBox vBox = fxmlLoader.load();  // GridPane ou o tipo correspondente do layout
            SeeHistoryView controller = fxmlLoader.getController();
//            controller.setPortfolio(portfolio);
            controller.setManager(manager);
            controller.updateListViewStocks();

            // Criar o diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("See History");

            // Configurar o DialogPane com o conteúdo
            dialog.getDialogPane().setContent(vBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); // Adicionando o botão de cancelar
            // Exibir o diálogo
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListViewStocks();//dar update à lista
    }

    public void handleCalculateValorization(ActionEvent actionEvent) {
        System.out.println("Calculate Valorization");

        try {
            // Carregar o FXML corretamente
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calculateValorization-view.fxml"));

            // Certifique-se de carregar o conteúdo correto do FXML
            VBox vBox = fxmlLoader.load();

            // Criar o diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Calculate Valorization");

            // Configurar o DialogPane com o conteúdo
            dialog.getDialogPane().setContent(vBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); // Adicionando o botão de fechar
            // Exibir o diálogo
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateListViewStocks();//dar update à lista
    }
}
