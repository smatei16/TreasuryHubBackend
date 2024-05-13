package com.treasury.treasuryhub.config;

import com.treasury.treasuryhub.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private StockService stockService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateStocks() {
        stockService.updateStocks();
    }
}
