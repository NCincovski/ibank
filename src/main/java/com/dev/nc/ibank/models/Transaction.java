package com.dev.nc.ibank.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    private LocalDateTime time;
    private String fromAccount;
    private String toAccount;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, LocalDateTime time, String fromAccount, String toAccount) {
        this.amount = amount;
        this.time = time;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String accountNumber) {
        this.fromAccount = accountNumber;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
