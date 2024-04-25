package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.TransactionCategoryDto;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.service.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return new ResponseEntity<>(transactionCategoryService.registerTransactionCategory(transactionCategoryDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionCategoryById(@PathVariable int id) throws TransactionCategoryNotFoundException {
        return new ResponseEntity<>(transactionCategoryService.getTransactionCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/getByUser")
    public ResponseEntity<?> getTransactionCategoriesByUser() {
        return new ResponseEntity<>(transactionCategoryService.getTransactionCategoriesByUser(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactionCategories() {
        return new ResponseEntity<>(transactionCategoryService.getAllTransactionCategories(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransactionCategory(@RequestBody TransactionCategoryDto transactionCategoryDto, @PathVariable int id) {
        return new ResponseEntity<>(transactionCategoryService.updateTransactionCategory(transactionCategoryDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionCategory(@PathVariable int id) throws TransactionCategoryNotFoundException {
        transactionCategoryService.deleteTransactionCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
