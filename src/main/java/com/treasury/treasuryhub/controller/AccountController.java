package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.AccountBalanceUpdateDto;
import com.treasury.treasuryhub.dto.AccountDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/save")
    public ResponseEntity<?> registerAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.registerAccount(accountDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable int id) throws AccountNotFoundException {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/user-all")
    public ResponseEntity<?> getCurrentUserAccounts() {
        return new ResponseEntity<>(accountService.getAccountsByUser(), HttpStatus.OK);
    }

    @PutMapping("/balance")
    public ResponseEntity<?> updateAccountBalance(@RequestBody AccountBalanceUpdateDto balanceUpdateDto) throws AccountNotFoundException {
        return new ResponseEntity<>(accountService.updateAccountBalance(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getBalance()),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDto accountDto, @PathVariable int id) {
        return new ResponseEntity<>(accountService.updateAccount(accountDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable int id) throws AccountNotFoundException {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
