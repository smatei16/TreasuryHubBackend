package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.TransactionCategoryDto;
import com.treasury.treasuryhub.model.TransactionCategory;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.TransactionCategoryRepository;
import com.treasury.treasuryhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class TransactionCategoryService {

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> registerTransactionCategory(TransactionCategoryDto transactionCategoryDto) {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        if(!Objects.equals(transactionCategoryDto.getTransactionType(), "INCOME") && !Objects.equals(transactionCategoryDto.getTransactionType(), "EXPENSE"))
            throw new RuntimeException("Transaction category type not supported");
        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setUserId(user.getId());
        transactionCategory.setName(transactionCategoryDto.getName());
        transactionCategory.setTransactionType(transactionCategoryDto.getTransactionType());
        transactionCategoryRepository.save(transactionCategory);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ArrayList<TransactionCategory> getUserTransactionCategories() {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return transactionCategoryRepository.getTransactionCategoriesByUserId(user.getId());
    }


}
