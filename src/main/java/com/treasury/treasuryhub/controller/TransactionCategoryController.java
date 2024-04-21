package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.TransactionCategoryDto;
import com.treasury.treasuryhub.service.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/transaction/category")
public class TransactionCategoryController {

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    @PostMapping("/save")
    public ResponseEntity<?> registerTransactionCategory(@RequestBody TransactionCategoryDto transactionCategoryDto) {
        return transactionCategoryService.registerTransactionCategory(transactionCategoryDto);
    }

    @GetMapping("/getByUser")
    public ResponseEntity<?> getUserTransactionCategories() {
        return ResponseEntity.ok(transactionCategoryService.getUserTransactionCategories());
    }
}
