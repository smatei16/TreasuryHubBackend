package com.treasury.treasuryhub.config;

import com.treasury.treasuryhub.dto.CategoryTotalsDto;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.service.EmailService;
import com.treasury.treasuryhub.service.StockService;
import com.treasury.treasuryhub.service.TransactionService;
import com.treasury.treasuryhub.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateStocks() {
        stockService.updateStocks();
    }

    @Scheduled(cron = "0 0 9 1 * ?")
    public void sendMonthlyStatement() throws MessagingException {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            Map<String, Object> model = new HashMap<>();
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endDate = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            List<CategoryTotalsDto> categories = transactionService.getCategoryTotalsByDateAndType(startDate, endDate, "EXPENSE");
            Double expense = transactionService.getTotalsByDateAndType(startDate, endDate, "EXPENSE");
            Double income = transactionService.getTotalsByDateAndType(startDate, endDate, "INCOME");
            model.put("name", user.getFirstName());
            model.put("expenses", categories);
            model.put("totalIncome", income);
            model.put("totalExpense", expense);
            emailService.sendLastMonthStatus(user.getEmail(), model);
        }
    }
}
