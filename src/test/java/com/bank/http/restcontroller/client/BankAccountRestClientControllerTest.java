package com.bank.http.restcontroller.client;

import com.bank.core.restservice.client.BankAccountClientRestService;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.bankaccount.FindOneBankAccountConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.bankaccount.FindAllBankAccountsForClientResponse;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  BankAccountRestClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BankAccountRestClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BankAccountReadConverter bankAccountReadConverter;
    @MockBean
    private FindOneBankAccountConverter findOneBankAccountConverter;
    @MockBean
    private BankAccountClientRestService bankAccountClientRestService;
    private Client client;
    private ClientReadDTO clientReadDTO;
    private Product product;
    private ProductReadDTO productReadDTO;
    private BankAccount bankAccount;
    private BankAccountReadDTO bankAccountReadDTO;

    private FindBankAccountForClient findBankAccountForClient;
    private Manager manager;
    private ManagerReadDTO managerReadDTO;
    private final Long CLIENT_ID=3L;
    private final Long MANAGER_ID=5L;
    private final Long PRODUCT_ID=10L;
    private final Long BANK_ACCOUNT_ID=10L;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime = LocalDateTime.now();
        manager =new Manager(    MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime  );
        product =  new Product(  PRODUCT_ID, manager, ACTIVE, "current_business_GBP",
                PLN, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime  );
        client =   new Client(  CLIENT_ID, manager, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime   );
        bankAccount = new BankAccount(10L,client,product,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), PLN,localDateTime, localDateTime );
        managerReadDTO = new ManagerReadDTO(
                MANAGER_ID, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        productReadDTO = new ProductReadDTO(
                PRODUCT_ID, managerReadDTO, ACTIVE, "current_business_GBP",
                PLN, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        clientReadDTO = new ClientReadDTO(
                CLIENT_ID, managerReadDTO, ACTIVATED,"5555555551",
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

    }


    @Test
    void bankAccountClientController_findAllBankAccountByClientId_returnResponse() throws Exception {
        List<FindBankAccountForClient> bankAccounts = new ArrayList<>();
        bankAccounts.add(findBankAccountForClient);
        bankAccounts.add(findBankAccountForClient);
        FindAllBankAccountsForClientResponse response = new FindAllBankAccountsForClientResponse(bankAccounts, new ArrayList<>());
        when(bankAccountClientRestService.findAllBankAccountByClientId()).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/client/bankAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bankAccounts.size())));
    }


    @Test
    void bankAccountClientController_findByIdAndClientId_returnResponse() throws Exception  {
        when(bankAccountClientRestService.findByIdAndClientId(anyLong())).thenReturn(Optional.of(findBankAccountForClient));
        when(findOneBankAccountConverter.convert(bankAccount)).thenReturn(findBankAccountForClient);
        this.mockMvc.perform(get("/api/v1/client/bankAccounts/{id}", BANK_ACCOUNT_ID))
                .andExpect(status().isOk());

    }
}