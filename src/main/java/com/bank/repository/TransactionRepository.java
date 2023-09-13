package com.bank.repository;

import com.bank.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByBankAccountId(Long bankAccountId);

 //   Optional<Transaction> findByBankAccountId(Long bankAccountId);



}
