package com.bank.core.restservice.client;

import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.transaction.FindOneTransactionConverter;
import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.dto.transaction.AuthorizeTransactionResponse;
import com.bank.model.dto.transaction.FindTransactionForClient;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import com.bank.model.entity.*;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.TransactionRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionClientRestServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountReadConverter bankAccountReadConverter;
    @Mock
    private BankAccountClientRestService bankAccountClientRestService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionReadConverter transactionReadConverter;
    @Mock
    private TransactionCreateUpdateConverter transactionCreateUpdateConverter;
    @Mock
    private TransactionReadToUpdateConverter transactionReadToUpdateConverter;
    @Mock
    private FindOneTransactionConverter findOneTransactionConverter;
    @Mock
    private LoginEntityRestService loginEntityRestService;
    @InjectMocks
    private TransactionClientRestService transactionClientRestService;

    private Client client;
    private ClientReadDTO clientReadDTO;
    private ClientCreateUpdateDTO clientCreateUpdateDTO;
    private Manager manager;
    private ManagerReadDTO managerReadDTO;
    private Product product;
    private ProductReadDTO productReadDTO;
    private ProductCreateUpdateDTO productCreateUpdateDTO;

    private BankAccount bankAccount;
    private BankAccountReadDTO bankAccountReadDTO;
    private BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO;

    private FindBankAccountForClient findBankAccountForClient;
    private Transaction transactionA;
    private FindTransactionForClient transactionAFindOne;
    private TransactionReadDTO transactionAReadDTO;
    private TransactionCreateUpdateDTO transactionACreateUpdateDTO;
    private Transaction transactionB;
    private FindTransactionForClient transactionBFindOne;

    private final Long CLIENT_ID=3L;
    private final Long MANAGER_ID=5L;
    private final Long PRODUCT_ID=10L;
    private final Long BANK_ACCOUNT_ID=8L;
    private final Long TRANSACT_A_ID=10L;
    private final Long TRANSACT_B_ID=11L;

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

        productCreateUpdateDTO = new ProductCreateUpdateDTO(
                PRODUCT_ID, manager.getId(), ACTIVE, "current_business_GBP",
                PLN, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );


        clientReadDTO = new ClientReadDTO(
                CLIENT_ID, managerReadDTO, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );
        clientCreateUpdateDTO = new ClientCreateUpdateDTO(
                CLIENT_ID, MANAGER_ID, ACTIVATED,"5555555551",
                "Karolina", "Green",  "alina@gmail.com",
                "Warszawa","+48777333111", localDateTime, localDateTime
        );

        bankAccountReadDTO = new BankAccountReadDTO(
                BANK_ACCOUNT_ID,clientReadDTO,productReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), PLN,localDateTime, localDateTime
        );
        bankAccountCreateUpdateDTO = new BankAccountCreateUpdateDTO(
                BANK_ACCOUNT_ID,clientCreateUpdateDTO.getId(),productCreateUpdateDTO.getId(),"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.CREATED,
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
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

        transactionACreateUpdateDTO = new TransactionCreateUpdateDTO(
                TRANSACT_A_ID,bankAccount.getId(),"Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );
        transactionAFindOne = new FindTransactionForClient(
                TRANSACT_A_ID,"Karolina Green","Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

        transactionBFindOne = new FindTransactionForClient(
                TRANSACT_B_ID,"Karolina Green","Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(1000.00),"payment for a rent",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_INVALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime, localDateTime
        );

    }


    @Test
    void transactClientService_createTransaction_returnsResponse() {
//        when(transactionRepository.findByBankAccountId(BANK_ACCOUNT_ID))
//                .thenReturn(Optional.ofNullable(transactionA));
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(bankAccountRepository.findById(BANK_ACCOUNT_ID))
                .thenReturn(Optional.ofNullable(bankAccount));
        when( bankAccountClientRestService.findById(bankAccountReadDTO.getId()))
                .thenReturn(Optional.ofNullable(bankAccountReadDTO));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionA);
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        when(transactionCreateUpdateConverter
                .convert(transactionACreateUpdateDTO)).thenReturn(transactionA);
        FindTransactionForClient createdTransaction
                = transactionClientRestService.create(transactionACreateUpdateDTO).getTransaction();
        assertNotNull(createdTransaction);
        assertThat(createdTransaction.getTransactionAmount()).isEqualTo( new BigDecimal(1000.00));

    }


    @Test
    void transactClientService_findByIdAndClientId_returnsResponse() {
        Long transactId = transactionA.getId();
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionRepository.findById(transactId)).thenReturn(Optional.of(transactionA));
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        FindTransactionForClient existingTransaction
                = transactionClientRestService.findByIdAndClientId(transactId).orElseThrow();
        assertNotNull(existingTransaction);
        assertThat(existingTransaction.getId()).isNotEqualTo(null);


    }
    @Test
    void transactClientService_findTransactionByIdForException() {
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionRepository.findById(15L)).thenReturn(Optional.of(transactionA));
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        assertThrows(RuntimeException.class, () -> {
            transactionClientRestService.findByIdAndClientId(transactionA.getId());
         });
    }

    @Test
    void transactClientService_findAllByBankAccountId_returnsResponse() {
        List<Transaction> list = new ArrayList<>();
        list.add(transactionA);
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionRepository.findAllByBankAccountId(BANK_ACCOUNT_ID)).thenReturn(list);
        when(bankAccountRepository.findById(BANK_ACCOUNT_ID)).thenReturn(Optional.ofNullable(bankAccount));
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
          List<FindTransactionForClient> transactions
                = transactionClientRestService.findAllByBankAccountId(BANK_ACCOUNT_ID).getTransactions();
        assertEquals(1, transactions.size());
        assertNotNull(transactions);

    }

    @Test
    void transactClientService_updateTransaction_returnsResponse() {
        LocalDateTime localDateTime =LocalDateTime.now();
        transactionAFindOne = new FindTransactionForClient(
                10L,"Karolina Green","Karolina Green","77000099990000333300009999",
                "Alina Smith" ,"11000022220000333300002222",
                new BigDecimal(300.00),"payment for a service",new BigDecimal(0.00),
                TransactionType.INTERNAL, TransactionStatus.DRAFT_VALID,
                "aecfd78c-a03d-494b-abef-b4dee329d774", localDateTime, localDateTime, localDateTime,localDateTime
        );
        Long transactId = transactionACreateUpdateDTO.getId();
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionRepository.findById(transactId)).thenReturn(Optional.ofNullable(transactionA));
        when(bankAccountRepository.findById(BANK_ACCOUNT_ID))
                .thenReturn(Optional.ofNullable(bankAccount));
       // when(transactionRepository.findByBankAccountId(BANK_ACCOUNT_ID)).thenReturn(Optional.ofNullable(transactionA));
        when(transactionCreateUpdateConverter.convert(transactionACreateUpdateDTO, transactionA))
                .thenReturn(transactionA);
        when(transactionRepository.saveAndFlush(transactionA)).thenReturn(transactionA);
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        FindTransactionForClient existingTransaction
                =  transactionClientRestService.update(transactId, transactionACreateUpdateDTO).getTransaction();
        assertNotNull(existingTransaction);
        assertEquals(new BigDecimal(300.00), existingTransaction.getTransactionAmount());

    }


    @Test
    void transactClientService_deleteTransaction_returnsResponse() {
        Long transactId = BANK_ACCOUNT_ID;
        when(loginEntityRestService.getClientId()).thenReturn(CLIENT_ID);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionA));
        when(findOneTransactionConverter.convert(transactionA)).thenReturn(transactionAFindOne);
        doNothing().when(transactionRepository).delete(any(Transaction.class));
        transactionClientRestService.delete(transactId);
        verify(transactionRepository, times(1)).delete(transactionA);

    }
}