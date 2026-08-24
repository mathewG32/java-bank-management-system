package com.bankapp;

import java.time.LocalDate;

public class LoanCalculator {

    public static LoanResult calculate(double principal, double annualRatePercent, int termMonths) {
        double monthlyRate = (annualRatePercent / AppConstants.PERCENT_TO_DECIMAL_DIVISOR) / AppConstants.MONTHS_PER_YEAR;
        double monthlyPayment;

        if (monthlyRate == 0) {
            monthlyPayment = principal / termMonths;
        } else {
            double growthFactor = Math.pow(1 + monthlyRate, termMonths);
            monthlyPayment = principal * (monthlyRate * growthFactor) / (growthFactor - 1);
        }

        double totalPaid = monthlyPayment * termMonths;
        double totalInterestPaid = totalPaid - principal;
        LocalDate payoffDate = LocalDate.now().plusMonths(termMonths);

        return new LoanResult(monthlyPayment, totalPaid, totalInterestPaid, principal, payoffDate);
    }
}
