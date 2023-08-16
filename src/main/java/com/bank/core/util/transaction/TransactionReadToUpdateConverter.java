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
                transactionReadDTO.getProduct().getId(),
                transactionReadDTO.getSender(),
                transactionReadDTO.getSource_account(),
                transactionReadDTO.getBeneficiary(),
                transactionReadDTO.getDestination_account(),
                transactionReadDTO.getTransaction_amount(),
                transactionReadDTO.getDescription(),
                transactionReadDTO.getInterest_rate(),
                transactionReadDTO.getTransaction_type(),
                transactionReadDTO.getTransaction_status(),
                transactionReadDTO.getTransaction_code(),
                transactionReadDTO.getTransaction_date(),
                transactionReadDTO.getEffective_date()

        );

    }


}
