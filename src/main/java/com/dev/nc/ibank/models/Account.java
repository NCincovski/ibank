package com.dev.nc.ibank.models;


import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String accountNumber = UUID.randomUUID().toString();
    @DecimalMin(value = "0.0")
    private BigDecimal currentBalance = BigDecimal.valueOf(0.0);
    // @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    // @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    // @JsonProperty
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
