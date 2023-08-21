package com.bank.core.util.transaction;

import com.bank.core.util.Converter;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Product;
import com.bank.model.entity.Transaction;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TransactionCreateUpdateConverter implements Converter<TransactionCreateUpdateDTO, Transaction> {

    private final BankAccountRepository bankAccountRepository;
    private final ProductRepository productRepository;

    @Override
    public Transaction convert(TransactionCreateUpdateDTO fromTransactionCreateUpdateDTO, Transaction toTransaction) {
        copy(fromTransactionCreateUpdateDTO, toTransaction);
        return toTransaction;
    }

    @Override
    public Transaction convert(TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        Transaction transaction=new Transaction();
        copy(transactionCreateUpdateDTO, transaction);
        return transaction;
    }

    private void copy(TransactionCreateUpdateDTO object, Transaction transaction) {
        transaction.setId(object.getId());
        transaction.setBankAccount(getBankAccount(object.getAccountId()));
        //transaction.setProduct(getProduct(object.getProduct_id()));
        transaction.setSender(object.getSender());
        transaction.setSourceAccount(object.getSourceAccount());
        transaction.setBeneficiary(object.getBeneficiary());
        transaction.setDestinationAccount(object.getDestinationAccount());
        transaction.setTransactionAmount(object.getTransactionAmount());
        transaction.setDescription(object.getDescription());
        transaction.setInterestRate(object.getInterestRate());
        transaction.setTransactionType(object.getTransactionType());
        transaction.setTransactionStatus(object.getTransactionStatus());
        transaction.setTransactionCode(object.getTransactionCode());
        transaction.setTransactionDate(object.getTransactionDate());
        transaction.setTransactionDate(object.getEffectiveDate());

    }

    public BankAccount getBankAccount(Long bankAccountId) {
        return Optional.ofNullable(bankAccountId)
                .flatMap(bankAccountRepository::findById)
                .orElse(null);
    }
    public Product getProduct(Long productId) {
        return Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElse(null);
    }

}
