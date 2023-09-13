package com.bank.repository;

import com.bank.model.entity.Client;
import com.bank.model.entity.LoginEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoginEntityRepositoryTest {

    @Autowired
    private LoginEntityRepository loginEntityRepository;
    @Autowired
    private ClientRepository clientRepository;
    private Client client ;
    private LoginEntity loginEntity;
    private static final Long CLIENT_ID =3L;
    private static final Long LOGIN_ID =3L;
    private static final String LOGIN_NAME ="robbywolf";

    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        client =clientRepository.findById(CLIENT_ID).orElseThrow();
        loginEntity = loginEntityRepository.findById(LOGIN_ID).orElseThrow();

    }

    @Test
    @DisplayName("It should return the LoginEntity by its username")
    void findByUsername() {
       Optional<LoginEntity> maybeProduct = loginEntityRepository.findByUsername(LOGIN_NAME);
        assertTrue(maybeProduct.isPresent());
        maybeProduct.ifPresent(product
                -> assertEquals("robbywolf", product.getUsername()));

    }


}