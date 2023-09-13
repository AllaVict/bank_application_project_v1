package com.bank.http.restcontroller.client;

import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.restservice.client.TransactionClientRestService;
import com.bank.core.util.transaction.FindOneTransactionConverter;
import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.dto.transaction.*;
import com.bank.model.entity.*;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.TransactionRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.BankAccountStatus.CREATED;
import static com.bank.model.enums.BankAccountType.BUSINESS;
import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.CurrencyCode.PLN;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(controllers =  TransactionRestClientController.class)
@AutoConfigureMockMvc(addFilters = true)
@ExtendWith(MockitoExtension.class)
class TransactionRestClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private TransactionReadConverter transactionReadConverter;
    @MockBean
    private TransactionCreateUpdateConverter transactionCreateUpdateConverter;
    @MockBean
    private FindOneTransactionConverter findOneTransactionConverter;
    @MockBean
    private TransactionReadToUpdateConverter transactionReadToUpdateConverter;
    @MockBean
    private LoginEntityRestService loginEntityRestService;

    @MockBean
    private TransactionClientRestService transactionClientRestService;

    private Client client;
    private ClientReadDTO clientReadDTO;
    private ClientCreateUpdateDTO clientCreateUpdateDTO;
    private Manager manager;
    private ManagerReadDTO managerReadDTO;
    private Product product;
    private ProductReadDTO productReadDTO;

    private BankAccount bankAccount;
    private BankAccountReadDTO bankAccountReadDTO;
    private FindBankAccountForClient findBankAccountForClient;
    private Transaction transactionA;
    TransactionReadDTO transactionAReadDTO;
    private FindTransactionForClient transactionAFindOne;
    private Transaction transactionB;
    private FindTransactionForClient transactionBFindOne;

    private final Long CLIENT_ID=3L;
    private final Long MANAGER_ID=5L;
    private final Long PRODUCT_ID=10L;
    private final Long BANK_ACCOUNT_ID=8L;

    private final Long TRANSACT_A_ID=9L;
    private final Long TRANSACT_B_ID=11L;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @BeforeEach
    public void applySecurity() {
        this.mockMvc = webAppContextSetup(wac)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @BeforeEach
    void init() {

        LocalDateTime localDateTime = LocalDateTime.now();
        manager =new Manager(    MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime  );
        product =  new Product(  PRODUCT_ID, manager, ACTIVE, "current_business_GBP",
                PLN, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime  );
        client =   new Client(  3L, manager, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime   );
        bankAccount = new BankAccount(BANK_ACCOUNT_ID,client,product,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), PLN,localDateTime, localDateTime );
        managerReadDTO = new ManagerReadDTO(
                MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        productReadDTO = new ProductReadDTO(
                10L, managerReadDTO, ACTIVE, "current_business_GBP",
                PLN, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        clientReadDTO = new ClientReadDTO(
                3L, managerReadDTO, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );

        bankAccountReadDTO = new BankAccountReadDTO(
                BANK_ACCOUNT_ID,clientReadDTO,productReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), PLN,localDateTime, localDateTime
        );
        findBankAccountForClient = new FindBankAccountForClient(
                BANK_ACCOUNT_ID,clientReadDTO.getLastName(),productReadDTO.getId(),"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), PLN
        );

        transactionA = new Transaction(
                TRANSACT_A_ID,bankAccount,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(777.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionAReadDTO = new TransactionReadDTO (
                TRANSACT_A_ID,bankAccountReadDTO,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(777.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );


        transactionAFindOne = new FindTransactionForClient(
                TRANSACT_A_ID,"Karolina Green","Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(777.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

        transactionBFindOne = new FindTransactionForClient(
                TRANSACT_B_ID,"Karolina Green","Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(555.00),"payment for a rent",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

    }

    // GET   http://localhost:8080/api/v1/client/transacts/8
    @Test
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_findAllTransactions_returnResponse() throws  Exception {
        List<FindTransactionForClient> transactionList = new ArrayList<>();
        transactionList.add(transactionAFindOne);
        transactionList.add(transactionAFindOne);
       // when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        FindAllTransactionsForClientResponse response
                = new FindAllTransactionsForClientResponse(transactionList, new ArrayList<>());
        when(transactionClientRestService
                .findAllByBankAccountId(BANK_ACCOUNT_ID)).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/client/transacts/{accountId}",BANK_ACCOUNT_ID))
                       .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(transactionList.size())));

    }

    // GET   http://localhost:8080/api/v1/client/transacts/transact/1

    @Test
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_findByIdTransaction_returnResponse() throws  Exception  {
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionClientRestService.findByIdAndClientId(anyLong())).thenReturn(Optional.of(transactionAFindOne));
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        this.mockMvc.perform(get("/api/v1/client/transacts/transact/{id}", TRANSACT_A_ID))
                .andExpect(status().isOk());
        //        .andExpect(jsonPath("$.transactionAmount", is(transactionReadDTO.getTransactionAmount())))
        //        .andExpect(jsonPath("$.transactionStatus", is(transactionReadDTO.getTransactionStatus())));


    }

    // Post   http://localhost:8080/api/v1/client/transacts/transact
    @Test
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_createTransaction_returnResponse() throws  Exception  {

        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        CreateUpdateTransactionForClientResponse createTransactionResponse
                = new CreateUpdateTransactionForClientResponse(transactionAFindOne,new ArrayList<>());
        when(transactionClientRestService.create(any(TransactionCreateUpdateDTO.class)))
                .thenReturn(createTransactionResponse);
        this.mockMvc.perform(post("/api/v1/client/transacts/transact")
                     .with(csrf()) //https://stackoverflow.com/questions/52994063/spring-webmvctest-with-post-returns-403
                     //.with(user("robbywolf").password("123").authorities())
                      // .with(httpBasic("robbywolf", "123"))
                       .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionAFindOne) ) )
                      .andExpect(status().isCreated());
      }

    // Put   http://localhost:8080/api/v1/client/transacts/transact/5

    @Test
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_updateTransaction_returnResponse() throws  Exception{
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        Long transactionId =transactionAFindOne.getId();
        CreateUpdateTransactionForClientResponse updateTransactionResponse
                = new CreateUpdateTransactionForClientResponse(transactionAFindOne,new ArrayList<>());
        when(transactionClientRestService.update(anyLong(), any(TransactionCreateUpdateDTO.class)))
                .thenReturn(updateTransactionResponse);
        this.mockMvc.perform(put("/api/v1/client/transacts/transact/{id}", transactionId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionResponse)))
                     .andExpect(status().isOk());

    }

    // Put   http://localhost:8080/api/v1/client/transacts/transact/auth/5
    @Test
    //@WithMockUser(username = "jaklinford", password = "123",authorities = {"CLIENT"})
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_authorizeTransaction_NotFound() throws Exception {
        Long transactionId =transactionAFindOne.getId();
        when(transactionClientRestService.findByIdAndClientId(transactionId))
                .thenReturn(Optional.ofNullable(transactionAFindOne));
        AuthorizeTransactionResponse authorizeTransactionResponse
                = new AuthorizeTransactionResponse(transactionAReadDTO,"",new ArrayList<>());
        when(transactionClientRestService.authorize(transactionId))
                .thenReturn(authorizeTransactionResponse);
        this.mockMvc.perform(put("/api/v1/client/transacts/transact/auth/", 9L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizeTransactionResponse)))
                .andExpect(status().isNotFound());

    }

    // Delete   http://localhost:8080/api/v1/client/transacts/transact/{id}
    @Test
    @WithMockUser(username = "test", password = "test",authorities = {"CLIENT"})
    void transactClientController_deleteTransaction_returnResponse() throws  Exception{

        Long transactionId =transactionAFindOne.getId();
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        DeleteTransactionForClientResponse  deleteTransactionResponse
                =new DeleteTransactionForClientResponse(transactionAFindOne,new ArrayList<>());
        when(transactionClientRestService.delete(transactionId)).thenReturn(deleteTransactionResponse);
        this.mockMvc.perform(delete("/api/v1/client/transacts/transact/{id}", transactionId)
                .with(csrf()))
                .andExpect(status().isOk());

    }
}