package com.bank.core.util.transaction;

import com.bank.core.util.Converter;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionReadToUpdateConverter implements Converter<TransactionReadDTO, TransactionCreateUpdateDTO> {

    @Override
    public TransactionCreateUpdateDTO convert(TransactionReadDTO transactionReadDTO) {
        return new TransactionCreateUpdateDTO(
                transactionReadDTO.getId(),
                transactionReadDTO.getBankAccount().getId(),
                transactionReadDTO.getSender(),
                transactionReadDTO.getSourceAccount(),
                transactionReadDTO.getBeneficiary(),
                transactionReadDTO.getDestinationAccount(),
                transactionReadDTO.getTransactionAmount(),
                transactionReadDTO.getDescription(),
                transactionReadDTO.getInterestRate(),
                transactionReadDTO.getTransactionType(),
                transactionReadDTO.getTransactionStatus(),
                transactionReadDTO.getTransactionCode(),
                transactionReadDTO.getCreatedAt(),
                transactionReadDTO.getUpdatedAt(),
                transactionReadDTO.getTransactionDate(),
                transactionReadDTO.getEffectiveDate()
        );
    }


}
