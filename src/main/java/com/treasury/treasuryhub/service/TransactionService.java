package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.model.Transaction;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionRepository;
import com.treasury.treasuryhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public ResponseEntity<?> registerTransaction(TransactionDto transactionDto) {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTransactionCategoryId(transactionDto.getTransactionCategoryId());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setSourceAccountId(transactionDto.getSourceAccountId());
        transaction.setDestinationAccountId(transactionDto.getDestinationAccountId());
        transaction.setDetails(transactionDto.getDetails());

        switch(transaction.getType()) {
            case "INCOME":
                accountService.registerIncome(transaction.getSourceAccountId(), transaction.getAmount());
                break;

            case "EXPENSE":
                accountService.registerExpense(transaction.getSourceAccountId(), transaction.getAmount());
                break;

            case "TRANSFER":
                accountService.registerTransfer(transaction.getSourceAccountId(), transaction.getDestinationAccountId(), transaction.getAmount());
                break;

            default: throw new RuntimeException("Operation not supported.");
        }
        transactionRepository.save(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ArrayList<Transaction> getUserTransactions() {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return transactionRepository.getTransactionsByUserId(user.getId());
    }

    @Transactional
    public ArrayList<Transaction> getUserTransactionsInInterval(LocalDateTime startDate, LocalDateTime endDate) {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return transactionRepository.getTransactionByUserIdInInterval(user.getId(), startDate, endDate);

    }
}
