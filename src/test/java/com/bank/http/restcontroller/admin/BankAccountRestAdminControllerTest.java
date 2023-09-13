package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.BankAccountAdminRestService;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.model.dto.bankaccount.*;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.model.enums.ClientStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.BankAccountStatus.CREATED;
import static com.bank.model.enums.BankAccountType.BUSINESS;
import static com.bank.model.enums.BankAccountType.PERSONAL;
import static com.bank.model.enums.CurrencyCode.CAD;
import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  BankAccountRestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BankAccountRestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BankAccountReadConverter bankAccountReadConverter;
    @MockBean
    private BankAccountAdminRestService bankAccountAdminRestService;

    private BankAccount bankAccountGBP;
    private BankAccountReadDTO bankAccountGBPReadDTO;
    private BankAccountReadDTO bankAccountCADReadDTO;
    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;

    private ProductReadDTO productGBPReadDTO;
    private ProductReadDTO productCADReadDTO;



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

        productGBPReadDTO = new ProductReadDTO(
                22L, managerReadDTODorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productCADReadDTO = new ProductReadDTO(
                23L, managerReadDTODorota, ACTIVE, "current_business_CAD",
                CAD, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        clientKarolina = new Client(
                16L, managerDorota, ClientStatus.CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientKarolinaReadDTO = new ClientReadDTO(
                16L, managerReadDTODorota, ClientStatus.CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        bankAccountGBPReadDTO = new BankAccountReadDTO(
                18L,clientKarolinaReadDTO,productGBPReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountCADReadDTO = new BankAccountReadDTO(
                19L,clientKarolinaReadDTO,productCADReadDTO,"current_per_CAD",
                "77000088880000333300008888", PERSONAL, CREATED,
                new BigDecimal(0.00), CAD,localDateTime, localDateTime
        );


    }


    @Test
    void bankAccountAdminController_findAllBankAccounts_returnResponse() throws Exception {
        List<BankAccountReadDTO> bankAccountReadDTOS = new ArrayList<>();
        bankAccountReadDTOS.add(bankAccountGBPReadDTO);
        bankAccountReadDTOS.add(bankAccountGBPReadDTO);
        FindAllBankAccountsResponse response = new FindAllBankAccountsResponse(bankAccountReadDTOS, new ArrayList<>());
        when(bankAccountAdminRestService.findAll()).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/admin/bankAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bankAccountReadDTOS.size())));

    }


    @Test
    void bankAccountAdminController_shouldFindOneBankAccountById_returnsBankAccountReadDTO() throws Exception {
        when(bankAccountAdminRestService.findById(anyLong())).thenReturn(Optional.of(bankAccountGBPReadDTO));
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        this.mockMvc.perform(get("/api/v1/admin/bankAccounts/{id}", 6L))
                .andExpect(status().isOk());
       //         .andExpect(jsonPath("$.accountName", is(bankAccountGBPReadDTO.getAccountName())))
       //         .andExpect(jsonPath("$.accountNumber", is(bankAccountGBPReadDTO.getAccountNumber())));

    }

    @Test
    void bankAccountAdminController_shouldCreateBankAccount_returnsBankAccountReadDTO() throws Exception {
        CreateUpdateBankAccountResponse createBankAccountResponse
                = new CreateUpdateBankAccountResponse(bankAccountGBPReadDTO,new ArrayList<>());
        when(bankAccountAdminRestService.create(any(BankAccountCreateUpdateDTO.class)))
                .thenReturn(createBankAccountResponse);
        this.mockMvc.perform(post("/api/v1/admin/bankAccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountGBPReadDTO)))
                .andExpect(status().isCreated());
       //         .andExpect(jsonPath("$.accountName", is(bankAccountGBPReadDTO.getAccountName())))
       //         .andExpect(jsonPath("$.accountNumber", is(bankAccountGBPReadDTO.getAccountNumber())));

    }

    @Test
    void bankAccountAdminController_updateBankAccount_returnsResponse() throws Exception {
        Long clientId =bankAccountGBPReadDTO.getId();
        CreateUpdateBankAccountResponse updateBankAccountResponse
                = new CreateUpdateBankAccountResponse(bankAccountGBPReadDTO,new ArrayList<>());
        when(bankAccountAdminRestService.update(anyLong(), any(BankAccountCreateUpdateDTO.class)))
                .thenReturn(updateBankAccountResponse);
        this.mockMvc.perform(put("/api/v1/admin/bankAccounts/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBankAccountResponse)))
                .andExpect(status().isOk());
        //        .andExpect(jsonPath("$.accountName", is(bankAccountGBPReadDTO.getAccountName())))
        //        .andExpect(jsonPath("$.accountNumber", is(bankAccountGBPReadDTO.getAccountNumber())));

    }

    @Test
    void bankAccountAdminController_shouldDeleteBankAccount_returnsResponse() throws Exception {
        Long bankAccountId =bankAccountGBPReadDTO.getId();
        DeleteBankAccountResponse deleteBankAccountResponse
                =new DeleteBankAccountResponse(bankAccountGBPReadDTO,new ArrayList<>());
        when(bankAccountAdminRestService.delete(bankAccountId)).thenReturn(deleteBankAccountResponse);
        this.mockMvc.perform(delete("/api/v1/admin/bankAccounts/{id}", bankAccountId))
                .andExpect(status().isOk());
       //         .andExpect(jsonPath("$.accountName", is(bankAccountGBPReadDTO.getAccountName())))
       //         .andExpect(jsonPath("$.accountNumber", is(bankAccountGBPReadDTO.getAccountNumber())));

    }




}