package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.BudgetDto;
import com.treasury.treasuryhub.exception.BudgetNotFoundException;
import com.treasury.treasuryhub.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/save")
    public ResponseEntity<?> registerBudget(@RequestBody BudgetDto budgetDto) {
        return new ResponseEntity<>(budgetService.registerBudget(budgetDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable int id) throws BudgetNotFoundException {
        return new ResponseEntity<>(budgetService.getBudgetById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBudgets() {
        return new ResponseEntity<>(budgetService.getAllBudgets(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUserBudgets() {
        return new ResponseEntity<>(budgetService.getBudgetsByUser(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBudget(@RequestBody BudgetDto budgetDto, @PathVariable int id) {
        return new ResponseEntity<>(budgetService.updateBudget(budgetDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable int id) throws BudgetNotFoundException {
        budgetService.deleteBudget(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
