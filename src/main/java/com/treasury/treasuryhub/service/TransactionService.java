package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.*;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.exception.TransactionNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.model.Transaction;
import com.treasury.treasuryhub.model.TransactionCategory;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionCategoryService transactionCategoryService;

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
        transaction.setUrl(transactionDto.getUrl());

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
        ArrayList<Object[]> results =  transactionRepository.getDetailedTransactionById(id);
        return convertObjectsToDetailedDto(results).get(0);
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
        transaction.setUrl(transactionDto.getUrl());
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

    public List<CategoryTotalsDto> getCategoryTotalsByDateAndType(LocalDate startDate, LocalDate endDate, String type) {
        User user = userService.fetchCurrentUser();
        return convertObjectsToCategoryTotals(transactionRepository.getCategoryTotalsByMonthAndType(user.getId(), startDate, endDate, type));
    }

    public List<CategoryTotalsDto> getCategoryTotalsByDate(LocalDate startDate, LocalDate endDate) {
        User user = userService.fetchCurrentUser();
        return convertObjectsToCategoryTotals(transactionRepository.getCategoryTotalsByMonth(user.getId(), startDate, endDate));
    }

    public Double getTotalsByDateAndType(LocalDate startDate, LocalDate endDate, String type) {
        User user = userService.fetchCurrentUser();
        return transactionRepository.getTotalsByMonthAndType(user.getId(), startDate, endDate, type);
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
                        (Timestamp) result[12],
                        (String) result[13],
                        (String) result[14],
                        (String) result[15])
        ).collect(Collectors.toList());
    }

    public List<CategoryTotalsDto> convertObjectsToCategoryTotals(List<Object[]> results) {
        return results.stream().map(result ->
                new CategoryTotalsDto((Integer) result[0],
                        (String) result[1],
                        (String) result[2],
                        (Double) result[3],
                        (Double) result[4])
        ).collect(Collectors.toList());
    }

    public MonthlySummaryResponseDto getMonthlySummary() {
        LocalDate time = LocalDate.now();
        LocalDateTime endDate = time.atTime(LocalTime.MAX);
        LocalDateTime startDate = time.minusMonths(11).withDayOfMonth(1).atTime(LocalTime.MIN);
        User user = userService.fetchCurrentUser();
        List<DetailedTransactionResponseDto> transactions = convertObjectsToDetailedDto(transactionRepository.getDetailedTransactionByUserIdInInterval(user.getId(), startDate, endDate));

        List<String> months = new ArrayList<>();
        List<Double> totalExpenses = new ArrayList<>();
        List<Double> totalIncomes = new ArrayList<>();

        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.now().minusMonths(i);
            months.add(yearMonth.toString());

            LocalDate start = yearMonth.atDay(1);
            LocalDate end = yearMonth.atEndOfMonth();

            Map<String, Double> totals = transactions.stream()
                    .filter(t -> !t.getDate().toLocalDateTime().toLocalDate().isBefore(start) && !t.getDate().toLocalDateTime().toLocalDate().isAfter(end))
                    .collect(Collectors.groupingBy(DetailedTransactionResponseDto::getType, Collectors.summingDouble(DetailedTransactionResponseDto::getAmount)));

            totalExpenses.add(totals.getOrDefault("EXPENSE", 0.0));
            totalIncomes.add(totals.getOrDefault("INCOME", 0.0));
        }
        return new MonthlySummaryResponseDto(months, totalExpenses, totalIncomes);
    }

    public CategorySummaryResponseDto getCategorySummary(String type) {
        List<TransactionCategory> categories = transactionCategoryService.getTransactionCategoriesByUser();
        LocalDate time = LocalDate.now();
        LocalDateTime endDate = time.atTime(LocalTime.MAX);
        LocalDateTime startDate = time.withDayOfMonth(1).atTime(LocalTime.MIN);
        User user = userService.fetchCurrentUser();
        List<DetailedTransactionResponseDto> transactions = convertObjectsToDetailedDto(transactionRepository.getDetailedTransactionByUserIdInInterval(user.getId(), startDate, endDate));

        if (type != null && !type.isEmpty()) {
            transactions = transactions.stream()
                    .filter(t -> t.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            categories = categories.stream()
                    .filter(c -> c.getTransactionType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        List<String> categoryNames = categories.stream().map(TransactionCategory::getName).collect(Collectors.toList());
        List<Double> categoryBudgets = categories.stream().map(TransactionCategory::getBudget).collect(Collectors.toList());

        Map<Integer, Double> transactionTotals = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionCategoryId(), Collectors.summingDouble(DetailedTransactionResponseDto::getAmount)));

        List<Double> totalPerCategory = categories.stream()
                .map(category -> transactionTotals.getOrDefault(category.getId(), 0.0))
                .collect(Collectors.toList());

        return new CategorySummaryResponseDto(categoryNames, totalPerCategory, categoryBudgets);
    }

    public MonthlyCategoryResponseDto getMonthlyCategorySummary(Integer categoryId) {
        LocalDate time = LocalDate.now();
        LocalDateTime endDate = time.atTime(LocalTime.MAX);
        LocalDateTime startDate = time.minusMonths(11).withDayOfMonth(1).atTime(LocalTime.MIN);
        User user = userService.fetchCurrentUser();
        List<DetailedTransactionResponseDto> transactions = convertObjectsToDetailedDto(transactionRepository.getDetailedTransactionByUserIdInInterval(user.getId(), startDate, endDate));

        List<String> months = new ArrayList<>();
        List<Double> monthlyExpenses = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            YearMonth yearMonth = YearMonth.now().minusMonths(i);
            months.add(yearMonth.toString());

            LocalDate start = yearMonth.atDay(1);
            LocalDate end = yearMonth.atEndOfMonth();

            double totalExpense = transactions.stream()
                    .filter(t -> t.getTransactionCategoryId() != null)
                    .filter(t -> t.getTransactionCategoryId().equals(categoryId))
                    .filter(t -> !t.getDate().toLocalDateTime().toLocalDate().isBefore(start) && !t.getDate().toLocalDateTime().toLocalDate().isAfter(end))
                    .mapToDouble(DetailedTransactionResponseDto::getAmount)
                    .sum();

            monthlyExpenses.add(totalExpense);
        }

        return new MonthlyCategoryResponseDto(months, monthlyExpenses);
    }
}
