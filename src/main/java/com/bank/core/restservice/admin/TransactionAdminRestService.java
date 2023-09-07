package com.bank.core.restservice.admin;

import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.transaction.*;
import com.bank.model.entity.Transaction;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.TransactionRepository;
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
public class TransactionAdminRestService {

    private final TransactionRepository transactionRepository;
    private final TransactionReadConverter transactionReadConverter;
    private final TransactionCreateUpdateConverter transactionCreateUpdateConverter;

    public FindAllTransactionsResponse findAll() {
        List<TransactionReadDTO> allTransactions;
        if (!transactionRepository.findAll().isEmpty()) {
            allTransactions = transactionRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(client ->clientReadConverter.convert(client))
                    .map(transactionReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllTransactionsResponse(allTransactions, new ArrayList<>());
    }

    public Optional<TransactionReadDTO> findById(Long id) {
        return Optional.of(transactionRepository.findById(id)
                .map(transactionReadConverter::convert).orElseThrow());
    }


    public CreateUpdateTransactionResponse create(TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        transactionCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        transactionCreateUpdateDTO.setTransactionType(TransactionType.INTERNAL);
        transactionCreateUpdateDTO.setTransactionStatus(TransactionStatus.DRAFT_INVALID);
         TransactionReadDTO transactionReadDTO= Optional.of(transactionCreateUpdateDTO)
                .map(transactionCreateUpdateConverter::convert)//TransactionCreateConverter  TransactionCreateDTO ->Transaction
                .map(transactionRepository::save)   // .map(Transaction -> TransactionRepository.save(Transaction))
                .map(transactionReadConverter::convert) //TransactionReadConverter  Transaction -> TransactionReadDTO
                .orElseThrow();
        return new CreateUpdateTransactionResponse(transactionReadDTO, new ArrayList<>());
    }

    public CreateUpdateTransactionResponse update(Long id, TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        Optional<Transaction> transactionForUpdate = Optional.ofNullable(transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transaction not found")));
        transactionCreateUpdateDTO.setCreatedAt(transactionForUpdate.get().getCreatedAt());
        transactionCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());
        transactionCreateUpdateDTO.setTransactionType(transactionForUpdate.get().getTransactionType());
        transactionCreateUpdateDTO.setTransactionStatus(transactionForUpdate.get().getTransactionStatus());
        return new CreateUpdateTransactionResponse(
                transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                        .map(transactionRepository::saveAndFlush) // save transactionCreateUpdateDTO
                        .map(transactionReadConverter::convert).orElseThrow()  // transaction -> transactionReadDTO
                , new ArrayList<>());
    }

    public DeleteTransactionResponse delete(Long request){
        Transaction clientForDelete =  transactionRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Transaction not found"));
        transactionRepository.delete(clientForDelete);
        return new DeleteTransactionResponse(transactionReadConverter.convert(clientForDelete), new ArrayList<>());

    }

}
