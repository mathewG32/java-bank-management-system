package com.bankapp;

public final class AppConstants {

    public static final String DATA_FILE_PATH = "data/accounts.xlsx";

    public static final String ACCOUNTS_SHEET_NAME = "Accounts";
    public static final String TRANSACTIONS_SHEET_NAME = "Transactions";
    public static final int HEADER_ROW_INDEX = 0;
    public static final int FIRST_DATA_ROW_INDEX = 1;

    public static final int ACCOUNT_COL_USERNAME = 0;
    public static final int ACCOUNT_COL_PASSWORD = 1;
    public static final int ACCOUNT_COL_TYPE = 2;
    public static final int ACCOUNT_COL_BALANCE = 3;

    public static final int TX_COL_USERNAME = 0;
    public static final int TX_COL_DATE = 1;
    public static final int TX_COL_TYPE = 2;
    public static final int TX_COL_AMOUNT = 3;
    public static final int TX_COL_BALANCE_AFTER = 4;

    public static final int MAX_LOGIN_ATTEMPTS = 3;

    public static final int MONTHS_PER_YEAR = 12;
    public static final double PERCENT_TO_DECIMAL_DIVISOR = 100.0;

    public static final String SAMPLE_CUSTOMER_USERNAME = "customer1";
    public static final String SAMPLE_CUSTOMER_PASSWORD = "pass123";
    public static final double[] SAMPLE_CUSTOMER_TRANSACTION_AMOUNTS =
            {1200.00, 350.50, -200.00, 500.25, -150.00};

    public static final String SAMPLE_ADMIN_USERNAME = "admin1";
    public static final String SAMPLE_ADMIN_PASSWORD = "admin123";
    public static final double[] SAMPLE_ADMIN_TRANSACTION_AMOUNTS =
            {5000.00, -1000.00, 2500.75};

    private AppConstants() {
    }
}
