package org.example.cuahangsach.model;

public class Payment {
    private String username;
    private double amount;
    private String paymentMethod;

    public Payment(String username, double amount, String paymentMethod) {
        this.username = username;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}

