package com.bankapp;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ExcelManager excelManager = new ExcelManager(AppConstants.DATA_FILE_PATH);
        SampleDataSeeder seeder = new SampleDataSeeder();
        BankService bankService = new BankService(excelManager, seeder);

        bankService.loadOrSeed();

        System.out.println("=======================================");
        System.out.println("        WELCOME TO BANKAPP");
        System.out.println("=======================================");

        boolean keepRunning = true;
        while (keepRunning) {
            AuthService authService = new AuthService(bankService.getAccounts());
            Optional<Account> loggedInAccount = authService.login(scanner);

            if (loggedInAccount.isEmpty()) {
                System.out.println("\nToo many failed attempts. Goodbye.");
                break;
            }

            Account account = loggedInAccount.get();
            MenuResult result = account.getType() == AccountType.ADMIN
                    ? new AdminMenu(bankService, account, scanner).run()
                    : new CustomerMenu(bankService, account, scanner).run();

            keepRunning = result != MenuResult.EXIT_PROGRAM;
        }

        scanner.close();
        System.out.println("Thanks for using BankApp!");
    }
}
