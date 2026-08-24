package com.bankapp;

import java.time.LocalDate;

public class LoanResult {

    private final double monthlyPayment;
    private final double totalPaid;
    private final double totalInterestPaid;
    private final double totalPrincipalPaid;
    private final LocalDate payoffDate;

    public LoanResult(double monthlyPayment, double totalPaid, double totalInterestPaid,
                       double totalPrincipalPaid, LocalDate payoffDate) {
        this.monthlyPayment = monthlyPayment;
        this.totalPaid = totalPaid;
        this.totalInterestPaid = totalInterestPaid;
        this.totalPrincipalPaid = totalPrincipalPaid;
        this.payoffDate = payoffDate;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public double getTotalInterestPaid() {
        return totalInterestPaid;
    }

    public double getTotalPrincipalPaid() {
        return totalPrincipalPaid;
    }

    public LocalDate getPayoffDate() {
        return payoffDate;
    }
}
