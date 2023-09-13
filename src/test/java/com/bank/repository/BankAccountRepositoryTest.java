package com.bank.repository;


import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.bank.model.enums.BankAccountStatus;
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

import static com.bank.model.enums.BankAccountStatus.CREATED;
import static com.bank.model.enums.BankAccountType.BUSINESS;
import static com.bank.model.enums.BankAccountType.PERSONAL;
import static com.bank.model.enums.CurrencyCode.EUR;
import static com.bank.model.enums.CurrencyCode.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;
    private BankAccount bankAccountEUR;
    private BankAccount bankAccountUSD;
    private Client clientKarolina;
    private Manager managerDorota;
    private Product productEUR;
    private Product productUSD;

    private static final Long CLIENT_ID =3L;
    private static final Long MANAGER_ID = 5L;
    private static final Long PRODUCT_ID_EUR =18L;

    private static final Long PRODUCT_ID_USD =17L;
    private static final Long BANK_ACCOUNT_ID =8L;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = managerRepository.findById(MANAGER_ID).get();
        clientKarolina = clientRepository.findById(CLIENT_ID).get();
        productEUR = productRepository.findById(PRODUCT_ID_EUR).get();
        productUSD = productRepository.findById(PRODUCT_ID_USD).get();

        bankAccountEUR = new BankAccount(
         18L,clientKarolina, productEUR,"current_bus_GBP",
          "77000099990000333300009999", BUSINESS, CREATED,
          new BigDecimal(0.00), EUR,localDateTime, localDateTime
        );
        bankAccountUSD = new BankAccount(
                19L,clientKarolina, productUSD,"current_per_CAD",
                "77000088880000333300008888", PERSONAL, CREATED,
                new BigDecimal(0.00), USD,localDateTime, localDateTime
        );
    }

    @Test
    @DisplayName("It should return the BankAccounts list of the client ")
    void findAllByClient() {
        List<BankAccount> result = bankAccountRepository.findAllByClient(clientKarolina);
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(1, result.size());
        assertThat(result).hasSize(1);

    }

    @Test
    @DisplayName("It should return the BankAccount by its number")
    void findByAccountNumber() {
        Optional<BankAccount> maybeBankAccount = bankAccountRepository
                .findByAccountNumber("22000011110000333300001111");
        assertNotNull(maybeBankAccount);
        assertTrue(maybeBankAccount.isPresent());
        maybeBankAccount.ifPresent(bankAccount
                -> assertEquals("22000011110000333300001111", bankAccount.getAccountNumber()));

    }

    @Test
    @DisplayName("It should return the BankAccount by its id and Client Id")
    void findByIdAndClientId() {
        Optional<BankAccount> maybeBankAccount
                = bankAccountRepository.findByIdAndClientId(BANK_ACCOUNT_ID, CLIENT_ID);
        assertNotNull(maybeBankAccount);
        assertTrue(maybeBankAccount.isPresent());
        maybeBankAccount.ifPresent(bankAccount
                -> assertEquals("22000011110000333300001111", bankAccount.getAccountNumber()));

    }

    @Test
    @DisplayName("It should save the BankAccount to the database")
    void saveBankAccount() {
        BankAccount actualResult = bankAccountRepository.save(bankAccountEUR);
        assertEquals(bankAccountEUR.getAccountNumber(), actualResult.getAccountNumber());
        assertEquals(bankAccountEUR.getClient().getId(), actualResult.getClient().getId());
        assertNotNull(actualResult);
        assertThat(actualResult.getId()).isNotEqualTo(null);
        assertThat(actualResult.getAccountNumber()).isEqualTo("77000099990000333300009999");
    }

    @Test
    @DisplayName("It should return the BankAccounts list with size of ")
    void getAllBankAccounts() {
        List<BankAccount> result = bankAccountRepository.findAll();
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(14, result.size());
        assertThat(result).hasSize(14);

    }

    @Test
    @DisplayName("It should return the BankAccount by its id")
    void findBankAccountById() {
        Optional<BankAccount> maybeBankAccount = bankAccountRepository.findById(BANK_ACCOUNT_ID);
        assertNotNull(maybeBankAccount);
        assertTrue(maybeBankAccount.isPresent());
        maybeBankAccount.ifPresent(bankAccount
                -> assertEquals("22000011110000333300001111", bankAccount.getAccountNumber()));

    }


    @Test
    @DisplayName("It should update the BankAccount with status")
    void updateBankAccount() {
        bankAccountEUR.setStatus(BankAccountStatus.ACTIVATED);
        Optional<BankAccount> actualResult = Optional.of(bankAccountRepository.saveAndFlush(bankAccountEUR));
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(bankAccount -> {
            assertEquals(bankAccountEUR.getAccountNumber(), bankAccount.getAccountNumber());
            assertEquals(bankAccountEUR.getClient().getId(), bankAccount.getClient().getId());
            assertEquals(BankAccountStatus.ACTIVATED, bankAccount.getStatus());

        });

    }


    @Test
    @DisplayName("It should delete the existing BankAccount")
    void deleteBankAccount() {
        bankAccountRepository.save(bankAccountEUR);
        Long id = bankAccountEUR.getId();
        bankAccountRepository.save(bankAccountUSD);
        bankAccountRepository.delete(bankAccountEUR);
        List<BankAccount> list = bankAccountRepository.findAll();
        Optional<BankAccount> exitingBankAccount = bankAccountRepository.findById(id);
        assertEquals(16, list.size());
        assertThat(exitingBankAccount).isEmpty();

    }




}