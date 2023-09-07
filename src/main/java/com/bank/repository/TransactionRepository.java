package com.bank.repository;

import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByBankAccount (BankAccount bankAccount);

    List<Transaction> findAllByBankAccountId(Long bankAccountId);

}
