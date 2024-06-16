package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> findBySymbol(String symbol);

    @Query(value = "select *  from th1.stock s where lower(s.symbol) like %?1% or lower(s.name) like %?1%", nativeQuery = true)
    ArrayList<Stock> getStocksByQuery(String query);
}
