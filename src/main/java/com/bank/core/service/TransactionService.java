package com.bank.core.service;

import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import com.bank.model.entity.Transaction;
import com.bank.repository.TransactionRepository;
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
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionReadConverter transactionReadConverter;
    private final TransactionCreateUpdateConverter transactionCreateUpdateConverter;

    public List<TransactionReadDTO> findAll() {
        return transactionRepository.findAll().stream()
                //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                //.map(transaction ->transactionReadConverter.convert(transaction))
                .map(transactionReadConverter::convert)
                .toList();
    }

    public Optional<TransactionReadDTO> findById(Long id) {
        return  transactionRepository.findById(id)// findById(ID id) return Optional<T>
                //<U> Optional<U> map(Function<? super T, ? extends U> mapper)
                // .map(transaction -> transactionReadConverter.convert(transaction))
                .map(transactionReadConverter::convert); // client -> clientReadDTO
    }

    // Optional.of Returns:an Optional with the value present Throws: NullPointerException – if value is null
    //public static <T> Optional<T> of(T value) {  return new Optional<>(Objects.requireNonNull(value))}
    @Transactional
    public TransactionReadDTO create(TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        transactionCreateUpdateDTO.setAccountId(1L);
        return Optional.of(transactionCreateUpdateDTO)
                .map(transactionCreateUpdateConverter::convert)
                // .map(client -> clientRepository.save(client))
                .map(transactionRepository::save)
                .map(transactionReadConverter::convert)
                .orElseThrow();
    }

    @Transactional
    public  Optional<TransactionReadDTO> update(Long id, TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        // transactionRepository.findById(id)// findById Client
        // .map(Transaction ->transactionRepository.saveAndFlush(transactionCreateUpdateDTO));
        Optional<Transaction> transactionForUpdate = Optional.ofNullable(transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transaction not found")));
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        transactionCreateUpdateDTO.setAccountId(1L);
        transactionCreateUpdateDTO.setTransactionDate(transactionForUpdate.get().getTransactionDate());
        transactionCreateUpdateDTO.setEffectiveDate(LocalDateTime.now());
        return transactionRepository.findById(id) // findById(ID id) return Optional<T>
                // convert(TransactionCreateUpdateDTO, Transaction) -- copy(fromTransactionCreateUpdateDTO, toTransaction);
                .map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                .map(transactionRepository::saveAndFlush) // save Transaction TransactionCreateUpdateDTO
                .map(transactionReadConverter::convert);  // Transaction -> TransactionReadDTO

    }

    @Transactional
    public boolean delete(Long id) {
        return transactionRepository.findById(id)
                .map(manager -> {
                    transactionRepository.delete(manager);
                    transactionRepository.flush();
                    return true;
                })
                .orElse(false);
    }


}