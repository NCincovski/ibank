package com.dev.nc.ibank.controllers;

import com.dev.nc.ibank.AbstractIntegrationTest;
import com.dev.nc.ibank.models.Account;
import com.dev.nc.ibank.models.Transaction;
import com.dev.nc.ibank.models.Customer;
import com.dev.nc.ibank.models.TransactionRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    public static final String ACCOUNT_PATH = ROOT_PATH + "/account/";

    @Test
    void createAndGetAll() throws Exception {
        final Account newAccount = postNewAccount(createAccount(TEST, 0.0));
        assertThat(newAccount).isNotNull();

        mvc.perform(get(ACCOUNT_PATH + "all"))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$", hasSize(1)))//wrap
                .andExpect(jsonPath("$[0].accountNumber").value(newAccount.getAccountNumber()))//wrap
                .andExpect(jsonPath("$[0].currentBalance").value(newAccount.getCurrentBalance()));
    }

    @Test
    void findByNumber() throws Exception {
        mvc.perform(get(ACCOUNT_PATH + UUID.randomUUID())).andExpect(status().isNotFound());
        final Account account = postNewAccount(createAccount(TEST, 0.0));
        mvc.perform(get(ACCOUNT_PATH + account.getAccountNumber())).andExpect(status().isOk());
    }

    @Test
    void transactions() throws Exception {
        final String from = postNewAccount(createAccount("From", 1000)).getAccountNumber();
        final String to = postNewAccount(createAccount("To", 0)).getAccountNumber();
        performTransactionStatement(from, to, 500, from + "/transactions");
    }

    @Test
    void withdrawals() throws Exception {
        final String from = postNewAccount(createAccount("From", 1000)).getAccountNumber();
        final String to = postNewAccount(createAccount("To", 0)).getAccountNumber();
        performTransactionStatement(from, to, 500, from + "/withdrawals");
    }

    @Test
    void deposits() throws Exception {
        final String from = postNewAccount(createAccount("From", 1000)).getAccountNumber();
        final String to = postNewAccount(createAccount("To", 0)).getAccountNumber();
        performTransactionStatement(from, to, 500, to + "/deposits");
    }

    @Test
    void transfer() throws Exception {
        final Account from = postNewAccount(createAccount("From", 1000));
        final Account to = postNewAccount(createAccount("To", 0));
        postNewTransaction(from.getAccountNumber(), to.getAccountNumber(), 500);
    }

    private static Account createAccount(String customerName, double currentBalance) {
        Account account = new Account();
        account.setCustomer(new Customer(customerName));
        account.setCurrentBalance(BigDecimal.valueOf(currentBalance));
        return account;
    }

    private Account postNewAccount(Account account) throws Exception {
        final MvcResult mvcResult = mvc.perform(post(ACCOUNT_PATH + "create")//wrap
                .contentType(MediaType.APPLICATION_JSON)//wrap
                .content(objectMapper.writeValueAsString(account)))//wrap
                .andExpect(status().isCreated())//wrap
                .andExpect(jsonPath("$.accountNumber", Matchers.hasLength(36)))//wrap
                .andExpect(jsonPath("$.currentBalance").value(account.getCurrentBalance()))//wrap
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        return mapToObject(mvcResult, Account.class);
    }

    private Transaction postNewTransaction(String from, String to, double amount) throws Exception {
        TransactionRequest request = new TransactionRequest(from, to, BigDecimal.valueOf(amount));
        final MvcResult mvcResult = mvc.perform(post(ACCOUNT_PATH + "/transfer")//wrap
                .contentType(MediaType.APPLICATION_JSON)//wrap
                .content(objectMapper.writeValueAsString(request)))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$.fromAccount").value(from))//wrap
                .andExpect(jsonPath("$.toAccount").value(to))//wrap
                .andExpect(jsonPath("$.amount").value(amount)).andReturn();

        return mapToObject(mvcResult, Transaction.class);
    }

    private void performTransactionStatement(String from, String to, double amount, String requestPath) throws Exception {
        String path = ACCOUNT_PATH + requestPath;
        mvc.perform(get(path)).andExpect(status().isOk()).andExpect(content().json("[]"));

        postNewTransaction(from, to, amount);
        mvc.perform(get(path))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$", hasSize(1)))//wrap
                .andExpect(jsonPath("$[0].fromAccount").value(from))//wrap
                .andExpect(jsonPath("$[0].toAccount").value(to))//wrap
                .andExpect(jsonPath("$[0].amount").value(amount));
    }
}