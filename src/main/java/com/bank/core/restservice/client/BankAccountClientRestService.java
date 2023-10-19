package com.bank.core.restservice.client;


import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.bankaccount.FindOneBankAccountConverter;
import com.bank.core.validation.NotFoundException;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.bankaccount.FindAllBankAccountsForClientResponse;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.entity.Client;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.ClientRepository;
import com.bank.repository.LoginEntityRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class BankAccountClientRestService {

    private final BankAccountRepository bankAccountRepository;
    private final LoginEntityRepository loginEntityRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;
    private final ClientRepository clientRepository;
    private final FindOneBankAccountConverter findOneBankAccountConverter;
    private final LoginEntityRestService loginEntityRestService;
    private Long CLIENT_ID;

    public FindAllBankAccountsForClientResponse findAllBankAccountByClientId() {
        CLIENT_ID =loginEntityRestService.getClientId();
        Client client =  clientRepository.findById(CLIENT_ID)
                .orElseThrow(() -> new NotFoundException("Client not found "));
        List<FindBankAccountForClient> allBankAccounts;
        if (!bankAccountRepository.findAllByClient(client).isEmpty()) {
            allBankAccounts = bankAccountRepository.findAllByClient(client).stream()
                    .map(findOneBankAccountConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }

        return new FindAllBankAccountsForClientResponse(allBankAccounts, new ArrayList<>());
    }
    public Optional<FindBankAccountForClient> findByIdAndClientId(Long id) {
        CLIENT_ID =loginEntityRestService.getClientId();
        return bankAccountRepository.findByIdAndClientId(id, CLIENT_ID)
                .map(findOneBankAccountConverter::convert);
    }

//        public BigDecimal getBalanceById(Long id) {
//        CLIENT_ID =loginEntityRestService.getClientId();
//        return bankAccountRepository.findByIdAndClientId(id, CLIENT_ID).orElseThrow().getBalance();
//
//    }

    Optional<BankAccountReadDTO> findByAccountNumber(String accountNumber){
        return Optional.of(bankAccountRepository.findByAccountNumber(accountNumber)
                .map(bankAccountReadConverter::convert)
                .orElseThrow(() -> new NotFoundException("Bank Account not found ")));
    }

    public Optional<BankAccountReadDTO> findById(Long id) {
        return Optional.of(bankAccountRepository.findById(id)
                .map(bankAccountReadConverter::convert)
                .orElseThrow(() -> new NotFoundException("Bank Account not found ")));
    }



}
