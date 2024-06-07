package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.AccountDto;
import com.treasury.treasuryhub.exception.AccountNotFoundException;
import com.treasury.treasuryhub.exception.TransactionCategoryNotFoundException;
import com.treasury.treasuryhub.exception.TransactionTypeNotSupportedException;
import com.treasury.treasuryhub.model.Account;
import com.treasury.treasuryhub.model.Transaction;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    public Account registerAccount(AccountDto accountDto) {
        User user = userService.fetchCurrentUser();
        Account newAccount = new Account();
        newAccount.setUserId(user.getId());
        newAccount.setBankName(accountDto.getBankName());
        newAccount.setAccountType(accountDto.getAccountType());
        newAccount.setAccountNumber(accountDto.getAccountNumber());
        newAccount.setBalance(accountDto.getBalance());

        return accountRepository.save(newAccount);
    }

    public Account getAccountById(int id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public List<Account> getAccountsByUser() {
        User user = userService.fetchCurrentUser();
        return accountRepository.getAccountByUserId(user.getId());
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account updateAccountBalance(int id, double balance) throws AccountNotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(balance);
        return accountRepository.save(account);
    }

    @Transactional
    public void registerTransactionAndUpdateBalance(Transaction transaction) throws AccountNotFoundException, TransactionTypeNotSupportedException, TransactionCategoryNotFoundException {
        if(transaction.getTransactionCategoryId() == null && transaction.getSourceAccountId() != null &&  transaction.getDestinationAccountId() != null) {
            registerTransfer(transaction.getSourceAccountId(), transaction.getDestinationAccountId(), transaction.getAmount());
        } else {
            String transactionType = transactionCategoryService.getTransactionCategoryById(transaction.getTransactionCategoryId()).getTransactionType();
            switch(transactionType) {
                case "INCOME":
                    registerIncome(transaction.getSourceAccountId(), transaction.getAmount());
                    break;

                case "EXPENSE":
                    registerExpense(transaction.getSourceAccountId(), transaction.getAmount());
                    break;

                default: throw new TransactionTypeNotSupportedException(transactionType);
            }
        }
    }

    @Transactional
    public void registerIncome(int id, double amount) throws AccountNotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Transactional
    public void registerExpense(int id, double amount) throws AccountNotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Transactional
    public void registerTransfer(int sourceAccountId, int destinationAccountId, double amount) throws AccountNotFoundException {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));
        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new AccountNotFoundException(destinationAccountId));
        double sourceBalance = sourceAccount.getBalance() - amount;
        double destinationBalance = destinationAccount.getBalance() + amount;
        sourceAccount.setBalance(sourceBalance);
        destinationAccount.setBalance(destinationBalance);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    @Transactional
    public Account updateAccount(AccountDto accountDto, int id) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setBankName(accountDto.getBankName());
                    account.setAccountType(accountDto.getAccountType());
                    account.setAccountNumber(accountDto.getAccountNumber());
                    account.setBalance(accountDto.getBalance());
                    return accountRepository.save(account);
                })
                .orElseGet(() -> registerAccount(accountDto));
    }

    public void deleteAccount(int id) throws AccountNotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
    }
}
