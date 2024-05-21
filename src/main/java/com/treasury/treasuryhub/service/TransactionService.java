package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.TransactionNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.model.Transaction;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Transaction registerTransaction(TransactionDto transactionDto) throws AccountNotFoundException, TransactionTypeNotSupportedException {
        User user = userService.fetchCurrentUser();
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTransactionCategoryId(transactionDto.getTransactionCategoryId());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setSourceAccountId(transactionDto.getSourceAccountId());
        transaction.setDestinationAccountId(transactionDto.getDestinationAccountId());
        transaction.setDetails(transactionDto.getDetails());
        transaction.setDate(LocalDateTime.parse(transactionDto.getDate()));

        accountService.registerTransactionAndUpdateBalance(transaction);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(int id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> getUserTransactions() {
        User user = userService.fetchCurrentUser();
        return transactionRepository.getTransactionsByUserId(user.getId());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getUserTransactionsInInterval(LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.fetchCurrentUser();
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
        transaction.setDate(LocalDateTime.parse(transactionDto.getDate()));
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

    public double getUserTransactionByDateAndTransactionCategory(int month, int year, int categoryId) {
        User user = userService.fetchCurrentUser();
        return transactionRepository.getTotalTransactionsSumByCategoryByMonth(user.getId(), month, year, categoryId);
    }
}
