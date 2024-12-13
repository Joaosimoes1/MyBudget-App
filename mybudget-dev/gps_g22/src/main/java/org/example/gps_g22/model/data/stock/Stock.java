package org.example.gps_g22.model.data.stock;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Date date;
    private double amount;
    private String name;
    private double price;
    private StockType stockType;
    private int id;

    public Stock(int id, double amount, String name, double price, StockType type, Date date) {
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.price = price;
        this.stockType = type;
        this.id = id;
  }

    public double getAmount() {
        return amount;
    }

    public int getId(){return id;}

    public Date getDate() {return date; }

    public String getName() {return name; }

    public double getPrice() {
        return price;
    }

    public StockType getType() {
        return stockType;
    }


    public String toString() {
        return "Stock "+name+" -> ID: "+ id + ", Amount: "+ amount + ", Price: "+ price + ", Type: " + stockType +", Date: "+ date +";";
    }
}