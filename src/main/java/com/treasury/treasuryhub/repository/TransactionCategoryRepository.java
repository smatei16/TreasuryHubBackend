package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Integer> {

    ArrayList<TransactionCategory> getTransactionCategoriesByUserId(int userId);
}
