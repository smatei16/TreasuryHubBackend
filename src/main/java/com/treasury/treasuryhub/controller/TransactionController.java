package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.dto.TransactionIntervalDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.exception.TransactionNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/save")
    public ResponseEntity<?> registerTransaction(@RequestBody TransactionDto transactionDto) throws AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        return new ResponseEntity<>(transactionService.registerTransaction(transactionDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable int id) throws TransactionNotFoundException {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserTransactions() {
        return new ResponseEntity<>(transactionService.getUserTransactions(), HttpStatus.OK);
    }

    @GetMapping("/user/detailed")
    public ResponseEntity<?> getDetailedUserTransactions() {
        return new ResponseEntity<>(transactionService.getDetailedUserTransactions(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/last30days")
    public ResponseEntity<?> getLast30DaysTransactions() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = today.minusDays(30);
        return ResponseEntity.ok(transactionService.getUserTransactionsInInterval(thirtyDaysAgo, today));
    }

    @GetMapping("/thisMonth")
    public ResponseEntity<?> getThisMonthTransactions() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = today.withDayOfMonth(1);
        return ResponseEntity.ok(transactionService.getUserTransactionsInInterval(firstDayOfMonth, today));
    }

    @GetMapping("/customInterval")
    public ResponseEntity<?> getCustomIntervalTransactions(@RequestBody TransactionIntervalDto transactionIntervalDto) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(transactionIntervalDto.getStartDate(), fmt);
        LocalDate endDate = LocalDate.parse(transactionIntervalDto.getEndDate(), fmt);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        return ResponseEntity.ok(transactionService.getUserTransactionsInInterval(startDateTime, endDateTime));
    }

    @GetMapping("/customIntervalDetailed")
    public ResponseEntity<?> getCustomIntervalDetailedTransactions(@RequestBody TransactionIntervalDto transactionIntervalDto) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(transactionIntervalDto.getStartDate(), fmt);
        LocalDate endDate = LocalDate.parse(transactionIntervalDto.getEndDate(), fmt);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        return ResponseEntity.ok(transactionService.getDetailedUserTransactionsInInterval(startDateTime, endDateTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@RequestBody TransactionDto transactionDto, @PathVariable int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        return new ResponseEntity<>(transactionService.updateTransaction(transactionDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/thismonthcategory/{id}")
    public ResponseEntity<?> getTransactionTotalsCurrentMonthForCategory(@PathVariable int id) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();
        return new ResponseEntity<>(transactionService.getUserTransactionByDateAndTransactionCategory(month, year, id), HttpStatus.OK);
    }

}
