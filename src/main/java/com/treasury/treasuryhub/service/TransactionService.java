package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.DetailedTransactionResponseDto;
import com.treasury.treasuryhub.dto.TransactionDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.exception.TransactionNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.model.Transaction;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Transaction registerTransaction(TransactionDto transactionDto) throws AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        User user = userService.fetchCurrentUser();
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTransactionCategoryId(transactionDto.getTransactionCategoryId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setSourceAccountId(transactionDto.getSourceAccountId());
        transaction.setDestinationAccountId(transactionDto.getDestinationAccountId());
        transaction.setMerchant(transactionDto.getMerchant());
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

    public List<DetailedTransactionResponseDto> getDetailedUserTransactions() {
        User user = userService.fetchCurrentUser();
        ArrayList<Object[]> results = transactionRepository.getDetailedTransactionByUserId(user.getId());
        return convertObjectsToDetailedDto(results);
        //this solution is working, except when category_id is null
//        ArrayList<Object[]> results = transactionRepository.getDetailedTransactionByUserId(user.getId());
//        return results.stream().map(result -> new DetailedTransactionResponseDto((int) result[0], (Integer) result[1], (String) result[2],
//                (Double) result[3], (Integer) result[4], (String) result[5], (Integer) result[6], (String) result[7],
//                (String) result[8], (Timestamp) result[9])).collect(Collectors.toList());

    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getUserTransactionsInInterval(LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.fetchCurrentUser();
        return transactionRepository.getTransactionByUserIdInInterval(user.getId(), startDate, endDate);
    }

    public List<DetailedTransactionResponseDto> getDetailedUserTransactionsInInterval(LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.fetchCurrentUser();
        ArrayList<Object[]> results =  transactionRepository.getDetailedTransactionByUserIdInInterval(user.getId(), startDate, endDate);
        return convertObjectsToDetailedDto(results);
    }

    public DetailedTransactionResponseDto getDetailedTransactionById(int id) {
        Object[] result =  transactionRepository.getDetailedTransactionById(id);
        return new DetailedTransactionResponseDto((Integer) result[0],
                (Integer) result[1],
                (Integer) result[2],
                (String) result[3],
                (String) result[4],
                (Double) result[5],
                (Integer) result[6],
                (String) result[7],
                (Integer) result[8],
                (String) result[9],
                (String) result[10],
                (String) result[11],
                (Timestamp) result[12])
    }

    @Transactional
    public Transaction updateTransaction(TransactionDto transactionDto, int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        Transaction transaction =  transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        double initialAmount = transaction.getAmount();
        transaction.setAmount(-initialAmount);
        accountService.registerTransactionAndUpdateBalance(transaction);
        transaction.setTransactionCategoryId(transactionDto.getTransactionCategoryId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setSourceAccountId(transactionDto.getSourceAccountId());
        transaction.setDestinationAccountId(transactionDto.getDestinationAccountId());
        transaction.setMerchant(transactionDto.getMerchant());
        transaction.setDetails(transactionDto.getDetails());
        transaction.setDate(LocalDateTime.parse(transactionDto.getDate()));
        accountService.registerTransactionAndUpdateBalance(transaction);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(int id) throws TransactionNotFoundException, AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
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

    public List<DetailedTransactionResponseDto> convertObjectsToDetailedDto(List<Object[]> results) {
        return results.stream().map(result ->
                new DetailedTransactionResponseDto((Integer) result[0],
                        (Integer) result[1],
                        (Integer) result[2],
                        (String) result[3],
                        (String) result[4],
                        (Double) result[5],
                        (Integer) result[6],
                        (String) result[7],
                        (Integer) result[8],
                        (String) result[9],
                        (String) result[10],
                        (String) result[11],
                        (Timestamp) result[12])
        ).collect(Collectors.toList());
    }
}
