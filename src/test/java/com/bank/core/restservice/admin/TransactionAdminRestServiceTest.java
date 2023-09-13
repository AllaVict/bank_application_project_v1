package com.bank.core.restservice.admin;

import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import com.bank.model.entity.*;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static com.bank.model.enums.BankAccountType.BUSINESS;
import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.ClientStatus.CREATED;
import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionAdminRestServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionCreateUpdateConverter transactionCreateUpdateConverter;
    @Mock
    private TransactionReadConverter transactionReadConverter;
    @InjectMocks
    private TransactionAdminRestService transactionAdminRestService;

    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private ClientCreateUpdateDTO clientKarolinaCreateUpdateDTO;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private Product productGBP;
    private ProductReadDTO productGBPReadDTO;
    private ProductCreateUpdateDTO productGBPCreateUpdateDTO;

    private BankAccount bankAccountGBP;
    private BankAccountReadDTO bankAccountGBPReadDTO;
    private BankAccountCreateUpdateDTO bankAccountGBPCreateUpdateDTO;

    private Transaction transactionA;
    private TransactionReadDTO transactionAReadDTO;
    private TransactionCreateUpdateDTO transactionACreateUpdateDTO;
    private Transaction transactionB;

    private final Long BANK_ACCOUNT_ID=10L;

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
                16L, managerReadDTODorota, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientKarolinaCreateUpdateDTO = new ClientCreateUpdateDTO(
                16L, 6L, ACTIVATED,"5555555551",
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
        productGBPCreateUpdateDTO = new ProductCreateUpdateDTO(
                22L, 6L, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

        bankAccountGBP = new BankAccount(
                18L,clientKarolina,productGBP,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountGBPReadDTO = new BankAccountReadDTO(
                18L,clientKarolinaReadDTO,productGBPReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountGBPCreateUpdateDTO = new BankAccountCreateUpdateDTO(
                18L,clientKarolinaCreateUpdateDTO.getId(),productGBPCreateUpdateDTO.getId(),"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );

        transactionA = new Transaction(
                10L,bankAccountGBP,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
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

        transactionAReadDTO = new TransactionReadDTO(
                10L,bankAccountGBPReadDTO,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionACreateUpdateDTO = new TransactionCreateUpdateDTO(
                10L,bankAccountGBP.getId(),"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

    }



    @Test
    void transactAdminService_createTransaction_returnsTransactionReadDto() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionA);
        when(transactionReadConverter.convert(transactionA)).thenReturn(transactionAReadDTO);
        when(transactionCreateUpdateConverter
                .convert(transactionACreateUpdateDTO)).thenReturn(transactionA);
        TransactionReadDTO createdTransaction
                = transactionAdminRestService.create(transactionACreateUpdateDTO).getTransaction();
        assertNotNull(createdTransaction);
        assertThat(createdTransaction.getTransactionAmount()).isEqualTo( new BigDecimal(1000.00));

    }


    @Test
    void transactAdminService_findTransactionById_returnsTransactionReadDTO() {
        Long transactId = BANK_ACCOUNT_ID;
        when(transactionRepository.findById(transactId)).thenReturn(Optional.of(transactionA));
        when(transactionReadConverter
                .convert(transactionA)).thenReturn(transactionAReadDTO);
        TransactionReadDTO existingTransaction
                = transactionAdminRestService.findById(transactId).orElseThrow();
        assertNotNull(existingTransaction);
        assertThat(existingTransaction.getId()).isNotEqualTo(null);
    }


    @Test
    void transactAdminService_findTransactionByIdForException() {
        when(transactionRepository.findById(15L)).thenReturn(Optional.of(transactionA));
        when(transactionReadConverter.convert(transactionA)).thenReturn(transactionAReadDTO);
        assertThrows(RuntimeException.class, () -> {
            transactionAdminRestService.findById(transactionA.getId());
        });
    }

    @Test
    void transactAdminService_findAllTransactions_returnResponse() {
        List<Transaction> list = new ArrayList<>();
        list.add(transactionA);
        list.add(transactionB);
        when(transactionRepository.findAll()).thenReturn(list);
        List<TransactionReadDTO> transactions = transactionAdminRestService.findAll().getTransactions();
        assertEquals(2, transactions.size());
        assertNotNull(transactions);
    }


    @Test
    void transactAdminService_updateTransaction_returnsTransactionReadDto() {
        LocalDateTime localDateTime =LocalDateTime.now();
        transactionA = new Transaction(
                10L,bankAccountGBP,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.FUTURE_DATE,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

       transactionAReadDTO = new TransactionReadDTO(
                10L,bankAccountGBPReadDTO,"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.FUTURE_DATE,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionACreateUpdateDTO = new TransactionCreateUpdateDTO(
                10L,bankAccountGBP.getId(),"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.FUTURE_DATE,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        Long transactId = transactionA.getId();
        when(transactionRepository.findById(transactId)).thenReturn(Optional.of(transactionA));
        when(transactionReadConverter.convert(transactionA)).thenReturn(transactionAReadDTO);
        when(transactionRepository.saveAndFlush(transactionA)).thenReturn(transactionA);
        when(transactionCreateUpdateConverter
                .convert(transactionACreateUpdateDTO, transactionA)).thenReturn(transactionA);
        TransactionReadDTO existingTransaction
                =  transactionAdminRestService.update(transactId, transactionACreateUpdateDTO).getTransaction();
        assertNotNull(existingTransaction);
        assertEquals(TransactionStatus.FUTURE_DATE, existingTransaction.getTransactionStatus());

    }

    @Test
    void transactAdminService_deleteTransactionById_returnResponse() {
        Long transactId = BANK_ACCOUNT_ID;
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionA));
        when(transactionReadConverter.convert(transactionA)).thenReturn(transactionAReadDTO);
        doNothing().when(transactionRepository).delete(any(Transaction.class));
        transactionAdminRestService.delete(transactId);
        verify(transactionRepository, times(1)).delete(transactionA);

    }




}
