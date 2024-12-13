package org.example.gps_g22;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CalculatorTest {

    @Test
    public void factorial() {
        //1!
        assertEquals(1, Calculator.factorial(1));

        //2!
        assertEquals(2, Calculator.factorial(2));

        //3!
        assertEquals(6, Calculator.factorial(3));

        //4!
        assertEquals(24, Calculator.factorial(4));

        //5!
        assertEquals(120, Calculator.factorial(5));

    }

    @Test
    public void sqrt() {
        assertEquals(2, Calculator.sqrt(4));

        assertEquals(3, Calculator.sqrt(9));

        assertEquals(10, Calculator.sqrt(100));
    }
}