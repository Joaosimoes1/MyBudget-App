package org.example.gps_g22.model.data;

import java.io.Serial;
import java.io.Serializable;

public class SpendingGoal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private double goalAmount;
    private double spentAmount;
    private final int id;
    static private int idCounter = 1;

    public SpendingGoal(String name, double goalAmount, double spentAmount) {
        this.name = name;
        this.goalAmount = goalAmount;
        this.spentAmount = spentAmount;

        this.id = idCounter;
        idCounter++;
    }

    public SpendingGoal(SpendingGoal sg) {
        this.name = sg.getName();
        this.goalAmount = sg.getGoalAmount();
        this.spentAmount = sg.getSpentAmount();
        this.id = sg.getId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }
}
