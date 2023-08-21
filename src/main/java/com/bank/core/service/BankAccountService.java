package com.bank.core.service;

import com.bank.core.util.bankaccount.BankAccountCreateUpdateConverter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.repository.BankAccountRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Data
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountCreateUpdateConverter bankAccountCreateUpdateConverter;

    public List<BankAccountReadDTO> findAll() {
        return bankAccountRepository.findAll().stream()
                //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                //.map(account ->accountReadConverter.convert(account))
                .map(bankAccountReadConverter::convert)
                .toList();
    }

    public Optional<BankAccountReadDTO> findById(Long id) {
        return  bankAccountRepository.findById(id)// findById(ID id) return Optional<T>
                //<U> Optional<U> map(Function<? super T, ? extends U> mapper)
                // .map(account -> accountReadConverter.convert(account))
                .map(bankAccountReadConverter::convert); // account -> accountReadDTO
    }

    // Optional.of Returns:an Optional with the value present Throws: NullPointerException – if value is null
    //public static <T> Optional<T> of(T value) {  return new Optional<>(Objects.requireNonNull(value))}
    // accountCreateConverter  accountCreateDTO ->account
    // accountReadConverter  account -> accountReadDTO
    @Transactional
    public BankAccountReadDTO create(BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        bankAccountCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        return Optional.of(bankAccountCreateUpdateDTO)
                .map(bankAccountCreateUpdateConverter::convert)
                // .map(bankAccount -> bankAccountRepository.save(bankAccount))
                .map(bankAccountRepository::save)
                .map(bankAccountReadConverter::convert)
                .orElseThrow();
    }

    @Transactional
    public  Optional<BankAccountReadDTO> update(Long id, BankAccountCreateUpdateDTO bankAccountCreateUpdateDTOO) {
        // bankAccountRepository.findById(id)// findById BankAccount
        // .map(BankAccount ->bankAccountRepository.saveAndFlush(bankAccountCreateUpdateDTOO));
        Optional<BankAccount> accountForUpdate = Optional.ofNullable(bankAccountRepository.findById(id)
                .orElseThrow(() -> new ValidationException("BankAccount not found")));
        bankAccountCreateUpdateDTOO.setCreatedAt(accountForUpdate.get().getCreatedAt());
        bankAccountCreateUpdateDTOO.setUpdatedAt(LocalDateTime.now());
        return bankAccountRepository.findById(id) // findById(ID id) return Optional<T>
                // convert(bankAccountCreateUpdateDTO, bankAccount) -- copy(fromBankAccountCreateUpdateDTO, toBankAccount);
                .map(bankAccount -> bankAccountCreateUpdateConverter.convert(bankAccountCreateUpdateDTOO, bankAccount))
                .map(bankAccountRepository::saveAndFlush) // save BankAccount bankAccountUpdateDTO
                .map(bankAccountReadConverter::convert);  // BankAccount -> bankAccountReadDTO

    }

    @Transactional
    public boolean delete(Long id) {
        return bankAccountRepository.findById(id)
                .map(manager -> {
                    bankAccountRepository.delete(manager);
                    bankAccountRepository.flush();
                    return true;
                })
                .orElse(false);
    }





}
