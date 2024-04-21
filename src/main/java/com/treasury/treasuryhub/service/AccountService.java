package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.AccountDto;
import com.treasury.treasuryhub.model.Account;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.AccountRepository;
import com.treasury.treasuryhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<?> registerAccount(AccountDto accountDto) {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        Account newAccount = new Account();
        newAccount.setUserId(user.getId());
        newAccount.setBankName(accountDto.getBankName());
        newAccount.setAccountType(accountDto.getAccountType());
        newAccount.setAccountNumber(accountDto.getAccountNumber());
        newAccount.setBalance(accountDto.getBalance());
        accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ArrayList<Account> getAccountsByUser() {
        UserDetails userDetails = userService.fetchCurrentUser();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return accountRepository.getAccountByUserId(user.getId());
    }

    public void updateAccountBalance(int accountId, double balance) {
        Account account = accountRepository.getAccountById(accountId);
        account.setBalance(balance);
        accountRepository.save(account);
    }

    public void registerIncome(int accountId, double amount) {
        Account account = accountRepository.getAccountById(accountId);
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public void registerExpense(int accountId, double amount) {
        Account account = accountRepository.getAccountById(accountId);
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public void registerTransfer(int sourceAccountId, int destinationAccountId, double amount) {
        Account sourceAccount = accountRepository.getAccountById(sourceAccountId);
        Account destinationAccount = accountRepository.getAccountById(destinationAccountId);
        double sourceBalance = sourceAccount.getBalance() - amount;
        double destinationBalance = destinationAccount.getBalance() + amount;
        sourceAccount.setBalance(sourceBalance);
        destinationAccount.setBalance(destinationBalance);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }
}
