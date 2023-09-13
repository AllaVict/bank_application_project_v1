package com.bank.core.restservice.admin;

import com.bank.core.util.manager.ManagerCreateUpdateConverter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Manager;
import com.bank.repository.ManagerRepository;
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

import static com.bank.model.enums.ManagerStatus.CHECKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
class ManagerAdminRestServiceTest {


    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private ManagerCreateUpdateConverter managerCreateUpdateConverter;
    @Mock
    private ManagerReadConverter managerReadConverter;

    @InjectMocks
    private ManagerAdminRestService managerAdminRestService;

    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private ManagerCreateUpdateDTO managerCreateUpdateDorota;
    private Manager managerWiktor;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        managerAdminRestService =
//        new ManagerAdminRestService(managerRepository, managerReadConverter, managerCreateUpdateConverter);
//    }

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


    }

    @Test
    void managerAdminService_createManager_returnsManagerReadDto() {
        when(managerRepository.save(any(Manager.class))).thenReturn(managerDorota);
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        when(managerCreateUpdateConverter.convert(managerCreateUpdateDorota)).thenReturn(managerDorota);
        ManagerReadDTO createdManager = managerAdminRestService.create(managerCreateUpdateDorota).getManager();
        assertNotNull(createdManager);
        assertThat(createdManager.getFirstName()).isEqualTo("Dorota");

    }

    @Test
    void managerAdminService_findManagerById_returnsManagerReadDTO() {
        Long managerId = 6L;
        when(managerRepository.findById(managerId)).thenReturn(Optional.of(managerDorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        ManagerReadDTO existingManager = managerAdminRestService.findById(managerId).orElseThrow();
        assertNotNull(existingManager);
        assertThat(existingManager.getId()).isNotEqualTo(null);
    }

    @Test
    void managerAdminService_findManagerByIdForException() {
        when(managerRepository.findById(8L)).thenReturn(Optional.of(managerDorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        assertThrows(RuntimeException.class, () -> {
            managerAdminRestService.findById(managerDorota.getId());
        });
    }

    @Test
    void managerAdminService_findAllManagers() {
        List<Manager> list = new ArrayList<>();
        list.add(managerDorota);
        list.add(managerWiktor);
        when(managerRepository.findAll()).thenReturn(list);
        List<ManagerReadDTO> managers = managerAdminRestService.findAll().getManagers();
        assertEquals(2, managers.size());
        assertNotNull(managers);
    }


    @Test
    void managerAdminService_updateManager_returnsManagerReadDto() {
        Long managerId = 6L;
        managerDorota.setDescription("new Description");
        managerCreateUpdateDorota.setDescription("new Description");
        when(managerRepository.findById(managerId)).thenReturn(Optional.of(managerDorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        when(managerRepository.saveAndFlush(any(Manager.class))).thenReturn(managerDorota);
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        when(managerCreateUpdateConverter.convert(managerCreateUpdateDorota, managerDorota)).thenReturn(managerDorota);

        ManagerReadDTO existingManager =  managerAdminRestService.update(managerId,managerCreateUpdateDorota)
                        .getManager();
        managerDorota.setDescription("new description");
        managerReadDTODorota.setDescription("new description");
        managerCreateUpdateDorota.setDescription("new description");
        assertNotNull(existingManager);
        assertEquals("new description", existingManager.getDescription());

    }

    @Test
    void managerAdminService_deleteManagerById_returnResponse() {
        Long managerId = 6L;
        when(managerRepository.findById(anyLong())).thenReturn(Optional.of(managerDorota));
        when(managerReadConverter.convert(managerDorota)).thenReturn(managerReadDTODorota);
        doNothing().when(managerRepository).delete(any(Manager.class));
        managerAdminRestService.delete(managerId);
        verify(managerRepository, times(1)).delete(managerDorota);

    }


}