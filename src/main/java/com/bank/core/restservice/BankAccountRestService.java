package com.bank.core.restservice;


import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.*;
import com.bank.model.entity.BankAccount;
import com.bank.repository.BankAccountRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class BankAccountRestService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;

    public FindAllBankAccountsResponse findAll(String accessKey) {
        List<BankAccountReadDTO> allBankAccountts;
        if (!bankAccountRepository.findAll().isEmpty()) {
            allBankAccountts = bankAccountRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(bankAccount ->bankAccountReadConverter.convert(bankAccount))
                    .map(bankAccountReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllBankAccountsResponse(allBankAccountts, new ArrayList<>());
    }

    public Optional<BankAccountReadDTO> findById(Long id) {
        return Optional.of(bankAccountRepository.findById(id)
                .map(bankAccountReadConverter::convert).orElseThrow());
    }

    public CreateUpdateBankAccountResponse create(BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        bankAccountCreateUpdateDTO.setCreated_at(LocalDateTime.now());
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
        bankAccountCreateUpdateDTO.setCreated_at(bankAccountForUpdate.get().getCreated_at());
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
        return new DeleteBankAccountResponse(bankAccountReadConverter.convert(bankAccountForDelete), new ArrayList<>());

    }

}
