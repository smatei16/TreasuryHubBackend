package com.treasury.treasuryhub.exception;

public class NoSuchUserException extends Exception {

    public NoSuchUserException(String errorMessage) {
        super(errorMessage);
    }
}
