package com.treasury.treasuryhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyRegistered() {
        return new ResponseEntity<>("There is already an account with this email in use.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<?> feedbackNotFound() {
        return new ResponseEntity<>("Feedback not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFound() {
        return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionCategoryNotFoundException.class)
    public ResponseEntity<?> transactionCategoryNotFound() {
        return new ResponseEntity<>("Transaction category not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionTypeNotSupportedException.class)
    public ResponseEntity<?> transactionTypeNotSupported() {
        return new ResponseEntity<>("Transaction type not supported.", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<?> transactionNotFound() {
        return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<?> userNotFound() {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
}
