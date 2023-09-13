package com.bank.repository;

import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findAllByClient(Client client);

    Optional<BankAccount> findByAccountNumber(String accountNumber);
    Optional<BankAccount> findByIdAndClientId(Long id, Long clientId);


}
