package com.bankapp;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private static final String OPTION_VIEW_BALANCE = "1";
    private static final String OPTION_VIEW_HISTORY = "2";
    private static final String OPTION_SEARCH_TRANSACTIONS = "3";
    private static final String OPTION_DEPOSIT = "4";
    private static final String OPTION_WITHDRAW = "5";
    private static final String OPTION_LOGOUT = "6";
    private static final String OPTION_EXIT = "7";

    private final BankService bankService;
    private final Account account;
    private final Scanner scanner;

    public CustomerMenu(BankService bankService, Account account, Scanner scanner) {
        this.bankService = bankService;
        this.account = account;
        this.scanner = scanner;
    }

    public MenuResult run() throws Exception {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case OPTION_VIEW_BALANCE -> viewBalance();
                case OPTION_VIEW_HISTORY -> viewHistory();
                case OPTION_SEARCH_TRANSACTIONS -> searchTransactions();
                case OPTION_DEPOSIT -> handleDeposit();
                case OPTION_WITHDRAW -> handleWithdraw();
                case OPTION_LOGOUT -> {
                    return MenuResult.LOGOUT;
                }
                case OPTION_EXIT -> {
                    return MenuResult.EXIT_PROGRAM;
                }
                default -> System.out.println("Not a valid option, try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== CUSTOMER MENU (" + account.getUsername() + ") =====");
        System.out.println(OPTION_VIEW_BALANCE + ". View Balance");
        System.out.println(OPTION_VIEW_HISTORY + ". View Transaction History");
        System.out.println(OPTION_SEARCH_TRANSACTIONS + ". Search Transactions");
        System.out.println(OPTION_DEPOSIT + ". Deposit");
        System.out.println(OPTION_WITHDRAW + ". Withdraw");
        System.out.println(OPTION_LOGOUT + ". Logout");
        System.out.println(OPTION_EXIT + ". Exit Program");
        System.out.print("Choose an option: ");
    }

    private void viewBalance() {
        System.out.printf("%nCurrent balance for %s: $%,.2f%n", account.getUsername(), account.getBalance());
    }

    private void viewHistory() {
        List<Transaction> history = bankService.getTransactionsFor(account.getUsername());
        System.out.println();
        System.out.println("--- Transaction History: " + account.getUsername() + " ---");

        if (history.isEmpty()) {
            System.out.println("(no transactions yet)");
            return;
        }

        double totalDeposits = 0;
        double totalWithdrawals = 0;
        for (Transaction transaction : history) {
            System.out.println(transaction);
            if (transaction.getType() == TransactionType.DEPOSIT) {
                totalDeposits += transaction.getAmount();
            } else {
                totalWithdrawals += transaction.getAmount();
            }
        }
        System.out.printf("Total transactions: %d | Total deposits: $%,.2f | Total withdrawals: $%,.2f%n",
                history.size(), totalDeposits, totalWithdrawals);
    }

    private void searchTransactions() {
        System.out.print("\nSearch by type (DEPOSIT / WITHDRAW / leave blank for either): ");
        String typeInput = scanner.nextLine().trim();
        TransactionType type = typeInput.isBlank() ? null : TransactionType.valueOf(typeInput.toUpperCase());

        System.out.print("Minimum amount (leave blank for 0): ");
        String minInput = scanner.nextLine().trim();
        double minAmount = minInput.isBlank() ? 0 : Double.parseDouble(minInput);

        List<Transaction> results = bankService.searchTransactions(account.getUsername(), type, minAmount);
        System.out.println();
        System.out.println("--- Search Results ---");
        if (results.isEmpty()) {
            System.out.println("No matching transactions.");
            return;
        }
        for (Transaction transaction : results) {
            System.out.println(transaction);
        }
    }

    private void handleDeposit() throws Exception {
        double amount = MenuInput.promptAmount(scanner, "Amount to deposit: ");
        if (amount <= 0) {
            return;
        }
        bankService.deposit(account, amount);
        System.out.printf("Deposited $%,.2f. New balance: $%,.2f%n", amount, account.getBalance());
    }

    private void handleWithdraw() throws Exception {
        double amount = MenuInput.promptAmount(scanner, "Amount to withdraw: ");
        if (amount <= 0) {
            return;
        }
        boolean success = bankService.withdraw(account, amount);
        if (success) {
            System.out.printf("Withdrew $%,.2f. New balance: $%,.2f%n", amount, account.getBalance());
        } else {
            System.out.printf("Insufficient funds. Current balance: $%,.2f%n", account.getBalance());
        }
    }
}
