package com.bank.repository;

import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.ClientStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ManagerRepository managerRepository;
    private Client clientKarolina;
    private Client clientOliwer;
    private Manager managerDorota;
    private static final Long CLIENT_ID =15L;
    private static final Long MANAGER_ID = 5L;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = managerRepository.findById(MANAGER_ID).get();
        clientKarolina = new Client(
               16L, managerDorota, CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientOliwer = new Client(
                17L, managerDorota, CREATED,"5555555551",
                "Oliwer", "Red",  "alina@gmail.com",
                "London","+48777333111", localDateTime, localDateTime
        );

    }

    @Test
    @DisplayName("It should save the Client to the database")
    void saveClient() {
        Client actualResult = clientRepository.save(clientKarolina);
        assertEquals(clientKarolina.getFirstName(), actualResult.getFirstName());
        assertEquals(clientKarolina.getManager().getId(), actualResult.getManager().getId());
        assertNotNull(actualResult);
        assertThat(actualResult.getId()).isNotEqualTo(null);
        assertThat(actualResult.getFirstName()).isEqualTo("Karolina");
    }

    @Test
    @DisplayName("It should return the Clients list with size of ")
    void getAllClients() {
        List<Client> result = clientRepository.findAll();
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(15, result.size());
        assertThat(result).hasSize(15);
    }


    @Test
    @DisplayName("It should return the Client by its id")
    void findClientById() {
//        clientRepository.save(clientKarolina);
//        Client newClient  = clientRepository.findById(clientKarolina.getId()).get();
        Optional<Client> maybeClient = clientRepository.findById(CLIENT_ID);
        assertNotNull(maybeClient);
        assertTrue(maybeClient.isPresent());
        maybeClient.ifPresent(client -> assertEquals("Jak", client.getFirstName()));
    }

    @Test
    @DisplayName("It should update the Client with status")
    void updateClient() {
        clientKarolina.setStatus(ACTIVATED);
        Optional<Client> actualResult = Optional.of(clientRepository.saveAndFlush(clientKarolina));
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(product -> {
            assertEquals(clientKarolina.getFirstName(), product.getFirstName());
            assertEquals(clientKarolina.getManager().getId(), product.getManager().getId());
            assertEquals(ACTIVATED, product.getStatus());
        });
    }


    @Test
    @DisplayName("It should delete the existing Client")
    void deleteClient() {
        clientRepository.save(clientKarolina);
        Long id = clientKarolina.getId();
        clientRepository.save(clientOliwer);
        clientRepository.delete(clientKarolina);
        List<Client> list = clientRepository.findAll();
        Optional<Client> exitingClient = clientRepository.findById(id);
        assertEquals(16, list.size());
        assertThat(exitingClient).isEmpty();

    }






}