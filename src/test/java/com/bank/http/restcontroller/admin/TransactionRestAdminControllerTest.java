package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.TransactionAdminRestService;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.dto.transaction.*;
import com.bank.model.entity.*;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.ClientStatus;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
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

import static com.bank.model.enums.BankAccountType.BUSINESS;
import static com.bank.model.enums.ClientStatus.CREATED;
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

@WebMvcTest(controllers = TransactionRestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TransactionRestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionReadConverter transactionReadConverter;
    @MockBean
    private TransactionAdminRestService transactionAdminRestService;

    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private Product productGBP;
    private ProductReadDTO productGBPReadDTO;
    private BankAccount bankAccountGBP;

;   private BankAccountReadDTO bankAccountGBPReadDTO;
    private Transaction transactionA;
    private TransactionReadDTO transactionAReadDTO;
    private TransactionCreateUpdateDTO transactionACreateUpdateDTO;
    private Transaction transactionB;



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
        productGBP = new Product(
                22L, managerDorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

        productGBPReadDTO = new ProductReadDTO(
                22L, managerReadDTODorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        bankAccountGBP = new BankAccount(
                18L,clientKarolina,productGBP,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        clientKarolinaReadDTO = new ClientReadDTO(
                16L, managerReadDTODorota, ClientStatus.CREATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        bankAccountGBPReadDTO = new BankAccountReadDTO(
                18L,clientKarolinaReadDTO,productGBPReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );

        transactionA = new Transaction(
                10L,bankAccountGBP,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(777.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionAReadDTO = new TransactionReadDTO(
                10L,bankAccountGBPReadDTO,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(777.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

        transactionB = new Transaction(
                11L,bankAccountGBP,"Karolina Green","77000099990000333300009999",
                "Jaklin Ford" ,"22000011110000333300001111",
                new BigDecimal(888.00),"payment for a rent ",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

    }


    @Test
    void transactAdminController_findAllTransactions_returnResponse() throws Exception {
        List<TransactionReadDTO> transactionReadDTOS = new ArrayList<>();
        transactionReadDTOS.add(transactionAReadDTO);
        transactionReadDTOS.add(transactionAReadDTO);
        FindAllTransactionsResponse response
                = new FindAllTransactionsResponse(transactionReadDTOS, new ArrayList<>());
        when(transactionAdminRestService.findAll()).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/admin/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(transactionReadDTOS.size())));

    }


    @Test
    void transactAdminController_shouldFindOneTransactionById_returnsTransactionReadDTO() throws Exception {
        when(transactionAdminRestService.findById(anyLong())).thenReturn(Optional.of(transactionAReadDTO));
        when(transactionReadConverter.convert(transactionA)).thenReturn(transactionAReadDTO);
        this.mockMvc.perform(get("/api/v1/admin/transactions/{id}", 6L))
                .andExpect(status().isOk());
        //        .andExpect(jsonPath("$.transactionAmount", is(transactionReadDTO.getTransactionAmount())))
        //        .andExpect(jsonPath("$.transactionStatus", is(transactionReadDTO.getTransactionStatus())));

    }

    @Test
    void transactAdminController_shouldCreateTransaction_returnsTransactionReadDTO() throws Exception {
        CreateUpdateTransactionResponse createTransactionResponse
                = new CreateUpdateTransactionResponse(transactionAReadDTO,new ArrayList<>());
        when(transactionAdminRestService.create(any(TransactionCreateUpdateDTO.class)))
                .thenReturn(createTransactionResponse);
        this.mockMvc.perform(post("/api/v1/admin/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionAReadDTO)))
                .andExpect(status().isCreated());
       //        .andExpect(jsonPath("$.transactionAmount", is(transactionReadDTO.getTransactionAmount())))
       //         .andExpect(jsonPath("$.transactionStatus", is(transactionReadDTO.getTransactionStatus())));

    }

    @Test
    void transactAdminController_updateTransaction_returnsResponse() throws Exception {
        Long clientId =transactionAReadDTO.getId();
        CreateUpdateTransactionResponse updateTransactionResponse
                = new CreateUpdateTransactionResponse(transactionAReadDTO,new ArrayList<>());
        when(transactionAdminRestService.update(anyLong(), any(TransactionCreateUpdateDTO.class)))
                .thenReturn(updateTransactionResponse);
        this.mockMvc.perform(put("/api/v1/admin/transactions/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionResponse)))
                .andExpect(status().isOk());
        //        .andExpect(jsonPath("$.transactionAmount", is(transactionReadDTO.getTransactionAmount())))
        //        .andExpect(jsonPath("$.transactionStatus", is(transactionReadDTO.getTransactionStatus())));

    }


    @Test
    void transactAdminController_shouldDeleteTransaction_returnsResponse() throws Exception {
        Long  transactionId=transactionAReadDTO.getId();
        DeleteTransactionResponse deleteTransactionResponse
                =new DeleteTransactionResponse(transactionAReadDTO,new ArrayList<>());
        when(transactionAdminRestService.delete(transactionId)).thenReturn(deleteTransactionResponse);
        this.mockMvc.perform(delete("/api/v1/admin/transactions/{id}", transactionId))
                .andExpect(status().isOk());
       //        .andExpect(jsonPath("$.transactionAmount", is(transactionAReadDTO.getTransactionAmount())))
       //         .andExpect(jsonPath("$.transactionStatus", is(transactionAReadDTO.getTransactionStatus())));

    }



}