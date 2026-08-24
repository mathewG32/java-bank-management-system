package com.bankapp;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SampleDataSeeder {

    public Map<String, Account> seedAccounts() {
        Map<String, Account> accounts = new LinkedHashMap<>();
        accounts.put(AppConstants.SAMPLE_CUSTOMER_USERNAME,
                new Account(AppConstants.SAMPLE_CUSTOMER_USERNAME, AppConstants.SAMPLE_CUSTOMER_PASSWORD,
                        AccountType.CUSTOMER, 0));
        accounts.put(AppConstants.SAMPLE_ADMIN_USERNAME,
                new Account(AppConstants.SAMPLE_ADMIN_USERNAME, AppConstants.SAMPLE_ADMIN_PASSWORD,
                        AccountType.ADMIN, 0));
        return accounts;
    }

    public void seedTransactions(Map<String, Account> accounts, List<Transaction> transactions) {
        Account customer = accounts.get(AppConstants.SAMPLE_CUSTOMER_USERNAME);
        seedTransactionsForAccount(customer, AppConstants.SAMPLE_CUSTOMER_TRANSACTION_AMOUNTS, transactions);

        Account admin = accounts.get(AppConstants.SAMPLE_ADMIN_USERNAME);
        seedTransactionsForAccount(admin, AppConstants.SAMPLE_ADMIN_TRANSACTION_AMOUNTS, transactions);
    }

    private void seedTransactionsForAccount(Account account, double[] amounts, List<Transaction> transactions) {
        LocalDate date = LocalDate.now().minusDays(amounts.length);
        for (double amount : amounts) {
            TransactionType type;
            double magnitude;
            if (amount >= 0) {
                type = TransactionType.DEPOSIT;
                magnitude = amount;
                account.deposit(magnitude);
            } else {
                type = TransactionType.WITHDRAW;
                magnitude = -amount;
                account.withdraw(magnitude);
            }
            transactions.add(new Transaction(account.getUsername(), date, type, magnitude, account.getBalance()));
            date = date.plusDays(1);
        }
    }
}
