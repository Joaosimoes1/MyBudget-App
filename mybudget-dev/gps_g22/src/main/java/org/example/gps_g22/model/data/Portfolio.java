package org.example.gps_g22.model.data;

import org.example.gps_g22.model.data.stock.Stock;
import org.example.gps_g22.model.data.stock.StockType;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Portfolio implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //Historico de compras e vendas de stocks
    private List<Stock> stockHistoryList = new ArrayList<Stock>();
    //Montantes de stocks
    private Map<String, Double> stocks = new HashMap<String, Double>();

    private int nextId = 0;

    public int getNextId() {
        return nextId;
    }


    public List<Stock> getStockHistoryList() {
        return stockHistoryList;
    }

    public String showHistoryByType(String type) {
        StringBuilder result = new StringBuilder();
        for (Stock stock : stockHistoryList) {
            if (stock.getType().equals(type)) {
                result.append(stock.toString()).append("\n");
            }
        }
        return result.toString();
    }

    public List<String> showStocks() {
        List<String> showStocks = new ArrayList<>();
        for (Map.Entry<String, Double> entry : stocks.entrySet()) {
            String name = entry.getKey();
            Double amount = entry.getValue();
            showStocks.add("Stock: Name -> " + name + "; Total Amount -> " + amount + ";\n");
        }
        return showStocks;
    }

    public double getAmountStockByName(String name) {
        if (stocks.containsKey(name.toUpperCase())) return stocks.get(name.toUpperCase());
        return -1;
    }

    public Stock getStockById(int id) {
        for (Stock s : stockHistoryList) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    //Adicionar Stocks
    public void buyStock(double newAmount, String name, double price, StockType type, Date date) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome do stock não pode ser vazio ou nulo.");
        }
        if (newAmount <= 0) {
            throw new IllegalArgumentException("Montante de compra deve ser maior que zero.");
        }

        String stockName = name.toUpperCase();
        Stock novaCompra = new Stock(nextId, newAmount, stockName, price, type, date);
        nextId++;

        if (stocks.containsKey(stockName)) {
            double previousAmount = stocks.get(stockName);
              stocks.put(stockName, previousAmount + newAmount);
        } else {
            stocks.put(stockName, newAmount);
        }

        stockHistoryList.add(novaCompra);
    }


    //Remover Stocks
    public void sellStock(String name, double amountToSell, Date date) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome do stock não pode ser vazio ou nulo.");
        }
        if (amountToSell <= 0) {
            throw new IllegalArgumentException("Montante de venda deve ser maior que zero.");
        }

        String stockName = name.toUpperCase(); // Nome consistente

        // Verificar se o stock existe
        if (stocks.containsKey(stockName)) {
            double previousAmount = stocks.get(stockName.toUpperCase());

            // Verificar se há quantidade suficiente para vender
            if (previousAmount >= amountToSell) {
                // Adicionar a venda ao histórico
                Stock novaVenda = new Stock(nextId, -amountToSell, stockName, 0, StockType.SALE, date);
                nextId++;
                stockHistoryList.add(novaVenda);

                // Atualizar ou remover do mapa de stocks
                if (previousAmount > amountToSell) {
                    stocks.put(stockName, previousAmount - amountToSell);
                } else {
                    stocks.remove(stockName);
                }
                System.out.println("Venda realizada com sucesso: " + amountToSell + " unidades de " + stockName);
            } else {
                System.out.println("Quantidade insuficiente para venda: disponível " + previousAmount + ", solicitado " + amountToSell);
            }
        } else {
            System.out.println("Stock não encontrado: " + stockName);
        }
    }

    public List<Stock> getSaleHistoryList() {
        List<Stock> saleHistoryList = new ArrayList<Stock>();
        for (Stock stock : stockHistoryList) {
            if (stock.getType().equals(StockType.SALE)) {
                saleHistoryList.add(stock);
            }
        }
        return saleHistoryList;
    }

    public List<Stock> getPreferredHistoryList() {
        List<Stock> preferredHistoryList = new ArrayList<Stock>();
        for (Stock stock : stockHistoryList) {
            if (stock.getType().equals(StockType.PREFERRED)) {
                preferredHistoryList.add(stock);
            }
        }
        return preferredHistoryList;
    }

    public List<Stock> getOrdinariesHistoryList() {
        List<Stock> ordinariesHistoryList = new ArrayList<Stock>();
        for (Stock stock : stockHistoryList) {
            if (stock.getType().equals(StockType.ORDINARIES)) {
                ordinariesHistoryList.add(stock);
            }
        }
        return ordinariesHistoryList;
    }
}
