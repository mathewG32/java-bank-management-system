package com.bankapp;

public class Account {

    private final String username;
    private final String password;
    private final AccountType type;
    private double balance;

    public Account(String username, String password, AccountType type, double balance) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isPasswordCorrect(String candidate) {
        return password.equals(candidate);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-8s | $%,.2f", username, type, balance);
    }
}
