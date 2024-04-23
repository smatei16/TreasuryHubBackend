package com.treasury.treasuryhub.exception;

public class FeedbackNotFoundException extends Exception {

    public FeedbackNotFoundException(int id) {
        super("Feedback with id " + id + " does not exist");
    }
}
