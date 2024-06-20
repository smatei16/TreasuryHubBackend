package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.UserStockDto;
import com.treasury.treasuryhub.exception.UserStockNotFoundException;
import com.treasury.treasuryhub.service.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user-stock")
public class UserStockController {

    @Autowired
    private UserStockService userStockService;

    @PostMapping("/save")
    public ResponseEntity<?> registerUserStock(@RequestBody UserStockDto userStockDto) {
        return new ResponseEntity<>(userStockService.registerUserStock(userStockDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserStockById(@PathVariable int id) throws UserStockNotFoundException {
        return new ResponseEntity<>(userStockService.getUserStockById(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserStocks() {
        return new ResponseEntity<>(userStockService.getUserStocks(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserStock(@RequestBody UserStockDto userStockDto, @PathVariable int id) {
        return new ResponseEntity<>(userStockService.updateUserStock(userStockDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserStock(@PathVariable int id) throws UserStockNotFoundException {
        userStockService.deleteUserStock(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
