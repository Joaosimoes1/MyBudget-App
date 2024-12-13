package org.example.gps_g22;

import org.example.gps_g22.model.data.Portfolio;
import org.example.gps_g22.model.data.stock.StockType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioTest {


    Portfolio portfolio;

    @BeforeEach
    public void setup() {
        portfolio = new Portfolio();


    }

    @Test
    public void testPortfolio() {
        //Arrange
        double amount = 1000;
        String nameText = "StuckMarket";
        double price = 100;
        StockType stockType = StockType.SALE;
        Date selectedDate = new Date();

        //Act
        //buyStock(double amount, String nameText, double price, StockType stockType, Date selectedDate) {
        portfolio.buyStock(amount, nameText, price, stockType, selectedDate);
        portfolio.buyStock(amount * 5, nameText, price, stockType, selectedDate);
        portfolio.buyStock(amount, nameText, price * 2, stockType, selectedDate);

        List<String> stocks = portfolio.showStocks();
        //ASSERT
        assertEquals(1, stocks.size());
        assertEquals(amount * 7, portfolio.getAmountStockByName(nameText));

        //public void sellStock(String nameText, double amount, Date selectedDate)
        portfolio.sellStock(nameText, amount * 3, selectedDate);
        assertEquals(amount * 4, portfolio.getAmountStockByName(nameText));
    }
}
