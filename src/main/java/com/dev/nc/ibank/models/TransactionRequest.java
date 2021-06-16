package com.dev.nc.ibank.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Model class that represents {@link Transaction} request to transfer funds between two {@link Account}s
 */
public class TransactionRequest {
    @NotBlank(message = "Sender account is required!")
    private String fromAccount;
    @NotBlank(message = "Receiving account is required!")
    private String toAccount;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "The amount must be greater than 0.0!")
    private BigDecimal amount;

    public TransactionRequest() {
    }

    public TransactionRequest(String fromAccount, String toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
