package com.bankapp;

import java.util.Scanner;

public final class MenuInput {

    public static double promptAmount(Scanner scanner, String label) {
        System.out.print(label);
        String input = scanner.nextLine().trim();
        try {
            double value = Double.parseDouble(input);
            if (value < 0) {
                System.out.println("Amount can't be negative.");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Not a valid number.");
            return -1;
        }
    }

    private MenuInput() {
    }
}
