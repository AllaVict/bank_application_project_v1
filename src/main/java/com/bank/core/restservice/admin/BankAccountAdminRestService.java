package com.bank.core.restservice.admin;


import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.validation.NotFoundException;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.*;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.ClientRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class BankAccountAdminRestService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;
    private final ClientRepository clientRepository;

    public FindAllBankAccountsResponse findAll() {
        List<BankAccountReadDTO> allBankAccounts;
        if (!bankAccountRepository.findAll().isEmpty()) {
            allBankAccounts = bankAccountRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(bankAccount ->bankAccountReadConverter.convert(bankAccount))
                    .map(bankAccountReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllBankAccountsResponse(allBankAccounts, new ArrayList<>());
    }


    public Optional<BankAccountReadDTO> findById(Long id) {
        return Optional.of(bankAccountRepository.findById(id)
                .map(bankAccountReadConverter::convert).orElseThrow());
    }

    public CreateUpdateBankAccountResponse create(BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        bankAccountCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        BankAccountReadDTO bankAccountReadDTO= Optional.of(bankAccountCreateUpdateDTO)
                .map(bankAccountCreateUpdateConverter::convert)//BankAccountCreateConverter  BankAccountCreateDTO ->BankAccount
                .map(bankAccountRepository::save)   // .map(BankAccount -> BankAccountRepository.save(BankAccount))
                .map(bankAccountReadConverter::convert) //BankAccountReadConverter  BankAccount -> BankAccountReadDTO
                .orElseThrow();
        return new CreateUpdateBankAccountResponse(bankAccountReadDTO, new ArrayList<>());
    }

    public CreateUpdateBankAccountResponse update(Long id, BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        Optional<BankAccount> bankAccountForUpdate = Optional.ofNullable(bankAccountRepository.findById(id)
                .orElseThrow(() -> new ValidationException("BankAccount not found")));
        //bankAccountCreateUpdateDTO.setBankAccountType(bankAccountForUpdate.get().getAccountType());
        bankAccountCreateUpdateDTO.setCreatedAt(bankAccountForUpdate.get().getCreatedAt());
        bankAccountCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());
        return new CreateUpdateBankAccountResponse(
                bankAccountForUpdate.map(bankAccount -> bankAccountCreateUpdateConverter.convert(bankAccountCreateUpdateDTO, bankAccount))
                        .map(bankAccountRepository::saveAndFlush) // save bankAccountCreateUpdateDTO
                        .map(bankAccountReadConverter::convert).orElseThrow()  // bankAccount -> bankAccountReadDTO
                , new ArrayList<>());
    }

    public DeleteBankAccountResponse delete(Long request){
        BankAccount bankAccountForDelete =  bankAccountRepository.findById(request)
                .orElseThrow(() -> new ValidationException("BankAccount not found"));
        bankAccountRepository.delete(bankAccountForDelete);
        return new DeleteBankAccountResponse(bankAccountReadConverter
                .convert(bankAccountForDelete), new ArrayList<>());

    }

}
