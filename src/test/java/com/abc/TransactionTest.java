package com.abc;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TransactionTest {
    @test
    public void transaction() {
        Transaction t = new Transaction(5);
        assertTrue(t instanceof Transaction);
    }
}
