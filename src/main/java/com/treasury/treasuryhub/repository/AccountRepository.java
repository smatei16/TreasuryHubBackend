package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    ArrayList<Account> getAccountByUserId(int userId);

    @Modifying
    @Query("update Account a set a.balance = :balance where a.id = :id")
    @Transactional
    void setAccountBalanceById(@Param(value = "balance") double balance, @Param(value = "id") int id);

    Account getAccountById(int id);
}
