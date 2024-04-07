package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.AccountBalanceUpdateDto;
import com.treasury.treasuryhub.dto.AccountDto;
import com.treasury.treasuryhub.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return accountService.registerAccount(accountDto);
    }

    @GetMapping("/getUserAll")
    public ResponseEntity<?> getCurrentUserAccounts() {
        return ResponseEntity.ok(accountService.getAccountsByUser());
    }

    @PostMapping("/balance")
    public ResponseEntity<?> updateAccountBalance(@RequestBody AccountBalanceUpdateDto balanceUpdateDto) {
        accountService.updateAccountBalance(balanceUpdateDto.getAccountId(), balanceUpdateDto.getBalance());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
