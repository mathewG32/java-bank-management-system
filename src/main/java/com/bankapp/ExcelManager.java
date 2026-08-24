package com.bankapp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelManager {

    private final String filePath;

    public ExcelManager(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Account> loadAccounts() throws IOException {
        Map<String, Account> accounts = new LinkedHashMap<>();

        if (!Files.exists(Paths.get(filePath))) {
            return accounts;
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(AppConstants.ACCOUNTS_SHEET_NAME);
            if (sheet == null) {
                return accounts;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == AppConstants.HEADER_ROW_INDEX) {
                    continue;
                }
                if (row.getCell(AppConstants.ACCOUNT_COL_USERNAME) == null) {
                    continue;
                }

                String username = readString(row.getCell(AppConstants.ACCOUNT_COL_USERNAME));
                if (username == null || username.isBlank()) {
                    continue;
                }

                String password = readString(row.getCell(AppConstants.ACCOUNT_COL_PASSWORD));
                String typeText = readString(row.getCell(AppConstants.ACCOUNT_COL_TYPE));
                double balance = readNumeric(row.getCell(AppConstants.ACCOUNT_COL_BALANCE));

                AccountType type = AccountType.valueOf(typeText.toUpperCase());
                accounts.put(username, new Account(username, password, type, balance));
            }
        }
        return accounts;
    }

    public List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        if (!Files.exists(Paths.get(filePath))) {
            return transactions;
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(AppConstants.TRANSACTIONS_SHEET_NAME);
            if (sheet == null) {
                return transactions;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == AppConstants.HEADER_ROW_INDEX) {
                    continue;
                }
                if (row.getCell(AppConstants.TX_COL_USERNAME) == null) {
                    continue;
                }

                String username = readString(row.getCell(AppConstants.TX_COL_USERNAME));
                if (username == null || username.isBlank()) {
                    continue;
                }

                LocalDate date = LocalDate.parse(readString(row.getCell(AppConstants.TX_COL_DATE)));
                TransactionType type = TransactionType.valueOf(
                        readString(row.getCell(AppConstants.TX_COL_TYPE)).toUpperCase());
                double amount = readNumeric(row.getCell(AppConstants.TX_COL_AMOUNT));
                double balanceAfter = readNumeric(row.getCell(AppConstants.TX_COL_BALANCE_AFTER));

                transactions.add(new Transaction(username, date, type, amount, balanceAfter));
            }
        }
        return transactions;
    }

    public void saveAll(Map<String, Account> accounts, List<Transaction> transactions) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            headerStyle.setFont(boldFont);

            writeAccountsSheet(workbook, headerStyle, accounts);
            writeTransactionsSheet(workbook, headerStyle, transactions);

            Files.createDirectories(Paths.get(filePath).toAbsolutePath().getParent());

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    private void writeAccountsSheet(Workbook workbook, CellStyle headerStyle, Map<String, Account> accounts) {
        Sheet sheet = workbook.createSheet(AppConstants.ACCOUNTS_SHEET_NAME);
        String[] columns = {"Username", "Password", "Type", "Balance"};
        Row header = sheet.createRow(AppConstants.HEADER_ROW_INDEX);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = AppConstants.FIRST_DATA_ROW_INDEX;
        for (Account account : accounts.values()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(AppConstants.ACCOUNT_COL_USERNAME).setCellValue(account.getUsername());
            row.createCell(AppConstants.ACCOUNT_COL_PASSWORD).setCellValue(account.getPassword());
            row.createCell(AppConstants.ACCOUNT_COL_TYPE).setCellValue(account.getType().name());
            row.createCell(AppConstants.ACCOUNT_COL_BALANCE).setCellValue(account.getBalance());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeTransactionsSheet(Workbook workbook, CellStyle headerStyle, List<Transaction> transactions) {
        Sheet sheet = workbook.createSheet(AppConstants.TRANSACTIONS_SHEET_NAME);
        String[] columns = {"Username", "Date", "Type", "Amount", "BalanceAfter"};
        Row header = sheet.createRow(AppConstants.HEADER_ROW_INDEX);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = AppConstants.FIRST_DATA_ROW_INDEX;
        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(AppConstants.TX_COL_USERNAME).setCellValue(transaction.getUsername());
            row.createCell(AppConstants.TX_COL_DATE).setCellValue(transaction.getDate().toString());
            row.createCell(AppConstants.TX_COL_TYPE).setCellValue(transaction.getType().name());
            row.createCell(AppConstants.TX_COL_AMOUNT).setCellValue(transaction.getAmount());
            row.createCell(AppConstants.TX_COL_BALANCE_AFTER).setCellValue(transaction.getBalanceAfter());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private String readString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }

    private double readNumeric(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return cell.getNumericCellValue();
    }
}
