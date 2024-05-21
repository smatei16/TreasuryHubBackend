package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    ArrayList<Transaction> getTransactionsByUserId(int userId);

    @Query(value = "select * from th1.transaction t where t.user_id = ?1 and t.create_ts between ?2 and ?3", nativeQuery = true)
    ArrayList<Transaction> getTransactionByUserIdInInterval(int userId, LocalDateTime startDate, LocalDateTime endDate);

    //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
    @Query(value = "select sum(amount) from th1.transaction t where t.user_id = ?1 and extract(month from t.date) = ?2 and extract(year from t.date) = ?3 and t.transaction_category_id = ?4", nativeQuery = true)
    double getTotalTransactionsSumByCategoryByMonth(int userId, int month, int year, int categoryId);


}
