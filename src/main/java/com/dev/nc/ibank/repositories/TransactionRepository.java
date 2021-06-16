package com.dev.nc.ibank.repositories;

import com.dev.nc.ibank.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByFromAccountEquals(String accountNumber);

    List<Transaction> findAllByToAccountEquals(String accountNumber);

    List<Transaction> findAllByFromAccountEqualsOrToAccountEqualsOrderByAmount(String fromAccount, String toAccount);
}
