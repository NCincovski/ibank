package com.dev.nc.ibank.services;

import com.dev.nc.ibank.models.Transaction;
import com.dev.nc.ibank.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service component that provides operations related to the {@link Transaction} class: creating, searching, removing
 */
@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
