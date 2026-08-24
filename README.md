# BankApp (Java Console Banking App)

A console banking app with login, customer/admin roles, and an Excel file
(`data/accounts.xlsx`) as the data store — Apache POI reads and writes it
automatically. See `GUIDE.txt` for a one-page quick reference.

## How to open and run in IntelliJ

1. Open IntelliJ IDEA -> File > Open -> select the `BankApp` folder (the
   one with `pom.xml` in it).
2. IntelliJ detects the Maven project and downloads Apache POI automatically
   (needs internet the first time).
3. Open `src/main/java/com/bankapp/Main.java`.
4. Click the green Run arrow, or right-click the file and choose
   Run 'Main.main()'.

## Login (sample accounts already in data/accounts.xlsx)

| Username  | Password | Type     |
|-----------|----------|----------|
| customer1 | pass123  | CUSTOMER |
| admin1    | admin123 | ADMIN    |

## Project structure

```
BankApp/
├── pom.xml
├── README.md
├── GUIDE.txt
├── data/
│   └── accounts.xlsx
└── src/main/java/com/bankapp/
    ├── Main.java
    ├── AuthService.java
    ├── BankService.java
    ├── CustomerMenu.java
    ├── AdminMenu.java
    ├── MenuInput.java
    ├── Account.java
    ├── Transaction.java
    ├── AccountType.java
    ├── TransactionType.java
    ├── MenuResult.java
    ├── ExcelManager.java
    ├── LoanCalculator.java
    ├── LoanResult.java
    ├── SampleDataSeeder.java
    └── AppConstants.java
```

## Design

- `Account` / `Transaction` / `AccountType` / `TransactionType` — model classes
- `BankService` — business logic: deposits, withdrawals, search, persistence
- `AuthService` — login logic
- `ExcelManager` — all Apache POI read/write code, isolated from the rest of the app
- `CustomerMenu` / `AdminMenu` — one class per role, each owns its own menu loop
- `LoanCalculator` / `LoanResult` — amortization math and its result
- `SampleDataSeeder` — builds the starter accounts/transactions if the Excel file is missing
- `AppConstants` — every file path, sheet name, column index, and seed value used across
  the app, so there are no magic numbers scattered through the logic
