package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.FeedbackDto;
import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.FeedbackNotFoundException;
import com.treasury.treasuryhub.exception.TransactionNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.model.Feedback;
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
import java.util.List;

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
    public Transaction registerTransaction(TransactionDto transactionDto) throws AccountNotFoundException, TransactionTypeNotSupportedException {
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

        accountService.registerTransactionAndUpdateBalance(transaction);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(int id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> getUserTransactions() {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return transactionRepository.getTransactionsByUserId(user.getId());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getUserTransactionsInInterval(LocalDateTime startDate, LocalDateTime endDate) {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return transactionRepository.getTransactionByUserIdInInterval(user.getId(), startDate, endDate);
    }

    @Transactional
    public Transaction updateTransaction(TransactionDto transactionDto, int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException {
        Transaction transaction =  transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        double initialAmount = transaction.getAmount();
        transaction.setAmount(-initialAmount);
        accountService.registerTransactionAndUpdateBalance(transaction);
        transaction.setTransactionCategoryId(transactionDto.getTransactionCategoryId());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setSourceAccountId(transactionDto.getSourceAccountId());
        transaction.setDestinationAccountId(transactionDto.getDestinationAccountId());
        transaction.setDetails(transactionDto.getDetails());
        accountService.registerTransactionAndUpdateBalance(transaction);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        double initialAmount = transaction.getAmount();
        transaction.setAmount(-initialAmount);
        accountService.registerTransactionAndUpdateBalance(transaction);
        transactionRepository.delete(transaction);
    }
}
