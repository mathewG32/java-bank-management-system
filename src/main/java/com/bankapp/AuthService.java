package com.bankapp;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class AuthService {

    private final Map<String, Account> accounts;

    public AuthService(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public Optional<Account> login(Scanner scanner) {
        int attempts = 0;
        while (attempts < AppConstants.MAX_LOGIN_ATTEMPTS) {
            System.out.println();
            System.out.println("--- LOGIN ---");
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            Account account = accounts.get(username);
            if (account != null && account.isPasswordCorrect(password)) {
                System.out.println();
                System.out.println("Login successful. Welcome, " + username + " (" + account.getType() + ")!");
                return Optional.of(account);
            }

            attempts++;
            int attemptsLeft = AppConstants.MAX_LOGIN_ATTEMPTS - attempts;
            System.out.println("Invalid username or password. Attempts left: " + attemptsLeft);
        }
        return Optional.empty();
    }
}
