package com.abc;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

public class Transaction {
    public final double amount;

    private Date transactionDate;

    public Transaction(double amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }

    public Transaction(double amount, Date date) {
        this.amount = amount;
        this.transactionDate = date;
    }

    public String printTransaction() {
        return (amount < 0 ? "withdrawal" : "deposit") + " " + PrintHelper.toDollars(amount);
    }

    public double getAmount()
    {
        return amount;
    }

    public Date getTransactionDate()
    {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate)
    {
        this.transactionDate = transactionDate;
    }

    public boolean isDeposit() {
        return this.amount > 0;
    }

}
