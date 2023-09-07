package com.bank.core.restservice.client;


import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.validation.NotFoundException;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.*;
import com.bank.model.entity.Client;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.ClientRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class BankAccountClientRestService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;
    private final ClientRepository clientRepository;

    public FindAllBankAccountsResponse findAllBankAccountByClientId(Long id) {
        Client client =  clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found "));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Client: "+client);
        List<BankAccountReadDTO> allBankAccounts;
        if (!bankAccountRepository.findAllByClient(client).isEmpty()) {
            allBankAccounts = bankAccountRepository.findAllByClient(client).stream()
                    .map(bankAccountReadConverter::convert)
                    .toList();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! allBankAccounts: "+allBankAccounts);
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllBankAccountsResponse(allBankAccounts, new ArrayList<>());
    }


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
