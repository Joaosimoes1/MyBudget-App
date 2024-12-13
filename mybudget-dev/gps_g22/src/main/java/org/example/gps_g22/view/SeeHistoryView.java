package org.example.gps_g22.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.example.gps_g22.model.Manager;
import org.example.gps_g22.model.data.Portfolio;

public class SeeHistoryView {
    @FXML
    private ComboBox ShowbyCategoryComboBox;
    @FXML
    private ListView stringListView;
    private Manager manager;
//    private Portfolio portfolio;

    public void setManager(Manager manager) {
        this.manager = manager;
        ShowbyCategoryComboBox.setValue("ALL");
    }
//
//    public void setPortfolio(Portfolio p) {
//        portfolio = p;
//    }

    @FXML
    protected void updateListViewStocks() {
        stringListView.getItems().clear();
        String category = (String) ShowbyCategoryComboBox.getValue();
        if (category.equals("ORDINARIES")) {
            stringListView.getItems().addAll(manager.getOrdinariesHistoryList());
        } else if (category.equals("PREFERRED")) {
            stringListView.getItems().addAll(manager.getPreferredHistoryList());
        } else if (category.equals("SALE")) {
            stringListView.getItems().addAll(manager.getSaleHistoryList());
        } else if (category.equals("ALL"))
            stringListView.getItems().addAll(manager.getStockHistoryList());
    }

    public void handleDisplayBy(ActionEvent actionEvent) {
        updateListViewStocks();
    }
}
