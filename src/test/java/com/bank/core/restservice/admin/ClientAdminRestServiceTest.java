package com.bank.core.restservice.admin;

import com.bank.core.util.client.ClientCreateUpdateConverter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.ClientStatus.CREATED;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientAdminRestServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientCreateUpdateConverter clientCreateUpdateConverter;
    @Mock
    private ClientReadConverter clientReadConverter;
    @InjectMocks
    private ClientAdminRestService clientAdminRestService;
    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private ClientCreateUpdateDTO clientKarolinaCreateUpdateDTO;
    private Client clientOliwer;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = new Manager(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        managerReadDTODorota = new ManagerReadDTO(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );

        clientKarolina = new Client(
                16L, managerDorota, CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );

        clientKarolinaReadDTO = new ClientReadDTO(
                16L, managerReadDTODorota, CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientKarolinaCreateUpdateDTO = new ClientCreateUpdateDTO(
                16L, 6L, CREATED,"5555555551",
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
    void clientAdminService_createClient_returnsClientReadDto() {
        when(clientRepository.save(any(Client.class))).thenReturn(clientKarolina);
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        when(clientCreateUpdateConverter
                .convert(clientKarolinaCreateUpdateDTO)).thenReturn(clientKarolina);
        ClientReadDTO createdClient
                = clientAdminRestService.create(clientKarolinaCreateUpdateDTO).getClient();
        assertNotNull(createdClient);
        assertThat(createdClient.getFirstName()).isEqualTo("Karolina");

    }

    @Test
    void clientAdminService_findClientById_returnsClientReadDTO() {
        Long clientId = 6L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientKarolina));
        when(clientReadConverter
                .convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        ClientReadDTO existingClient
                = clientAdminRestService.findById(clientId).orElseThrow();
        assertNotNull(existingClient);
        assertThat(existingClient.getId()).isNotEqualTo(null);
    }


    @Test
    void clientAdminService_findProductByIdForException() {
        when(clientRepository.findById(8L)).thenReturn(Optional.of(clientKarolina));
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        assertThrows(RuntimeException.class, () -> {
            clientAdminRestService.findById(clientKarolina.getId());
        });
    }

    @Test
    void clientAdminService_findAllProducts_returnResponse() {
        List<Client> list = new ArrayList<>();
        list.add(clientKarolina);
        list.add(clientOliwer);
        when(clientRepository.findAll()).thenReturn(list);
        List<ClientReadDTO> products = clientAdminRestService.findAll().getClients();
        assertEquals(2, products.size());
        assertNotNull(products);
    }

    @Test
    void clientAdminService_updateClient_returnsClientReadDto() {
        LocalDateTime localDateTime =LocalDateTime.now();
        clientKarolina = new Client(
                16L, managerDorota, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );

        clientKarolinaReadDTO = new ClientReadDTO(
                16L, managerReadDTODorota, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientKarolinaCreateUpdateDTO = new ClientCreateUpdateDTO(
                16L, 6L, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        Long clientId = clientKarolina.getId();
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientKarolina));
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        when(clientRepository.saveAndFlush(any(Client.class))).thenReturn(clientKarolina);
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        when(clientCreateUpdateConverter.convert(clientKarolinaCreateUpdateDTO, clientKarolina)).thenReturn(clientKarolina);

        ClientReadDTO existingClient
                =  clientAdminRestService.update(clientId, clientKarolinaCreateUpdateDTO).getClient();
        assertNotNull(existingClient);
        assertEquals(ACTIVATED, existingClient.getStatus());

    }

    @Test
    void clientAdminService_deleteClientById_returnResponse() {
        Long clientId = 6L;
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientKarolina));
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        doNothing().when(clientRepository).delete(any(Client.class));
        clientAdminRestService.delete(clientId);
        verify(clientRepository, times(1)).delete(clientKarolina);

    }

}