package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.BudgetDto;
import com.treasury.treasuryhub.exception.BudgetNotFoundException;
import com.treasury.treasuryhub.model.Budget;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.BudgetRepository;
import com.treasury.treasuryhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserService userService;

    public Budget registerBudget(BudgetDto budgetDto) {
        User user = userService.fetchCurrentUser();
        Budget newBudget = new Budget();
        newBudget.setUserId(user.getId());
        newBudget.setTransactionCategoryId(budgetDto.getTransactionCategoryId());
        newBudget.setAmount(budgetDto.getAmount());

        return budgetRepository.save(newBudget);
    }

    public Budget getBudgetById(int id) throws BudgetNotFoundException {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
    }

    public List<Budget> getBudgetsByUser() {
        User user = userService.fetchCurrentUser();
        return budgetRepository.getBudgetByUserId(user.getId());
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget updateBudget(BudgetDto budgetDto, int id) {
        return budgetRepository.findById(id)
                .map(budget -> {
                    budget.setTransactionCategoryId(budgetDto.getTransactionCategoryId());
                    budget.setAmount(budgetDto.getAmount());
                    return budgetRepository.save(budget);
                })
                .orElseGet(() -> registerBudget(budgetDto));
    }

    public void deleteBudget(int id) throws BudgetNotFoundException {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        budgetRepository.delete(budget);
    }
}
