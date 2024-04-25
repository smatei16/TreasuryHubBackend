package com.treasury.treasuryhub.exception;

public class TransactionTypeNotSupportedException extends Exception {

    public TransactionTypeNotSupportedException(String type) {
        super("The transaction type " + type + " is not supported.");
    }
}
