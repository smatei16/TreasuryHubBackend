package com.treasury.treasuryhub.exception;

public class UserStockNotFoundException extends Exception {

    public UserStockNotFoundException(int id) {
        super("User stock with id " + id + " does not exist.");
    }
}
