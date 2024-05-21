package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.TransactionCategoryDto;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.model.TransactionCategory;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionCategoryService {

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private UserService userService;

    public TransactionCategory registerTransactionCategory(TransactionCategoryDto transactionCategoryDto) {
        User user = userService.fetchCurrentUser();
        //TODO 405 not allowed
        if(!Objects.equals(transactionCategoryDto.getTransactionType(), "INCOME") && !Objects.equals(transactionCategoryDto.getTransactionType(), "EXPENSE"))
            throw new RuntimeException("Transaction category type not supported");
        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setUserId(user.getId());
        transactionCategory.setName(transactionCategoryDto.getName());
        transactionCategory.setTransactionType(transactionCategoryDto.getTransactionType());
        //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
        transactionCategory.setBudget(transactionCategoryDto.getBudget());

        return transactionCategoryRepository.save(transactionCategory);
    }

    public TransactionCategory getTransactionCategoryById(int id) throws TransactionCategoryNotFoundException {
        return transactionCategoryRepository.findById(id)
                .orElseThrow(() -> new TransactionCategoryNotFoundException(id));
    }

    public List<TransactionCategory> getTransactionCategoriesByUser() {
        User user = userService.fetchCurrentUser();
        return transactionCategoryRepository.getTransactionCategoriesByUserId(user.getId());
    }

    public List<TransactionCategory> getAllTransactionCategories() {
        return transactionCategoryRepository.findAll();
    }

    public TransactionCategory updateTransactionCategory(TransactionCategoryDto transactionCategoryDto, int id) {
        return transactionCategoryRepository.findById(id)
                .map(transactionCategory -> {
                    transactionCategory.setName(transactionCategoryDto.getName());
                    transactionCategory.setTransactionType(transactionCategoryDto.getTransactionType());
                    //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
                    transactionCategory.setBudget(transactionCategoryDto.getBudget());
                    return transactionCategoryRepository.save(transactionCategory);
                }).orElseGet(() -> registerTransactionCategory(transactionCategoryDto));
    }

    public void deleteTransactionCategory(int id) throws TransactionCategoryNotFoundException {
        TransactionCategory transactionCategory = transactionCategoryRepository.findById(id)
                .orElseThrow(() -> new TransactionCategoryNotFoundException(id));
        transactionCategoryRepository.delete(transactionCategory);
    }

}
