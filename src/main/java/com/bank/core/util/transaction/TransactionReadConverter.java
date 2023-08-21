package com.bank.core.util.transaction;

import com.bank.core.util.Converter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import com.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionReadConverter implements Converter<Transaction, TransactionReadDTO> {

    private final ProductReadConverter productReadConverter;
    private final BankAccountReadConverter bankAccountReadConverter;

    @Override
    public TransactionReadDTO convert(Transaction transaction) {
//       ProductReadDTO product = Optional.ofNullable(transaction.getProduct())
//                .map(productReadConverter::convert)
//                .orElse(null);

        BankAccountReadDTO bankAccount = Optional.ofNullable(transaction.getBankAccount())
                .map(bankAccountReadConverter::convert)
                .orElse(null);

        return new TransactionReadDTO(
                transaction.getId(),
                bankAccount,
                transaction.getSender(),
                transaction.getSourceAccount(),
                transaction.getBeneficiary(),
                transaction.getDestinationAccount(),
                transaction.getTransactionAmount(),
                transaction.getDescription(),
                transaction.getInterestRate(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getTransactionCode(),
                transaction.getTransactionDate(),
                transaction.getEffectiveDate()
           );


    }
}
