package com.treasury.treasuryhub.exception;

public class BudgetNotFoundException extends Exception {

    public BudgetNotFoundException(int id) {
        super("Budget with id " + id + " does not exist.");
    }
}
