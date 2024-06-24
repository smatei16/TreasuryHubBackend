package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.dto.DetailedTransactionResponseDto;
import com.treasury.treasuryhub.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    ArrayList<Transaction> getTransactionsByUserId(int userId);

    @Query(value = "select * from th1.transaction t where t.user_id = ?1 and t.date between ?2 and ?3", nativeQuery = true)
    ArrayList<Transaction> getTransactionByUserIdInInterval(int userId, LocalDateTime startDate, LocalDateTime endDate);

    //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
    @Query(value = "select sum(amount) from th1.transaction t where t.user_id = ?1 and extract(month from t.date) = ?2 and extract(year from t.date) = ?3 and t.transaction_category_id = ?4", nativeQuery = true)
    double getTotalTransactionsSumByCategoryByMonth(int userId, int month, int year, int categoryId);



//    @Query(value = "SELECT " +
//            "t.id, t.transaction_category_id transactionCategoryId, tc.name name, t.amount amount, t.source_account_id sourceAccountId, a1.bank_name sourceAccountBankName, t.destination_account_id destinationAccountId, a2.bank_name destinationAccountBankName, t.details details, t.date date " +
//            "FROM th1.transaction t " +
//            "LEFT JOIN th1.transaction_category tc ON t.transaction_category_id = tc.id " +
//            "LEFT JOIN th1.account a1 ON t.source_account_id =  a1.id " +
//            "LEFT JOIN th1.account a2 ON t.destination_account_id = a2.id " +
//            "WHERE t.user_id = ?1", nativeQuery = true)
//    ArrayList<Object[]> getDetailedTransactionByUserId(int userId);

    @Query(value = "select * from th1.v_ifout_detailed_transactions t where t.user_id = ?1 order by date desc", nativeQuery = true)
    ArrayList<Object[]> getDetailedTransactionByUserId(int userId);

    @Query(value = "select * from th1.v_ifout_detailed_transactions t where t.id = ?1", nativeQuery = true)
    ArrayList<Object[]> getDetailedTransactionById(int id);

    @Query(value = "select * from th1.v_ifout_detailed_transactions t where t.user_id = ?1 and t.date between ?2 and ?3 order by date desc", nativeQuery = true)
    ArrayList<Object[]> getDetailedTransactionByUserIdInInterval(int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "select t.transaction_category_id, tc.name, tc.transaction_type, tc.budget, sum(t.amount) from th1.transaction t right outer join th1.transaction_category tc" +
            " on t.transaction_category_id = tc.id" +
            " where t.user_id = ?1" +
            " and t.date between ?2 and ?3" +
            " and tc.transaction_type = ?4" +
            " group by t.transaction_category_id, tc.name, tc.transaction_type, tc.budget" +
            " order by sum(t.amount) desc", nativeQuery = true)
    ArrayList<Object[]> getCategoryTotalsByMonthAndType(int userId, LocalDate startDate, LocalDate endDate, String type);

    @Query(value = "select t.transaction_category_id, tc.name, tc.transaction_type, tc.budget, sum(t.amount) from th1.transaction t right outer join th1.transaction_category tc" +
            " on t.transaction_category_id = tc.id" +
            " where t.user_id = ?1" +
            " and t.date between ?2 and ?3" +
            " group by t.transaction_category_id, tc.name, tc.transaction_type, tc.budget" +
            " order by sum(t.amount) desc", nativeQuery = true)
    ArrayList<Object[]> getCategoryTotalsByMonth(int userId, LocalDate startDate, LocalDate endDate);

    @Query(value = "select sum(t.amount) from th1.transaction t" +
            " join th1.transaction_category tc" +
            " on t.transaction_category_id = tc.id" +
            " where t.user_id = ?1" +
            " and t.date between ?2 and ?3" +
            " and tc.transaction_type = ?4", nativeQuery = true)
    Double getTotalsByMonthAndType(int userId, LocalDate startDate, LocalDate endDate, String type);
}
