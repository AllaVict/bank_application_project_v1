package com.bank.core.util.transaction;

import com.bank.core.util.Converter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.transaction.FindTransactionForClient;
import com.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindOneTransactionConverter implements Converter<Transaction, FindTransactionForClient> {

        private final BankAccountReadConverter bankAccountReadConverter;


        @Override
        public FindTransactionForClient convert(Transaction transaction) {

            BankAccountReadDTO bankAccount = Optional.ofNullable(transaction.getBankAccount())
                    .map(bankAccountReadConverter::convert)
                    .orElse(null);
            String clientName = bankAccount.getClient().getFirstName()+" "
                    +bankAccount.getClient().getLastName();

            return new FindTransactionForClient(
                    transaction.getId(),
                    clientName,
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
                    transaction.getCreatedAt(),
                    transaction.getUpdatedAt(),
                    transaction.getTransactionDate(),
                    transaction.getEffectiveDate()
            );


        }
    }
