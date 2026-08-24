package com.bankapp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BankService {

    private final ExcelManager excelManager;
    private final SampleDataSeeder seeder;
    private Map<String, Account> accounts;
    private List<Transaction> transactions;

    public BankService(ExcelManager excelManager, SampleDataSeeder seeder) {
        this.excelManager = excelManager;
        this.seeder = seeder;
        this.accounts = new LinkedHashMap<>();
        this.transactions = new ArrayList<>();
    }

    public void loadOrSeed() throws IOException {
        accounts = excelManager.loadAccounts();
        transactions = excelManager.loadTransactions();

        if (accounts.isEmpty()) {
            System.out.println("No " + AppConstants.DATA_FILE_PATH + " found (or it was empty) - creating sample data...");
            accounts = seeder.seedAccounts();
            seeder.seedTransactions(accounts, transactions);
            persist();
        }
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public Account findAccount(String username) {
        return accounts.get(username);
    }

    public List<Account> searchAccountsByUsername(String usernameFragment) {
        List<Account> matches = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUsername().toLowerCase().contains(usernameFragment.toLowerCase())) {
                matches.add(account);
            }
        }
        return matches;
    }

    public List<Transaction> getTransactionsFor(String username) {
        List<Transaction> owned = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.belongsTo(username)) {
                owned.add(transaction);
            }
        }
        return owned;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> searchTransactions(String username, TransactionType type, double minAmount) {
        List<Transaction> results = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (!transaction.belongsTo(username)) {
                continue;
            }
            if (type != null && transaction.getType() != type) {
                continue;
            }
            if (transaction.getAmount() < minAmount) {
                continue;
            }
            results.add(transaction);
        }
        return results;
    }

    public void deposit(Account account, double amount) throws IOException {
        account.deposit(amount);
        transactions.add(new Transaction(account.getUsername(), LocalDate.now(), TransactionType.DEPOSIT, amount, account.getBalance()));
        persist();
    }

    public boolean withdraw(Account account, double amount) throws IOException {
        if (!account.withdraw(amount)) {
            return false;
        }
        transactions.add(new Transaction(account.getUsername(), LocalDate.now(), TransactionType.WITHDRAW, amount, account.getBalance()));
        persist();
        return true;
    }

    public double totalFundsInBank() {
        double total = 0;
        for (Account account : accounts.values()) {
            total += account.getBalance();
        }
        return total;
    }

    private void persist() throws IOException {
        excelManager.saveAll(accounts, transactions);
    }
}
