package com.bank.repository;

import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findAllByClient(Client client);

    Optional<BankAccount> findByAccountNumber(String accountNumber);
    Optional<BankAccount> findByAccountNumberAndClient(String accountNumber, Client client);


}
