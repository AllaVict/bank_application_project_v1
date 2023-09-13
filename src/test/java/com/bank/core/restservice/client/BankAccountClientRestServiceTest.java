package com.bank.core.restservice.client;

import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.bankaccount.FindOneBankAccountConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.ClientRepository;
import com.bank.repository.ManagerRepository;
import com.bank.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountClientRestServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountReadConverter bankAccountReadConverter;
    @Mock
    private BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;
    @Mock
    private FindOneBankAccountConverter findOneBankAccountConverter;
    @Mock
    private LoginEntityRestService loginEntityRestService;
    @InjectMocks
    private BankAccountClientRestService bankAccountClientRestService;
    private final Long CLIENT_ID=3L;
    private final Long MANAGER_ID=5L;
    private final Long PRODUCT_ID=10L;
    private final Long BANK_ACCOUNT_ID=10L;
    private Client client;
    private ClientReadDTO clientReadDTO;
    private Product product;
    private ProductReadDTO productReadDTO;
    private BankAccount bankAccount;
    private BankAccountReadDTO bankAccountReadDTO;

    private FindBankAccountForClient findBankAccountForClient;
    private Manager manager;
    private ManagerReadDTO managerReadDTO;

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
        bankAccount = new BankAccount(BANK_ACCOUNT_ID,client,product,"current_bus_GBP",
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
    void bankAccountClientService_findAllBankAccountByClientId_returnResponse() {

        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount);
        List<FindBankAccountForClient> list = new ArrayList<>();
        list.add(findBankAccountForClient);
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.ofNullable(client));
        when(bankAccountRepository.findAllByClient(client)).thenReturn(bankAccountList);
        when(findOneBankAccountConverter.convert(bankAccount)).thenReturn(findBankAccountForClient);
        List<FindBankAccountForClient> bankAccounts
                    = bankAccountClientRestService.findAllBankAccountByClientId().getBankAccounts();
        assertEquals(1, bankAccounts.size());
        assertNotNull(bankAccounts);

    }


    @Test
    void  bankAccountClientService_findByIdAndClientIdForException() {
        when(bankAccountRepository.findById(18L)).thenReturn(Optional.of(bankAccount));
        when(bankAccountReadConverter.convert(bankAccount)).thenReturn(bankAccountReadDTO);
        assertThrows(RuntimeException.class, () -> {
            bankAccountClientRestService.findById(bankAccount.getId());
        });
    }


    @Test
    void  bankAccountClientService_findByIdAndClientId_returnResponse() {
        Long bankAccountId = BANK_ACCOUNT_ID;
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(bankAccountReadConverter.convert(bankAccount)).thenReturn(bankAccountReadDTO);
        BankAccountReadDTO existingBankAccount = bankAccountClientRestService.findById(bankAccountId).orElseThrow();
        assertNotNull(existingBankAccount);
        assertThat(existingBankAccount.getId()).isNotEqualTo(null);

    }


    @Test
    void bankAccountClientService_findByAccountNumber_returnResponse() {
        String bankAccountNumber = "77000099990000333300009999";
        when(bankAccountRepository.findByAccountNumber(bankAccountNumber)).thenReturn(Optional.of(bankAccount));
        when(bankAccountReadConverter.convert(bankAccount)).thenReturn(bankAccountReadDTO);
        BankAccountReadDTO existingBankAccount
                = bankAccountClientRestService.findByAccountNumber(bankAccountNumber).orElseThrow();
        assertNotNull(existingBankAccount);
        assertThat(existingBankAccount.getId()).isNotEqualTo(null);

    }



}