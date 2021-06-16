package com.dev.nc.ibank.controllers;

import com.dev.nc.ibank.models.Account;
import com.dev.nc.ibank.models.Transaction;
import com.dev.nc.ibank.utils.AccountUtil;
import com.dev.nc.ibank.models.Customer;
import com.dev.nc.ibank.models.TransactionRequest;
import com.dev.nc.ibank.services.AccountService;
import com.dev.nc.ibank.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(description = "Account API operations", name = "accounts")
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create a new account")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "201", description = "Account created"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account supplied")//wrap
    })
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Account create(@RequestBody @Valid Account account) {
        final Customer customer = account.getCustomer();
        Optional<Customer> optionalCustomer = Optional.empty();
        if (customer.getId() != null) {
            optionalCustomer = customerService.findById(customer.getId());
            optionalCustomer.ifPresent(account::setCustomer);
        }
        if (optionalCustomer.isEmpty()) {
            final String customerName = customer.getName();
            if (customerName != null && !customerName.trim().isEmpty()) {
                account.setCustomer(customerService.save(customer));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer!", null);
            }
        }
        return accountService.save(account);
    }

    @Operation(summary = "Retrieve all accounts")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All accounts retrieved"),//wrap
            @ApiResponse(responseCode = "500", description = "Internal error")//wrap
    })
    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public List<Account> all() {
        return accountService.findAll();
    }

    @Operation(summary = "Retrieve account by number")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All accounts retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account number"),//wrap
            @ApiResponse(responseCode = "404", description = "Account not found")//wrap
    })
    @GetMapping(value = "/{accountNumber}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity findByNumber(@Parameter(description = "account number to be searched")//wrap
                                       @PathVariable String accountNumber) {
        AccountUtil.validateAccountNumber(accountNumber);
        final Optional<Account> account = accountService.findByAccountNumber(accountNumber);
        return account.isPresent() ? ResponseEntity.ok(account.get()) : new ResponseEntity<>("Account not found!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Retrieve transactions by account number")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All transactions retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account number"),//wrap
            @ApiResponse(responseCode = "404", description = "Account not found")//wrap
    })
    @GetMapping(value = "/{accountNumber}/transactions", produces = APPLICATION_JSON_VALUE)
    public List<Transaction> transactions(@Parameter(description = "account number to be searched")//wrap
                                          @PathVariable String accountNumber) {
        accountService.checkAccount(accountNumber);
        return accountService.findAllTransactions(accountNumber);
    }

    @Operation(summary = "Retrieve withdrawals by account number")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All withdrawals retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account number"),//wrap
            @ApiResponse(responseCode = "404", description = "Account not found")//wrap
    })
    @GetMapping(value = "/{accountNumber}/withdrawals", produces = APPLICATION_JSON_VALUE)
    public List<Transaction> withdrawals(@Parameter(description = "account number to be searched")//wrap
                                         @PathVariable String accountNumber) {
        accountService.checkAccount(accountNumber);
        return accountService.findAllWithdrawals(accountNumber);
    }

    @Operation(summary = "Retrieve deposits by account number")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All deposits retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account number"),//wrap
            @ApiResponse(responseCode = "404", description = "Account not found")//wrap
    })
    @GetMapping(value = "/{accountNumber}/deposits", produces = APPLICATION_JSON_VALUE)
    public List<Transaction> deposits(@Parameter(description = "account number to be searched")//wrap
                                      @PathVariable String accountNumber) {
        accountService.checkAccount(accountNumber);
        return accountService.findAllDeposits(accountNumber);
    }

    @Operation(summary = "Transfer amount between accounts")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All deposits retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid account(s) supplied"),//wrap
            @ApiResponse(responseCode = "404", description = "Account(s) not found")//wrap
    })
    @PostMapping(value = "/transfer", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Transaction transfer(@RequestBody @Valid TransactionRequest transactionRequest) {
        String fromAccountNumber = transactionRequest.getFromAccount();
        String toAccountNumber = transactionRequest.getToAccount();
        Account fromAccount = accountService.checkAccount(fromAccountNumber);
        Account toAccount = accountService.checkAccount(toAccountNumber);
        BigDecimal amount = transactionRequest.getAmount();
        if (fromAccount.getCurrentBalance().compareTo(amount) >= 0) {
            final Transaction transaction = accountService.performTransfer(fromAccount, toAccount, amount);
            LOGGER.info("Transferred {} from: {} to: {}. Transaction ID: {}", amount, fromAccountNumber, toAccountNumber, transaction.getId());
            return transaction;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current balance not enough to process the transaction!", null);
        }
    }
}
