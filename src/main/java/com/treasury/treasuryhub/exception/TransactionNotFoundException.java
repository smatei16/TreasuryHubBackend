package com.treasury.treasuryhub.exception;

public class TransactionNotFoundException extends Exception {

    public TransactionNotFoundException(int id) {
        super("Transaction with id " + id + " does not exist.");
    }
}
