package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.dto.TransactionIntervalDto;
import com.treasury.treasuryhub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
    public ResponseEntity<?> registerTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.registerTransaction(transactionDto);
    }

    @GetMapping("/getByUser")
    public ResponseEntity<?> getUserTransactions() {
        return ResponseEntity.ok(transactionService.getUserTransactions());
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

}
