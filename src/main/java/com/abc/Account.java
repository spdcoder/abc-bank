package com.abc;

import java.util.*;

import static java.lang.Math.abs;

public class Account {

    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;

    private final int accountType;

    private String accountId;
    private List<Transaction> transactions;
    private double accountAmount = 0.0;

    private static final double MAXI_SAVINGS_MAX_DAILY_Rate = 0.05 /365.0;
    private static final double MAXI_SAVINGS_MIN_DAILY_Rate = MAXI_SAVINGS_MAX_DAILY_Rate / 50.0;
    private static final int ONE_DAY = 24 * 3600000;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }

    public Account(int accountType, String accountId) {
        this(accountType);
        this.accountId = accountId;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            accountAmount += amount;
            transactions.add(new Transaction(amount));
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            accountAmount -= amount;
            transactions.add(new Transaction(-amount));
        }
    }

    public double interestEarned() {
        double amount = sumTransactions();
        switch(accountType){
            case SAVINGS:
                if (amount <= 1000)
                    return amount * 0.001;
                else
                    return 1 + (amount-1000) * 0.002;
//            case SUPER_SAVINGS:
//                if (amount <= 4000)
//                    return 20;
            case MAXI_SAVINGS:
//                if (amount <= 1000)
//                    return amount * 0.02;
//                if (amount <= 2000)
//                    return 20 + (amount-1000) * 0.05;
//                return 70 + (amount-2000) * 0.1;
                return getMaxiSavingInterestEarned();
            default:
                return amount * 0.001;
        }
    }

    private double getMaxiSavingInterestEarned()
    {
        double amount = 0.0;
        double totalInterest = 0.0;
        Date startDate = null;

        //filer unique withdraw days indexes
        List<Integer> withdrawTransactions = new LinkedList<Integer>();
        int size = transactions.size();
        Date tempDate = null;
        for (int i = 0; i < size; i++) {
            Transaction t = transactions.get(i);
            if(startDate == null) {
                startDate = t.getTransactionDate();
            }

            if(!t.isDeposit()) {
                if(tempDate == null) {
                    tempDate = t.getTransactionDate();
                    withdrawTransactions.add(i);
                }
                else {
                    if(!isSameDay(tempDate, t.getTransactionDate())){
                        withdrawTransactions.add(i);
                        tempDate = t.getTransactionDate();
                    }
                }
            }
        }

        Date endDate = new Date();

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        //Initialize nextWithdrawTransaction
        Transaction nextWithdrawTransaction = null;

        //Calculate interest day by day
        int transactionAmountIndex = 0;
        while(!start.after(end))
        {
            int year = start.get(Calendar.YEAR);
            int month = start.get(Calendar.MONTH) + 1;
            int day = start.get(Calendar.DAY_OF_MONTH);
            double newInterest = 0.0;
            //System.out.printf("%d.%d.%d \n\n", day, month, year);

            //Update amount to start date
            for (int i = transactionAmountIndex; i < size; i++)
            {
                Transaction t = transactions.get(i);
                if(isSameDay(start.getTime(), t.getTransactionDate())) {
                    amount += t.getAmount();
                    //System.out.println("new amount = " + amount);
                }
                else {
                    transactionAmountIndex = i;
                    //System.out.println("Break at transactionAmountIndex = " +transactionAmountIndex);
                    break;
                }
            }

            //find nextWithdrawTransaction
            if(nextWithdrawTransaction == null || nextWithdrawTransaction.getTransactionDate().before(start.getTime())) {
                if(!withdrawTransactions.isEmpty()) {
                    nextWithdrawTransaction = getTransaction(withdrawTransactions.remove(0));
                    //System.out.println("nextWithdrawTransaction = " + nextWithdrawTransaction.getTransactionDate());
                }
            }

            //Add MAX daily rate if next withdraw ia 10 days away, otherwise add MIN daily rate
            if(moreThanTenDays(nextWithdrawTransaction, start)) {
                newInterest = amount * MAXI_SAVINGS_MAX_DAILY_Rate;
                //System.out.println("Add MAX rate, newInterest = " + newInterest);
            }
            else {
                newInterest = amount * MAXI_SAVINGS_MIN_DAILY_Rate;
               // System.out.println("Add MIN rate, newInterest = " + newInterest);
            }

            newInterest = round(newInterest, 2);
            amount += newInterest;
            totalInterest += newInterest;

            //System.out.printf("%d.%d.%d amount:%f, newInterest:%f , totalInterest:%f\n\n", day, month, year, amount,newInterest,totalInterest);
            start.add(Calendar.DATE, 1);
        }

        return round(totalInterest, 2);
    }

    private Transaction getTransaction(Integer index)
    {
        if(index > 0 && index < transactions.size()) {
            return transactions.get(index);
        }
        return null;
    }

    private boolean isSameDay(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        //System.out.println("Left : " + cal1.get(Calendar.YEAR) + " : " + cal1.get(Calendar.DAY_OF_YEAR) + ", right : " +
        //        cal2.get(Calendar.YEAR) + " : " + cal2.get(Calendar.DAY_OF_YEAR));
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean moreThanTenDays(Transaction nextWithdrawTransaction, Calendar start)
    {
        if(nextWithdrawTransaction == null) {
            return true;
        }
        Date withdrawDate = nextWithdrawTransaction.getTransactionDate();
        if(isSameDay(withdrawDate, start.getTime())) {
            return false;
        }

        return moreThanTenDays(withdrawDate, start.getTime());
    }

    private boolean moreThanTenDays(Date nextWithdrawTransaction, Date start)
    {
        long startTime = start.getTime();
        long endTIme = nextWithdrawTransaction.getTime();
        long diff = endTIme - startTime;
        //System.out.println("startTime = " + startTime + " , endTIme = " + endTIme + " , diff = " + diff);

        // return nextWithdrawTransaction.compareTo(start);
        return diff < 0? true : (diff >= 10 * ONE_DAY);
    }

    public double sumTransactions() {
       //return checkIfTransactionsExist(true);
        return this.accountAmount;
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.amount;
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }


    public String getAccountId()
    {
        return accountId;
    }

    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String statementForAccount() {
        String s = "";

        //Translate to pretty account type
        switch(getAccountType()){
            case Account.CHECKING:
                s += "Checking Account\n";
                break;
            case Account.SAVINGS:
                s += "Savings Account\n";
                break;
            case Account.MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        //Now total up all the transactions
        double total = 0.0;
        for (Transaction t : transactions) {
            s += "  " + t.printTransaction() + "\n";
            total += t.amount;
        }
        s += "Total " + PrintHelper.toDollars(total);
        return s;
    }

    public void addTransaction(Transaction t) {
        if(t != null){
            this.transactions.add(t);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
