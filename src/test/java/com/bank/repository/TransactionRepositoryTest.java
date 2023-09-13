package com.bank.repository;

import com.bank.model.entity.*;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;
    private Client clientRobby;
    private Manager managerDorota;

    private Product productPLN;
    private BankAccount bankAccountPLN;

    private Transaction transactionA;

    private Transaction transactionB;
    private static final Long CLIENT_ID =3L;
    private static final Long MANAGER_ID = 5L;
    private static final Long PRODUCT_ID =10L;
    private static final Long BANK_ACCOUNT_ID =8L;

    private static final Long TRANSACTION_ID =9L;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = managerRepository.findById(MANAGER_ID).get();
        clientRobby = clientRepository.findById(CLIENT_ID).get();
        productPLN =productRepository.findById(PRODUCT_ID).get();
        bankAccountPLN = bankAccountRepository.findById(BANK_ACCOUNT_ID).get();
        transactionA = new Transaction(
                10L,bankAccountPLN,"Robby Wolf","11000066660000666600006666",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionB = new Transaction(
                11L,bankAccountPLN,"Robby Wolf","11000066660000666600006666",
                "Jaklin Ford" ,"22000011110000333300001111",
                new BigDecimal(2000.00),"payment for a rent ",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
    }

    @Test
        //@DisplayName("It should save the Transaction to the database")
    void findAllByBankAccountId() {
        List<Transaction> result = transactionRepository.findAllByBankAccountId(BANK_ACCOUNT_ID);
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(4, result.size());
        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("It should save the Transaction to the database")
    void saveTransaction() {
        Transaction actualResult = transactionRepository.save(transactionA);
        assertEquals(transactionA.getTransactionType(), actualResult.getTransactionType());
        assertEquals(transactionA.getBankAccount().getAccountNumber(), actualResult.getBankAccount().getAccountNumber());
        assertNotNull(actualResult);
        assertThat(actualResult.getId()).isNotEqualTo(null);
       assertThat(actualResult.getTransactionAmount()).isEqualTo(new BigDecimal(1000.00));
    }

    @Test
    @DisplayName("It should return the Transactions list with size of ")
    void getAllTransactions() {
        List<Transaction> result = transactionRepository.findAll();
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(9, result.size());
        assertThat(result).hasSize(9);
    }

    @Test
    @DisplayName("It should return the Transaction by its id")
    void findTransactionById() {
        Optional<Transaction> maybeTransaction = transactionRepository.findById(TRANSACTION_ID);
        assertNotNull(maybeTransaction);
        assertTrue(maybeTransaction.isPresent());
        maybeTransaction.ifPresent(bankAccount
                -> assertEquals("11000022220000333300002222", maybeTransaction.get().getSourceAccount()));

    }


    @Test
    @DisplayName("It should update the Transaction with status")
    void updateTransaction() {

        transactionA.setTransactionStatus(TransactionStatus.DRAFT_INVALID);
        transactionA.setTransactionAmount(new BigDecimal(999.00));
        Optional<Transaction> actualResult = Optional.of(transactionRepository.saveAndFlush(transactionA));
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(transaction -> {
            assertEquals(transactionA.getTransactionAmount(), transaction.getTransactionAmount());
            assertEquals(transactionA.getBankAccount().getClient().getId(), transaction.getBankAccount().getClient().getId());
            assertEquals(new BigDecimal(999.00), transaction.getTransactionAmount());
            assertEquals(TransactionStatus.DRAFT_INVALID, transaction.getTransactionStatus());

        });


    }

    @Test
    @DisplayName("It should delete the existing Transaction")
    void deleteTransaction() {

        transactionRepository.save(transactionA);
        Long id = transactionA.getId();
        transactionRepository.save(transactionB);
        transactionRepository.delete(transactionA);
        List<Transaction> list = transactionRepository.findAll();
        Optional<Transaction> exitingTransaction = transactionRepository.findById(id);
        assertEquals(10, list.size());
        assertThat(exitingTransaction).isEmpty();

    }


}