package com.dev.nc.ibank.controllers;

import com.dev.nc.ibank.AbstractIntegrationTest;
import com.dev.nc.ibank.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("database-cleanup")
class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    public static final String CUSTOMER_PATH = ROOT_PATH + "/customer/";
    private Customer customer;

    @BeforeEach
    protected void setCustomer() throws Exception {
        customer = postNewCustomer(new Customer(TEST));
    }

    @Test
    void createAndGetAll() throws Exception {
        getAndExpectCustomer(customer);
        deleteCustomer(customer);
    }

    @Test
    void delete() throws Exception {
        getAndExpectCustomer(customer);
        deleteCustomer(customer);
        mvc.perform(get(CUSTOMER_PATH + "all"))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void accounts() throws Exception {
        mvc.perform(get(CUSTOMER_PATH + customer.getId() + "/accounts"))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$", hasSize(0)));
        deleteCustomer(customer);
    }

    private Customer postNewCustomer(Customer customer) throws Exception {
        final MvcResult mvcResult = mvc.perform(post(CUSTOMER_PATH + "create")//wrap
                .contentType(MediaType.APPLICATION_JSON)//wrap
                .content(objectMapper.writeValueAsString(customer)))//wrap
                .andExpect(status().isCreated())//wrap
                .andExpect(jsonPath("$.name").value(customer.getName()))//wrap
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        return mapToObject(mvcResult, Customer.class);
    }

    private void getAndExpectCustomer(Customer customer) throws Exception {
        mvc.perform(get(CUSTOMER_PATH + "all"))//wrap
                .andExpect(status().isOk())//wrap
                .andExpect(jsonPath("$", hasSize(1)))//wrap
                .andExpect(jsonPath("$[0].name").value(customer.getName()))//wrap
                .andExpect(jsonPath("$[0].id").value(customer.getId()));
    }

    private void deleteCustomer(Customer customer) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/customer/" + customer.getId()))//wrap
                .andExpect(status().isOk());
    }
}