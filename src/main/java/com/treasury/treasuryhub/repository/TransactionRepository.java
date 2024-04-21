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

    @Query(value = "select * from transaction t where t.user_id = ?1 and t.create_ts between ?2 and ?3", nativeQuery = true)
    ArrayList<Transaction> getTransactionByUserIdInInterval(int userId, LocalDateTime startDate, LocalDateTime endDate);


}
