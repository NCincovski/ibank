package com.dev.nc.ibank.services;

import com.dev.nc.ibank.models.Account;
import com.dev.nc.ibank.models.Customer;
import com.dev.nc.ibank.models.Transaction;
import com.dev.nc.ibank.repositories.AccountRepository;
import com.dev.nc.ibank.repositories.TransactionRepository;
import com.dev.nc.ibank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service component that provides operations related to the {@link Account} class:
 * creating, searching, checking, executing transactions
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumberEquals(accountNumber);
    }

    public List<Account> findByCustomer(Customer customer) {
        return accountRepository.findAllByCustomer(customer);
    }

    public List<Transaction> findAllTransactions(String accountNumber) {
        return transactionRepository.findAllByFromAccountEqualsOrToAccountEqualsOrderByAmount(accountNumber, accountNumber);
    }

    public List<Transaction> findAllWithdrawals(String accountNumber) {
        return transactionRepository.findAllByFromAccountEquals(accountNumber);
    }

    public List<Transaction> findAllDeposits(String accountNumber) {
        return transactionRepository.findAllByToAccountEquals(accountNumber);
    }

    @Transactional
    public Transaction performTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(amount));
        this.save(fromAccount);
        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(amount));
        this.save(toAccount);
        return transactionRepository.save(//wrap
                new Transaction(amount, LocalDateTime.now(), fromAccount.getAccountNumber(), toAccount.getAccountNumber()));
    }

    public Account checkAccount(String accountNumber) {
        AccountUtil.validateAccountNumber(accountNumber);
        final Optional<Account> account = this.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!", null);
        }
        return account.get();
    }
}
