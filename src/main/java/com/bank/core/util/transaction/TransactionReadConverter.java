package com.bank.core.util.transaction;

import com.bank.core.util.Converter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
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
       ProductReadDTO product = Optional.ofNullable(transaction.getProduct())
                .map(productReadConverter::convert)
                .orElse(null);

        BankAccountReadDTO bankAccount = Optional.ofNullable(transaction.getBankAccount())
                .map(bankAccountReadConverter::convert)
                .orElse(null);

        return new TransactionReadDTO(
                transaction.getId(),
                bankAccount,
                product,
                transaction.getSender(),
                transaction.getSource_account(),
                transaction.getBeneficiary(),
                transaction.getDestination_account(),
                transaction.getTransaction_amount(),
                transaction.getDescription(),
                transaction.getInterest_rate(),
                transaction.getTransaction_type(),
                transaction.getTransaction_status(),
                transaction.getTransaction_code(),
                transaction.getTransaction_date(),
                transaction.getEffective_date()
           );


    }
}
