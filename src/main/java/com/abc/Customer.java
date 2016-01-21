package com.abc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Customer implements  Comparable<Customer>{
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public Customer openAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() {
        double total = 0;
        for (Account a : accounts)
            total += a.interestEarned();
        return total;
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + a.statementForAccount() + "\n";
            total += a.sumTransactions();
        }
        statement += "\nTotal In All Accounts " + PrintHelper.toDollars(total);
        return statement;
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    public int compareTo(Customer customer)
    {
        return this.name.compareTo(customer.getName());
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Customer) {
            return this.name.equals(((Customer) obj).getName());
        }
        return false;
    }

    public void transferWithinAccounts(String fromAccountId, String toAccountId, double amount) {

        Account fromAccount = null;
        Account toAccount = null;

        for(Account account : accounts) {
            if(account.getAccountId().equals(fromAccountId)) {
                fromAccount = account;
            }
            else if(account.getAccountId().equals(toAccountId)); {
                toAccount = account;
            }
        }

        transferWithinAccounts(fromAccount, toAccount, amount);
    }

    public void transferWithinAccounts(Account fromAccount, Account toAccount, double amount) {

        if(amount < 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }

        if(fromAccount == null) {
            throw new IllegalArgumentException("fromAccountId not found");
        }
        if(toAccount == null) {
            throw new IllegalArgumentException("toAccountId not found");
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }
}
