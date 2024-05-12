package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    ArrayList<Budget> getBudgetByUserId(int userId);
}
