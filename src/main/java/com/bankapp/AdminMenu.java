package com.bankapp;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private static final String OPTION_VIEW_ALL_ACCOUNTS = "1";
    private static final String OPTION_SEARCH_ACCOUNT = "2";
    private static final String OPTION_ADD_FUNDS = "3";
    private static final String OPTION_REMOVE_FUNDS = "4";
    private static final String OPTION_VIEW_HISTORY = "5";
    private static final String OPTION_LOAN_CALCULATOR = "6";
    private static final String OPTION_LOGOUT = "7";
    private static final String OPTION_EXIT = "8";

    private final BankService bankService;
    private final Account account;
    private final Scanner scanner;

    public AdminMenu(BankService bankService, Account account, Scanner scanner) {
        this.bankService = bankService;
        this.account = account;
        this.scanner = scanner;
    }

    public MenuResult run() throws Exception {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case OPTION_VIEW_ALL_ACCOUNTS -> viewAllAccounts();
                case OPTION_SEARCH_ACCOUNT -> searchAccount();
                case OPTION_ADD_FUNDS -> adjustFunds(true);
                case OPTION_REMOVE_FUNDS -> adjustFunds(false);
                case OPTION_VIEW_HISTORY -> viewHistory();
                case OPTION_LOAN_CALCULATOR -> runLoanCalculator();
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
        System.out.println("===== ADMIN MENU (" + account.getUsername() + ") =====");
        System.out.println(OPTION_VIEW_ALL_ACCOUNTS + ". View All Accounts");
        System.out.println(OPTION_SEARCH_ACCOUNT + ". Search Account");
        System.out.println(OPTION_ADD_FUNDS + ". Add Funds to an Account");
        System.out.println(OPTION_REMOVE_FUNDS + ". Remove Funds from an Account");
        System.out.println(OPTION_VIEW_HISTORY + ". View Transaction History (any account)");
        System.out.println(OPTION_LOAN_CALCULATOR + ". Loan Calculator");
        System.out.println(OPTION_LOGOUT + ". Logout");
        System.out.println(OPTION_EXIT + ". Exit Program");
        System.out.print("Choose an option: ");
    }

    private void viewAllAccounts() {
        System.out.println();
        System.out.println("--- All Accounts ---");
        System.out.printf("%-10s | %-8s | %s%n", "Username", "Type", "Balance");
        for (Account a : bankService.getAccounts().values()) {
            System.out.println(a);
        }
        System.out.printf("Total accounts: %d | Total funds in bank: $%,.2f%n",
                bankService.getAccounts().size(), bankService.totalFundsInBank());
    }

    private void searchAccount() {
        System.out.print("\nSearch username contains: ");
        String query = scanner.nextLine().trim();
        List<Account> matches = bankService.searchAccountsByUsername(query);

        System.out.println("--- Results ---");
        if (matches.isEmpty()) {
            System.out.println("No matching accounts.");
            return;
        }
        for (Account a : matches) {
            System.out.println(a);
        }
    }

    private void adjustFunds(boolean isAdd) throws Exception {
        System.out.print("Username to " + (isAdd ? "add funds to" : "remove funds from") + ": ");
        String username = scanner.nextLine().trim();
        Account target = bankService.findAccount(username);
        if (target == null) {
            System.out.println("No account with that username.");
            return;
        }

        double amount = MenuInput.promptAmount(scanner, "Amount: ");
        if (amount <= 0) {
            return;
        }

        if (isAdd) {
            bankService.deposit(target, amount);
        } else {
            boolean success = bankService.withdraw(target, amount);
            if (!success) {
                System.out.printf("Cannot remove $%,.2f - account only has $%,.2f%n", amount, target.getBalance());
                return;
            }
        }
        System.out.printf("%s's new balance: $%,.2f%n", username, target.getBalance());
    }

    private void viewHistory() {
        System.out.print("Username to view (blank = all): ");
        String username = scanner.nextLine().trim();

        if (username.isBlank()) {
            for (Transaction transaction : bankService.getAllTransactions()) {
                System.out.println(transaction);
            }
            return;
        }

        List<Transaction> history = bankService.getTransactionsFor(username);
        System.out.println();
        System.out.println("--- Transaction History: " + username + " ---");
        if (history.isEmpty()) {
            System.out.println("(no transactions yet)");
            return;
        }
        for (Transaction transaction : history) {
            System.out.println(transaction);
        }
    }

    private void runLoanCalculator() {
        System.out.println();
        System.out.println("--- Loan Calculator ---");
        double principal = MenuInput.promptAmount(scanner, "Loan amount: ");
        double annualRate = MenuInput.promptAmount(scanner, "Annual interest rate (e.g. 6.5 for 6.5%): ");
        System.out.print("Loan term in months: ");
        int termMonths = Integer.parseInt(scanner.nextLine().trim());

        LoanResult result = LoanCalculator.calculate(principal, annualRate, termMonths);

        System.out.println();
        System.out.println("--- Loan Summary ---");
        System.out.printf("Loan amount:            $%,.2f%n", principal);
        System.out.printf("Annual interest rate:   %.2f%%%n", annualRate);
        System.out.printf("Term:                   %d months%n", termMonths);
        System.out.printf("Estimated monthly pay:  $%,.2f%n", result.getMonthlyPayment());
        System.out.println("Payoff date:            " + result.getPayoffDate());
        System.out.printf("Total paid over loan:   $%,.2f%n", result.getTotalPaid());
        System.out.printf("  - Principal paid:     $%,.2f%n", result.getTotalPrincipalPaid());
        System.out.printf("  - Interest paid:      $%,.2f%n", result.getTotalInterestPaid());
    }
}
