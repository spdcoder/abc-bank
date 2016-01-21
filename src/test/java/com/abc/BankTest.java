package com.abc;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;
    private static final int ONE_DAY = 24 * 3600000;
    @Test
    public void customerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(Account.CHECKING));
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void checkingAccount() {
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        checkingAccount.deposit(100.0);

        assertEquals(0.1, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void savings_account() {
        Bank bank = new Bank();
        Account savingAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(savingAccount));

        savingAccount.deposit(1500.0);

        assertEquals(2.0, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void maxi_savings_account() {
        Bank bank = new Bank();
        Account maxiSavingAccount = new Account(Account.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(maxiSavingAccount));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);

        Transaction transaction = new Transaction(10000.0, cal.getTime());
        Transaction transaction11 = new Transaction(1000.0, new Date(new Date().getTime() - (20 * ONE_DAY)));
        Transaction transaction12 = new Transaction(2000.0, new Date(new Date().getTime() - (20 * ONE_DAY)));
        Transaction transaction13 = new Transaction(3000.0, new Date(new Date().getTime() - (20 * ONE_DAY)));

        Transaction transaction2 = new Transaction(-1000.0, new Date(new Date().getTime() - (16 * ONE_DAY)));
        Transaction transaction21 = new Transaction(-2000.0, new Date(new Date().getTime() - (16 * ONE_DAY)));
        Transaction transaction22 = new Transaction(-3000.0, new Date(new Date().getTime() - (16 * ONE_DAY)));

        Transaction transaction3 = new Transaction(1000.0, new Date(new Date().getTime() - (8 * ONE_DAY)));
        Transaction transaction4 = new Transaction(500.0, new Date(new Date().getTime() - (5 * ONE_DAY)));

        Transaction transaction5= new Transaction(-2000.0, new Date(new Date().getTime() - (3 * ONE_DAY)));
        Transaction transaction51= new Transaction(-2000.0, new Date(new Date().getTime() - (3 * ONE_DAY)));
        Transaction transaction52= new Transaction(-1000.0, new Date(new Date().getTime() - (2 * ONE_DAY)));

        maxiSavingAccount.addTransaction(transaction);
        maxiSavingAccount.addTransaction(transaction11);
        maxiSavingAccount.addTransaction(transaction12);
        maxiSavingAccount.addTransaction(transaction13);

        maxiSavingAccount.addTransaction(transaction2);
        maxiSavingAccount.addTransaction(transaction21);
        maxiSavingAccount.addTransaction(transaction22);

        maxiSavingAccount.addTransaction(transaction3);
        maxiSavingAccount.addTransaction(transaction4);
        maxiSavingAccount.addTransaction(transaction5);
        maxiSavingAccount.addTransaction(transaction51);
        maxiSavingAccount.addTransaction(transaction52);

        assertEquals(13.39, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    /**
     * Print customer in alphabetically order
     */
    public void sortedCustomerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(Account.CHECKING));
        bank.addCustomer(john);

        Customer adele = new Customer("Adele");
        adele.openAccount(new Account(Account.CHECKING));
        adele.openAccount(new Account(Account.SAVINGS));
        bank.addCustomer(adele);
        //System.out.println(bank.customerSummary());

        assertEquals("Customer Summary\n" +
                " - Adele (2 accounts)\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void checkingAccountMultipleTests() {
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        checkingAccount.deposit(100.0);

        assertEquals(0.1, bank.totalInterestPaid(), DOUBLE_DELTA);

        checkingAccount.deposit(2000.0);
        assertEquals(2.1, bank.totalInterestPaid(), DOUBLE_DELTA);

        checkingAccount.deposit(30000.0);
        assertEquals(32.1, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void savingAccountMultipleTests() {
        Bank bank = new Bank();
        //Bill
        Account savingingAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(savingingAccount));

        savingingAccount.deposit(500.0);
        assertEquals(0.5, bank.totalInterestPaid(), DOUBLE_DELTA);

        savingingAccount.deposit(1000.0);
        assertEquals(2.0, bank.totalInterestPaid(), DOUBLE_DELTA);

        savingingAccount.deposit(20000.0);
        assertEquals(42.0, bank.totalInterestPaid(), DOUBLE_DELTA);

        //Curry
        Account savingingAccount2 = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Curry").openAccount(savingingAccount2));

        savingingAccount2.deposit(1000.0);
        assertEquals(43.0, bank.totalInterestPaid(), DOUBLE_DELTA);

        savingingAccount2.deposit(2000.0);
        assertEquals(47.0, bank.totalInterestPaid(), DOUBLE_DELTA);

        savingingAccount2.deposit(500.0);
        assertEquals(48.0, bank.totalInterestPaid(), DOUBLE_DELTA);
    }



}
