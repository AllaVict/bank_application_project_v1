package com.bank.repository;

import com.bank.model.entity.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;
    private Manager managerDorota;
    private Manager managerWiktor;

    @BeforeEach
    void init() {
        managerDorota = new Manager(
                "Dorota",
                "Dancer",
                CHECKING,
                "Description"
              //  LocalDateTime.of(2023,9,1,22, 20)
        );

        managerWiktor = new Manager(
                "Wiktor",
                "Singer",
                CHECKING,
                "Description"
             //   LocalDateTime.of(2023,9,1,22,20)
        );
        managerDorota.setCreatedAt(LocalDateTime.now());
        managerWiktor.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("It should save the Manager to the database")
    void saveManager() {
        managerRepository.save(managerDorota);
        Manager newManager = managerRepository.save(managerDorota);
        assertNotNull(newManager);
        assertThat(newManager.getId()).isNotEqualTo(null);
        assertThat(newManager.getFirstName()).isEqualTo("Dorota");
    }

    @Test
    @DisplayName("It should return the managers list with size of 2")
    void getAllManagers() {
        managerRepository.save(managerDorota);
        managerRepository.save(managerWiktor);

        List<Manager> list = managerRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(7, list.size());
    }


    @Test
    @DisplayName("It should return the Manager by its id")
    void findManagerById() {
        managerRepository.save(managerDorota);
        Manager newManager = managerRepository.findById(managerDorota.getId()).get();
        assertNotNull(newManager);
        assertEquals("Description", newManager.getDescription());

    }


    @Test
    @DisplayName("It should update the Manager with new Description")
    void updateManager() {
        managerRepository.save(managerDorota);
        Manager existingManager = managerRepository.findById(managerDorota.getId()).get();
        existingManager.setDescription("new Description");
        Manager updatedManager = managerRepository.save(existingManager);
        assertEquals("new Description", updatedManager.getDescription());
        assertEquals("Dorota", updatedManager.getFirstName());
    }



    @Test
    @DisplayName("It should delete the existing Manager")
    void deleteManager() {

        managerRepository.save(managerDorota);
        Long id = managerDorota.getId();
        managerRepository.save(managerWiktor);
        managerRepository.delete(managerDorota);
        List<Manager> list = managerRepository.findAll();
        Optional<Manager> exitingManager = managerRepository.findById(id);
        assertEquals(6, list.size());
        assertThat(exitingManager).isEmpty();

    }


}