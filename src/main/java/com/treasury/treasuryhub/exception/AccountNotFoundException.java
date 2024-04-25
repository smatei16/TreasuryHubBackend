package com.treasury.treasuryhub.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(int id) {
        super("Account with id " + id + " does not exist.");
    }
}
