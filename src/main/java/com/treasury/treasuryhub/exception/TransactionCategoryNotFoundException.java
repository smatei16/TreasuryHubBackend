package com.treasury.treasuryhub.exception;

public class TransactionCategoryNotFoundException extends Exception {

    public TransactionCategoryNotFoundException(int id) {
        super("Transaction category with id " + id + " does not exist.");
    }
}
