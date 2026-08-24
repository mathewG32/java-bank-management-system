package com.bankapp;

import java.time.LocalDate;

public class Transaction {

    private final String username;
    private final LocalDate date;
    private final TransactionType type;
    private final double amount;
    private final double balanceAfter;

    public Transaction(String username, LocalDate date, TransactionType type, double amount, double balanceAfter) {
        this.username = username;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public boolean belongsTo(String candidateUsername) {
        return username.equals(candidateUsername);
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-10s | %-8s | $%,-10.2f | Balance after: $%,.2f",
                username, date, type, amount, balanceAfter);
    }
}
