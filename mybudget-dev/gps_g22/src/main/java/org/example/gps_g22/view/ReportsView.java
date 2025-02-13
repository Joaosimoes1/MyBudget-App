package org.example.gps_g22.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.stock.Stock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.example.gps_g22.view.ViewsUtils.showAlert;

public class ReportsView {

    @FXML
    private ComboBox typeReportComboBox;
    @FXML
    private ComboBox typeTransactionComboBox;
    @FXML
    private ComboBox typeStockComboBox;
    @FXML
    private ComboBox yearComboBox;
    @FXML
    private ComboBox monthComboBox;
    @FXML
    private ComboBox timeFrameComboBox;
    @FXML
    private Button previousButton;

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    public void initialize() {
        // Inicializando as ComboBoxes com base em interações do usuário
        typeTransactionComboBox.setDisable(true);
        typeTransactionComboBox.setValue("");
        typeStockComboBox.setDisable(true);
        typeStockComboBox.setValue("");
        timeFrameComboBox.setDisable(true);
        timeFrameComboBox.setValue("");
        monthComboBox.setDisable(true);
        monthComboBox.setValue("");
        yearComboBox.setDisable(true);
        yearComboBox.setValue("");

        // Listener para habilitar timeFrameComboBox quando reportComboBox for selecionado
        typeReportComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            typeTransactionComboBox.setDisable(newValue == null || newValue.toString().isEmpty() || !newValue.toString().equals("Transactions"));
            typeStockComboBox.setDisable(newValue == null || newValue.toString().isEmpty() || !newValue.toString().equals("Stocks"));
            timeFrameComboBox.setDisable(newValue == null || newValue.toString().isEmpty());
        });

        // Listener para habilitar monthComboBox quando timeFrameComboBox for selecionado
        timeFrameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            monthComboBox.setDisable(newValue == null || newValue.toString().isEmpty() || !newValue.toString().equals("Monthly"));
            yearComboBox.setDisable(newValue == null || newValue.toString().isEmpty());
        });
    }

    private Manager manager = Manager.getInstance();

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

    public boolean verifyParameters() {
        String typeReport = (String) typeReportComboBox.getValue();
        if (typeReport == null || typeReport.isEmpty()) {
            showAlert("Missing Data", "Please fill in all fields.");
            return false;
        }

        if (typeReport.equals("Stocks")) {
            typeReport = (String) typeStockComboBox.getValue();
        } else {
            typeReport = (String) typeTransactionComboBox.getValue();
        }

        if (typeReport == null || typeReport.isEmpty()) {
            showAlert("Missing Data", "Please fill in all fields.");
            return false;
        }

        String timeFrame = (String) timeFrameComboBox.getValue();
        if (timeFrame == null || timeFrame.isEmpty()) {
            showAlert("Missing Data", "Please fill in all fields.");
            return false;
        }

        String year = (String) yearComboBox.getValue();
        if (year == null || year.isEmpty()) {
            showAlert("Missing Data", "Please fill in all fields.");
            return false;
        }
        if (timeFrame.equals("Monthly")) {
            String month = (String) monthComboBox.getValue();
            if (month == null || month.isEmpty()) {
                showAlert("Missing Data", "Please fill in all fields.");
                return false;
            }
        }

        return true;
    }

    @FXML
    protected void downloadPDF() {
        if (!verifyParameters()) {
            return;
        }
        List<Object> reportsList = filterParameter(); // <Stock> ou <TransactInfo>
        if (reportsList.isEmpty()) {
            if (reportsList.isEmpty()) {
                showAlert("NOT FOUND", "No records found to do the report.");
                return;
            }
            return;
        }
        showResultDialog(makePDF(reportsList), "PDF");
    }

    private boolean makePDF(List<Object> reportsList) {
        // Adicionando título ao PDF
        String typeReport = typeReportComboBox.getValue().toString(); // Determina o tipo de relatório

        // Adicionando a legenda sobre o timeframe
        String timeFrame = timeFrameComboBox.getValue().toString();
        String timeFrameText = "Time Frame: ";
        if ("Monthly".equalsIgnoreCase(timeFrame)) {
            String month = monthComboBox.getValue().toString();
            String year = yearComboBox.getValue().toString();
            timeFrameText += month + " " + year;
        } else {
            String year = yearComboBox.getValue().toString();
            timeFrameText += year;
        }

        return manager.saveReportAsPDF(reportsList, typeReport, timeFrameText);
    }


    public List<Object> filterParameter() {

        String typeReport = typeReportComboBox.getValue().toString();
        String typeTransaction = typeTransactionComboBox.getValue().toString();
        String typeStock = typeStockComboBox.getValue().toString();
        String year = yearComboBox.getValue().toString();
        String month = monthComboBox.getValue().toString();
        String timeFrame = timeFrameComboBox.getValue().toString();

        //for stocks or transactions
        List<Object> reportsList = new ArrayList<Object>();


        if (typeReport.equals("Stocks")) {
            //Armazena na lista report os dados do tipo do stock pretendido
            List<Stock> stockHistoryList = new ArrayList<>();

            if (typeStock.equals("Ordinaries")) {
                stockHistoryList = manager.getOrdinariesHistoryList();
            } else if (typeStock.equals("Preferred")) {
                stockHistoryList = manager.getPreferredHistoryList();
            } else if (typeStock.equals("Sale")) {
                stockHistoryList = manager.getSaleHistoryList();
            } else if (typeStock.equals("All")) {
                stockHistoryList = manager.getStockHistoryList();
            }

            String monthInt = "-1";
            if (!month.isEmpty()) {
                monthInt = mesParaInt(month);
            }

            stockHistoryList = filterStocks(stockHistoryList, year, monthInt, timeFrame);

            reportsList.addAll(stockHistoryList);

        } else if (typeReport.equals("Transactions")) {
            //Armazena na lista report os dados do tipo de transação pretendido
            List<TransactInfo> transactionHistoryList = new ArrayList<>();
            if (typeTransaction.equals("All")) {
                transactionHistoryList = manager.getTransactions(manager.getCurrentAccount().getId());
            } else if (typeTransaction.equals("Income")) {
                transactionHistoryList = manager.getIncomeTransact(manager.getCurrentAccount().getId());
            } else if (typeTransaction.equals("Expense")) {
                transactionHistoryList = manager.getExpenseTransactions(manager.getCurrentAccount().getId());
            }

            String monthInt = "-1";
            if (!month.isEmpty()) {
                monthInt = mesParaInt(month);
            }

            transactionHistoryList = filterTransactions(transactionHistoryList, year, monthInt, timeFrame);
            reportsList.addAll(transactionHistoryList);
        }

        for (Object report : reportsList) {
            System.out.println(report.toString());
        }

        return reportsList;
    }

    private List<TransactInfo> filterTransactions(List<TransactInfo> transactionHistoryList, String year, String month, String timeFrame) {
        String filterTimeFrame = year;
        List<TransactInfo> transactionList = new ArrayList<>();
        if (timeFrame.equals("Monthly")) {
            filterTimeFrame = year + "-" + month;
        }
        for (TransactInfo t : transactionHistoryList) {
            if (t.getDateOfTransaction().toString().startsWith(filterTimeFrame)) {
                transactionList.add(t);
            }
        }
        return transactionList;
    }

    private List<Stock> filterStocks(List<Stock> stockHistoryList, String year, String month, String timeFrame) {
        String filterTimeFrame = year;
        List<Stock> stockList = new ArrayList<>();
        if (timeFrame.equals("Monthly")) {
            filterTimeFrame = year + "-" + month;
        }
        for (Stock stock : stockHistoryList) {
            if (stock.getDate().toString().startsWith(filterTimeFrame)) {
                stockList.add(stock);
            }
        }
        return stockList;
    }

    public static String mesParaInt(String mes) {
        switch (mes.toLowerCase()) {
            case "january":
                return "1";
            case "february":
                return "2";
            case "march":
                return "3";
            case "april":
                return "4";
            case "may":
                return "5";
            case "june":
                return "6";
            case "july":
                return "7";
            case "august":
                return "8";
            case "september":
                return "9";
            case "october":
                return "10";
            case "november":
                return "11";
            case "december":
                return "12";
            default:
                throw new IllegalArgumentException("Mês inválido: " + mes);
        }
    }


    @FXML
    protected void downloadCSV() {
        if (!verifyParameters()) {
            return;
        }
        List<Object> reportsList = filterParameter(); // <Stock> ou <TransactInfo>
        if (reportsList.isEmpty()) {
            showAlert("NOT FOUND", "No records found to do the report.");
            return;
        }
        showResultDialog(makeCSV(reportsList), "CSV");
    }

    private boolean makeCSV(List<Object> reportsList) {

            String typeReport = typeReportComboBox.getValue().toString(); // Determina o tipo de relatório

            // Adicionando a legenda sobre o timeframe
            String timeFrame = timeFrameComboBox.getValue().toString();
            String timeFrameText = "Time Frame: ";
            if ("Monthly".equalsIgnoreCase(timeFrame)) {
                String month = monthComboBox.getValue().toString();
                String year = yearComboBox.getValue().toString();
                timeFrameText += month + " " + year;
            } else {
                String year = yearComboBox.getValue().toString();
                timeFrameText += year;
            }

          return manager.saveReportAsCSV(reportsList, typeReport, timeFrameText);
    }

    private void showResultDialog(boolean isSuccess, String fileType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Definindo o título do alerta
        alert.setTitle("Operation Result");

        if (isSuccess) {
            // Mensagem de sucesso
            alert.setHeaderText(fileType + " created successfully!");
            alert.setContentText("The " + fileType + " has been created successfully and saved in your Downloads folder.");
        } else {
            // Mensagem de erro
            alert.setHeaderText("Error creating " + fileType);
            alert.setContentText("There was an error while creating the " + fileType + ". Please try again.");
        }

        // Exibe o alerta e aguarda a resposta do usuário
        alert.showAndWait();
    }
}
