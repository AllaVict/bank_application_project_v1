package com.bank.core.restservice;

import com.bank.core.util.loginentity.LoginEntityReadConverter;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.loginentity.LoginEntityReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Client;
import com.bank.model.entity.LoginEntity;
import com.bank.model.entity.Manager;
import com.bank.model.enums.ClientStatus;
import com.bank.model.enums.Role;
import com.bank.repository.LoginEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginEntityRestServiceTest {

    @Mock
    LoginEntityRepository loginEntityRepository;
    @Mock
    LoginEntityReadConverter loginEntityReadConverter;
    @InjectMocks
    LoginEntityRestService loginEntityRestService;

    private LoginEntity loginEntity;
    private LoginEntityReadDTO loginEntityReadDTO;
    private Client client;
    private ClientReadDTO clientReadDTO;
    private Manager manager;
    private ManagerReadDTO managerReadDTO;

    private static final Long CLIENT_ID =3L;
    private static final Long MANAGER_ID = 5L;
    private static final Long LOGIN_ID =14L;

    private static final String USERNAME ="robbywolf";
    

    @BeforeEach
    void init() {
        LocalDateTime localDateTime = LocalDateTime.now();
        loginEntity = new LoginEntity(
                LOGIN_ID, "robbywolf", "$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm",
                Role.ADMIN, client, localDateTime, localDateTime
        );

       loginEntityReadDTO = new LoginEntityReadDTO(
                LOGIN_ID, "robbywolf", "$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm",
                Role.ADMIN, clientReadDTO, localDateTime, localDateTime
        );

        manager = new Manager(
                MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        managerReadDTO = new ManagerReadDTO(
                MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );

        client = new Client(
                CLIENT_ID, manager, ClientStatus.CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientReadDTO = new ClientReadDTO(
                CLIENT_ID, managerReadDTO, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );

    }

    @Test
    void loginService_loadUserByUsername_returnsUserDetails() {
        when(loginEntityRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(loginEntity));
        final UserDetails userDetails = loginEntityRestService.loadUserByUsername(USERNAME);
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(),  "robbywolf");

    }
    @Test
    void loginService_findByUsername_returnsLoginEntityReadDTO() {
        when(loginEntityRepository.findByUsername(USERNAME)).thenReturn(Optional.of(loginEntity));
        when(loginEntityReadConverter.convert(loginEntity)).thenReturn(loginEntityReadDTO);
        LoginEntityReadDTO existingLoginEntity = loginEntityRestService.findByUsername(USERNAME).orElseThrow();
        assertNotNull(existingLoginEntity);
        assertThat(existingLoginEntity.getId()).isNotEqualTo(null);
        assertEquals(existingLoginEntity.getUsername(), loginEntity.getUsername());
    }


}