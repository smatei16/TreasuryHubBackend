package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/update")
    public ResponseEntity<?> updateStocks() {
        stockService.updateStocks();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
