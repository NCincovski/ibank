package com.dev.nc.ibank.controllers;

import com.dev.nc.ibank.models.Account;
import com.dev.nc.ibank.models.Customer;
import com.dev.nc.ibank.services.AccountService;
import com.dev.nc.ibank.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(description = "Customer API operations", name = "customers")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new customer")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "201", description = "Customer created"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid customer supplied")//wrap
    })
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Customer create(@RequestBody @Valid Customer customer) {
        return customerService.save(customer);
    }

    @Operation(summary = "Retrieve all customers")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All customers retrieved"),//wrap
            @ApiResponse(responseCode = "500", description = "Internal error")//wrap
    })
    @GetMapping("/all")
    public List<Customer> all() {
        return customerService.findAll();
    }

    @Operation(summary = "Delete customer by id")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "Customer deleted"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid customer id"),//wrap
            @ApiResponse(responseCode = "404", description = "Customer not found")//wrap
    })
    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable Long customerId) {
        customerService.checkCustomer(customerId);
        customerService.delete(customerId);
    }

    @Operation(summary = "Retrieve customer accounts")
    @ApiResponses({//wrap
            @ApiResponse(responseCode = "200", description = "All customer accounts retrieved"),//wrap
            @ApiResponse(responseCode = "400", description = "Invalid customer id"),//wrap
            @ApiResponse(responseCode = "404", description = "Customer not found")//wrap
    })
    @GetMapping("/{customerId}/accounts")
    public List<Account> accounts(@PathVariable Long customerId) {
        final Customer customer = customerService.checkCustomer(customerId);
        return accountService.findByCustomer(customer);
    }
}
