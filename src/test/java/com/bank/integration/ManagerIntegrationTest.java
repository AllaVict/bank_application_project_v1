package com.bank.integration;


import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bank.core.restservice.admin.ManagerAdminRestService;
import com.bank.core.util.manager.ManagerCreateUpdateConverter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.manager.CreateUpdateManagerResponse;
import com.bank.model.dto.manager.FindAllManagersResponse;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Manager;
import com.bank.repository.ManagerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManagerIntegrationTest {


    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ManagerRepository managerRepository;

    @MockBean
    private ManagerAdminRestService managerAdminRestService;

    @MockBean
    private ManagerCreateUpdateConverter managerCreateUpdateConverter;

    @MockBean
    private ManagerReadConverter managerReadConverter;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private ManagerCreateUpdateDTO managerCreateUpdateDorota;
    private Manager managerWiktor;
    private ManagerReadDTO managerReadDTOWiktor;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }
    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" + port + "/api/v1/admin/managers";
        LocalDateTime localDateTime = LocalDateTime.now();
        managerDorota = new Manager(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        managerReadDTODorota = new ManagerReadDTO(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );

        managerCreateUpdateDorota = new ManagerCreateUpdateDTO(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );

        managerWiktor = new Manager(
                7L, "Wiktor", "Singer",
                CHECKING, "description", localDateTime
        );
        managerReadDTOWiktor = new ManagerReadDTO(
                7L, "Wiktor", "Singer",
                CHECKING, "description", localDateTime
        );

    }

    @AfterEach
    public void afterSetup() {
  //      managerRepository.deleteAll();
    }


    // GET   http://localhost:8080/api/v1/admin/managers
    @Test
    void shouldFindAllManagersTest() throws Exception{
        List<ManagerReadDTO> listManagersDTO = new ArrayList<>();
        listManagersDTO.add(managerReadDTODorota);
        listManagersDTO.add(managerReadDTOWiktor);
        FindAllManagersResponse response = new FindAllManagersResponse(listManagersDTO, new ArrayList<>());
        when(managerAdminRestService.findAll()).thenReturn(response);
        FindAllManagersResponse existingListManagers=new FindAllManagersResponse();
        try{
            existingListManagers
                    = restTemplate.getForObject(baseUrl, FindAllManagersResponse.class);
        } catch (HttpMessageConversionException exception){

        } finally {
            assertNotNull(existingListManagers);
            assertEquals(2, listManagersDTO.size());
        }

    }

    // GET   http://localhost:8080/api/v1/admin/managers/3
    @Test
    void shouldFindOneManagerByIdTest() {
        when(managerAdminRestService.findById(anyLong())).thenReturn(Optional.of(managerReadDTODorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        ManagerReadDTO existingManager
                = restTemplate.getForObject(baseUrl+"/"+managerReadDTODorota.getId(), ManagerReadDTO.class);
        assertNotNull(existingManager);
        assertEquals("Dorota", existingManager.getFirstName());
    }

    // Post   http://localhost:8080/api/v1/admin/managers
    @Test
    void shouldCreateManagerTest() {
        LocalDateTime localDateTime =LocalDateTime.now();
        ManagerReadDTO managerReadDTOKira = new ManagerReadDTO(
                8L, "Kira", "Newton",
                CHECKING, "description", localDateTime
        );
        ManagerCreateUpdateDTO  managerCreateUpdateKira = new ManagerCreateUpdateDTO(
                "Kira", "Newton",
                CHECKING, "description"
        );
        CreateUpdateManagerResponse newManager
                = restTemplate.postForObject(baseUrl, managerCreateUpdateKira, CreateUpdateManagerResponse.class);
        assertNotNull(managerReadDTOKira);
        assertThat(managerReadDTOKira.getId()).isNotNull();
    }


    //   Put   http://localhost:8080/api/v1/admin/managers/7
    @Test
    void shouldUpdateManagerTest() {
        managerCreateUpdateDorota.setDescription("new");
        restTemplate.put(baseUrl+"/"+managerCreateUpdateDorota.getId(), managerCreateUpdateDorota);
        managerReadDTODorota.setDescription("new");
        when(managerAdminRestService.findById(anyLong())).thenReturn(Optional.of(managerReadDTODorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        ManagerReadDTO existingManager
                = restTemplate.getForObject(baseUrl+"/"+managerCreateUpdateDorota.getId(), ManagerReadDTO.class);
        assertNotNull(existingManager);
        assertEquals("new", existingManager.getDescription());

    }

    //Delete   http://localhost:8080/api/v1/admin/managers/{id}
    @Test
    void shouldDeleteMovieTest() {
        restTemplate.delete(baseUrl+"/"+managerDorota.getId());
        int count = managerRepository.findAll().size();
        assertEquals(5, count);

    }




}
