package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ClientAdminRestService;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.model.dto.client.*;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.ClientStatus.CREATED;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientRestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ClientRestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientAdminRestService clientAdminRestService;
    @MockBean
    private ClientReadConverter clientReadConverter;

    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private ClientCreateUpdateDTO clientKarolinaCreateUpdateDTO;
    private Client clientOliwer;
    private ClientReadDTO clientOliwerReadDTO;
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
        clientOliwerReadDTO = new ClientReadDTO(
                17L, managerReadDTODorota, CREATED,"5555555551",
                "Oliwer", "Red",  "alina@gmail.com",
                "London","+48777333111", localDateTime, localDateTime
        );

    }

    @Test
    void clientAdminController_findAllClients_returnResponse() throws Exception {
        List<ClientReadDTO> clientReadDTOS = new ArrayList<>();
        clientReadDTOS.add(clientKarolinaReadDTO);
        clientReadDTOS.add(clientOliwerReadDTO);
        FindAllClientsResponse response = new FindAllClientsResponse(clientReadDTOS, new ArrayList<>());
        when(clientAdminRestService.findAll()).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/admin/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(clientReadDTOS.size())));

    }

    @Test
    void clientAdminController_shouldFindOneClientById_returnsClientReadDTO() throws Exception {
        when(clientAdminRestService.findById(anyLong())).thenReturn(Optional.of(clientKarolinaReadDTO));
        when(clientReadConverter.convert(clientKarolina)).thenReturn(clientKarolinaReadDTO);
        this.mockMvc.perform(get("/api/v1/admin/clients/{id}", 6L))
                .andExpect(status().isOk());
        //        .andExpect(jsonPath("$.firstName", is(clientKarolinaReadDTO.getFirstName())))
        //        .andExpect(jsonPath("$.lastName", is(clientKarolinaReadDTO.getLastName())));

    }

    @Test
    void clientAdminController_shouldCreateClient_returnsClientReadDTO() throws Exception {
        CreateUpdateClientResponse  updateClientResponse
                = new CreateUpdateClientResponse(clientKarolinaReadDTO,new ArrayList<>());
        when(clientAdminRestService.create(any(ClientCreateUpdateDTO.class)))
                .thenReturn(updateClientResponse);
        this.mockMvc.perform(post("/api/v1/admin/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientKarolinaReadDTO)))
                .andExpect(status().isCreated());
    //            .andExpect(jsonPath("$.firstName", is(clientKarolinaReadDTO.getFirstName())))
    //            .andExpect(jsonPath("$.lastName", is(clientKarolinaReadDTO.getLastName())));

    }


    @Test
    void clientAdminController_updateClient_returnsResponse() throws Exception {
        Long clientId =clientKarolinaReadDTO.getId();
        CreateUpdateClientResponse updateClientResponse
                = new CreateUpdateClientResponse(clientKarolinaReadDTO,new ArrayList<>());
        when(clientAdminRestService.update(anyLong(), any(ClientCreateUpdateDTO.class)))
                .thenReturn(updateClientResponse);
        this.mockMvc.perform(put("/api/v1/admin/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateClientResponse)))
                .andExpect(status().isOk());
      //          .andExpect(jsonPath("$.firstName", is(clientKarolinaReadDTO.getFirstName())))
      //          .andExpect(jsonPath("$.lastName", is(clientKarolinaReadDTO.getLastName())));

    }

    @Test
    void clientAdminController_shouldDeleteClient_returnsResponse() throws Exception {
        Long clientId =clientKarolinaReadDTO.getId();
        DeleteClientResponse deleteClientResponse
                =new DeleteClientResponse(clientKarolinaReadDTO,new ArrayList<>());
        when(clientAdminRestService.delete(clientId)).thenReturn(deleteClientResponse);
        this.mockMvc.perform(delete("/api/v1/admin/clients/{id}", clientId))
                .andExpect(status().isOk());
       //         .andExpect(jsonPath("$.firstName", is(clientKarolinaReadDTO.getFirstName())))
      //          .andExpect(jsonPath("$.lastName", is(clientKarolinaReadDTO.getLastName())));

    }




}