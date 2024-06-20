package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface UserStockRepository extends JpaRepository<UserStock, Integer> {

    ArrayList<UserStock> getUserStockByUserId(int userId);
}
