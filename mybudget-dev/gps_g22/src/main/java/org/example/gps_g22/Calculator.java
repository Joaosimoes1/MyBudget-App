package org.example.gps_g22;

public class Calculator {
    private Calculator() {}

    public static int factorial(int n) {
        return n < 1 ? 1 : (n * factorial(n - 1));
    }

    public static double sqrt(double n) {
        return Math.sqrt(Math.abs(n));
    }

}
