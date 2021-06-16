package com.dev.nc.ibank.repositories;

import com.dev.nc.ibank.models.Account;
import com.dev.nc.ibank.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumberEquals(String accountNumber);

    List<Account> findAllByCustomer(Customer customer);
}
