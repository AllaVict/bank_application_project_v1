package com.bank.core.restservice.admin;

import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.ClientStatus;
import com.bank.repository.BankAccountRepository;
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
import static com.bank.model.enums.BankAccountType.PERSONAL;
import static com.bank.model.enums.ClientStatus.ACTIVATED;
import static com.bank.model.enums.CurrencyCode.CAD;
import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountAdminRestServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;
    @Mock
    private BankAccountReadConverter bankAccountReadConverter;
    @InjectMocks
    private BankAccountAdminRestService bankAccountAdminRestService;

    private BankAccount bankAccountGBP;
    private BankAccountReadDTO bankAccountGBPReadDTO;
    private BankAccountCreateUpdateDTO bankAccountGBPCreateUpdateDTO;
    private BankAccount bankAccountCAD;
    private Client clientKarolina;
    private ClientReadDTO clientKarolinaReadDTO;
    private ClientCreateUpdateDTO clientKarolinaCreateUpdateDTO;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;
    private Product productGBP;
    private Product productCAD;
    private ProductReadDTO productGBPReadDTO;
    private ProductCreateUpdateDTO productGBPCreateUpdateDTO;
    private final Long BANK_ACCOUNT_ID=16L;

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
        productCAD = new Product(
                23L, managerDorota, ACTIVE, "current_business_CAD",
                CAD, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

        clientKarolina = new Client(
                16L, managerDorota, ClientStatus.CREATED,"5555555551",
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

        bankAccountGBP = new BankAccount(
                18L,clientKarolina,productGBP,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountCAD = new BankAccount(
                19L,clientKarolina,productCAD,"current_per_CAD",
                "77000088880000333300008888", PERSONAL, CREATED,
                new BigDecimal(0.00), CAD,localDateTime, localDateTime
        );

         bankAccountGBPReadDTO = new BankAccountReadDTO(
                18L,clientKarolinaReadDTO,productGBPReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountGBPCreateUpdateDTO = new BankAccountCreateUpdateDTO(
                18L,clientKarolinaCreateUpdateDTO.getId(),productGBPCreateUpdateDTO.getId(),"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, CREATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );

    }

    @Test
    void bankAccountAdminService_createBankAccount_returnsBankAccountReadDto() {
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccountGBP);
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        when(bankAccountCreateUpdateConverter
                .convert(bankAccountGBPCreateUpdateDTO)).thenReturn(bankAccountGBP);
        BankAccountReadDTO createdBankAccount
                = bankAccountAdminRestService.create(bankAccountGBPCreateUpdateDTO).getBankAccount();
        assertNotNull(createdBankAccount);
        assertThat(createdBankAccount.getAccountNumber()).isEqualTo("77000099990000333300009999");

    }


    @Test
    void bankAccountAdminService_findBankAccountById_returnsBankAccountReadDTO() {
        Long bankAccountId = BANK_ACCOUNT_ID;
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccountGBP));
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        BankAccountReadDTO existingBankAccount = bankAccountAdminRestService.findById(bankAccountId).orElseThrow();
        assertNotNull(existingBankAccount);
        assertThat(existingBankAccount.getId()).isNotEqualTo(null);

    }


    @Test
    void bankAccountAdminService_findBankAccountByIdForException() {
        when(bankAccountRepository.findById(30L)).thenReturn(Optional.of(bankAccountGBP));
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        assertThrows(RuntimeException.class, () -> {
            bankAccountAdminRestService.findById(bankAccountGBP.getId());
        });
    }


    @Test
    void bankAccountAdminService_findAllBankAccounts_returnResponse() {
        List<BankAccount> list = new ArrayList<>();
        list.add(bankAccountGBP);
        list.add(bankAccountCAD);
        when(bankAccountRepository.findAll()).thenReturn(list);
        List<BankAccountReadDTO> bankAccounts = bankAccountAdminRestService.findAll().getBankAccounts();
        assertEquals(2, bankAccounts.size());
        assertNotNull(bankAccounts);
    }

    @Test
    void bankAccountAdminService_updateBankAccount_returnsBankAccountReadDto() {
        LocalDateTime localDateTime =LocalDateTime.now();
        bankAccountGBP = new BankAccount(
                18L,clientKarolina,productGBP,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.ACTIVATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountGBPReadDTO = new BankAccountReadDTO(
        18L,clientKarolinaReadDTO,productGBPReadDTO,"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.ACTIVATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );
        bankAccountGBPCreateUpdateDTO = new  BankAccountCreateUpdateDTO  (
                18L,clientKarolinaCreateUpdateDTO.getId(),productGBPCreateUpdateDTO.getId(),"current_bus_GBP",
                "77000099990000333300009999", BUSINESS, BankAccountStatus.ACTIVATED,
                new BigDecimal(0.00), GBP,localDateTime, localDateTime
        );

        Long bankAccountId = bankAccountGBP.getId();
        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccountGBP));
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        when(bankAccountRepository.saveAndFlush(any(BankAccount.class))).thenReturn(bankAccountGBP);
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        when(bankAccountCreateUpdateConverter
                .convert(bankAccountGBPCreateUpdateDTO, bankAccountGBP)).thenReturn(bankAccountGBP);
        BankAccountReadDTO existingBankAccount
                =  bankAccountAdminRestService.update(bankAccountId, bankAccountGBPCreateUpdateDTO).getBankAccount();
        assertNotNull(existingBankAccount);
        assertEquals(BankAccountStatus.ACTIVATED, existingBankAccount.getStatus());

    }

    @Test
    void bankAccountAdminService_deleteBankAccountById_returnResponse() {
        Long bankAccountId = BANK_ACCOUNT_ID;
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccountGBP));
        when(bankAccountReadConverter.convert(bankAccountGBP)).thenReturn(bankAccountGBPReadDTO);
        doNothing().when(bankAccountRepository).delete(any(BankAccount.class));
        bankAccountAdminRestService.delete(bankAccountId);
        verify(bankAccountRepository, times(1)).delete(bankAccountGBP);

    }




}