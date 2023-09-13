package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ManagerAdminRestService;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.manager.*;
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

import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ManagerRestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ManagerRestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ManagerAdminRestService managerAdminRestService;
    @MockBean
    private ManagerReadConverter managerReadConverter;

    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private ManagerCreateUpdateDTO managerCreateUpdateDorota;
    private Manager managerWiktor;
    private ManagerReadDTO managerReadDTOWiktor;


    @BeforeEach
    void init() {
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

    /**
     GET   http://localhost:8080/api/v1/admin/managers
     @GetMapping("/managers")
     public ResponseEntity<FindAllManagersResponse> findAllManagers(){
     FindAllManagersResponse response = managerAdminRestService.findAll();
     return new ResponseEntity<>(response, HttpStatus.OK);      } */

    @Test
    void managerAdminController_findAllManagers_returnResponse() throws Exception {
        List<ManagerReadDTO> listManagersDTO = new ArrayList<>();
        listManagersDTO.add(managerReadDTODorota);
        listManagersDTO.add(managerReadDTOWiktor);
        FindAllManagersResponse response = new FindAllManagersResponse(listManagersDTO, new ArrayList<>());
        when(managerAdminRestService.findAll()).thenReturn(response);
       this.mockMvc.perform(get("/api/v1/admin/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listManagersDTO.size())));

    }


    @Test
    void managerAdminController_shouldFindOneManagerById_returnsManagerReadDTO() throws Exception {
        when(managerAdminRestService.findById(anyLong())).thenReturn(Optional.of(managerReadDTODorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        this.mockMvc.perform(get("/api/v1/admin/managers/{id}", 6L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(managerReadDTODorota.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(managerReadDTODorota.getLastName())));

    }

    /**
     Post   http://localhost:8080/api/v1/admin/managers
     @PostMapping("/managers")
     public ResponseEntity<CreateUpdateManagerResponse> createManager(@RequestBody ManagerCreateUpdateDTO request){
     CreateUpdateManagerResponse response  = managerAdminRestService.create(request);
     return new ResponseEntity<>(response, HttpStatus.CREATED);   }
     */

    @Test
    void managerAdminController_shouldCreateManager_returnsManagerReadDTO() throws Exception {
        CreateUpdateManagerResponse createManagerResponse
                = new CreateUpdateManagerResponse(managerReadDTODorota,new ArrayList<>());
        when(managerAdminRestService.create(any(ManagerCreateUpdateDTO.class)))
                .thenReturn(createManagerResponse);
        this.mockMvc.perform(post("/api/v1/admin/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerReadDTODorota)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manager.firstName", is(managerReadDTODorota.getFirstName())))
                .andExpect(jsonPath("$.manager.lastName", is(managerReadDTODorota.getLastName())));

   }

    /**
     Put   http://localhost:8080/api/v1/admin/managers/7
     @PutMapping("/managers/{id}")
     public ResponseEntity<CreateUpdateManagerResponse> updateManager(@PathVariable("id") Long id,
     @RequestBody ManagerCreateUpdateDTO request){
     CreateUpdateManagerResponse response = managerAdminRestService.update(request.getId(),request);
     return new ResponseEntity<>(response, HttpStatus.OK);    }     */

    @Test
    void managerAdminController_updateManager_returnsResponse() throws Exception {
        Long managerId =managerReadDTODorota.getId();
        CreateUpdateManagerResponse updateManagerResponse
                = new CreateUpdateManagerResponse(managerReadDTODorota,new ArrayList<>());
        when(managerAdminRestService.update(anyLong(), any(ManagerCreateUpdateDTO.class)))
                .thenReturn(updateManagerResponse);
        this.mockMvc.perform(put("/api/v1/admin/managers/{id}", managerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerReadDTODorota)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manager.firstName", is(managerReadDTODorota.getFirstName())))
                .andExpect(jsonPath("$.manager.lastName", is(managerReadDTODorota.getLastName())));

    }
    /**   Delete   http://localhost:8080/api/v1/admin/managers/{id}
    @DeleteMapping("/managers/{id}")
    public ResponseEntity<DeleteManagerResponse> deleteManager(@PathVariable("id") Long id) {
        DeleteManagerResponse response =  managerAdminRestService.delete(id);
        log.info("Deleting manager " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);    }     */
    @Test
    void managerAdminController_shouldDeleteManager_returnsResponse() throws Exception {
        Long managerId =managerReadDTODorota.getId();
        DeleteManagerResponse deleteManagerResponse
                =new DeleteManagerResponse(managerReadDTODorota,new ArrayList<>());
        when(managerAdminRestService.delete(managerId)).thenReturn(deleteManagerResponse);
        this.mockMvc.perform(delete("/api/v1/admin/managers/{id}", managerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manager.firstName", is(managerReadDTODorota.getFirstName())))
                .andExpect(jsonPath("$.manager.lastName", is(managerReadDTODorota.getLastName())));

    }




}