package com.bank.core.restservice;

import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.transaction.*;
import com.bank.model.entity.Transaction;
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
public class TransactionRestService {

    private final TransactionRepository transactionRepository;
    private final TransactionReadConverter transactionReadConverter;
    private final TransactionCreateUpdateConverter transactionCreateUpdateConverter;

    public FindAllTransactionsResponse findAll(String accessKey) {
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
        transactionCreateUpdateDTO.setTransaction_date(LocalDateTime.now());
        TransactionReadDTO clientReadDTO= Optional.of(transactionCreateUpdateDTO)
                .map(transactionCreateUpdateConverter::convert)//ClientCreateConverter  ClientCreateDTO ->Client
                .map(transactionRepository::save)   // .map(Client -> ClientRepository.save(Client))
                .map(transactionReadConverter::convert) //ClientReadConverter  Client -> ClientReadDTO
                .orElseThrow();
        return new CreateUpdateTransactionResponse(clientReadDTO, new ArrayList<>());
    }

    public CreateUpdateTransactionResponse update(Long id, TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        Optional<Transaction> transactionForUpdate = Optional.ofNullable(transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transaction not found")));
        transactionCreateUpdateDTO.setTransaction_date(transactionForUpdate.get().getTransaction_date());
        transactionCreateUpdateDTO.setEffective_date(LocalDateTime.now());

        return new CreateUpdateTransactionResponse(
                transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                        .map(transactionRepository::saveAndFlush) // save clientCreateUpdateDTO
                        .map(transactionReadConverter::convert).orElseThrow()  // client -> clientReadDTO
                , new ArrayList<>());
    }

    public DeleteTransactionResponse delete(Long request){
        Transaction clientForDelete =  transactionRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Transaction not found"));
        transactionRepository.delete(clientForDelete);
        return new DeleteTransactionResponse(transactionReadConverter.convert(clientForDelete), new ArrayList<>());

    }

}
