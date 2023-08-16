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
        transaction.setBankAccount(getBankAccount(object.getAccount_id()));
        transaction.setProduct(getProduct(object.getProduct_id()));
        transaction.setSender(object.getSender());
        transaction.setSource_account(object.getSource_account());
        transaction.setBeneficiary(object.getBeneficiary());
        transaction.setDestination_account(object.getDestination_account());
        transaction.setTransaction_amount(object.getTransaction_amount());
        transaction.setDescription(object.getDescription());
        transaction.setInterest_rate(object.getInterest_rate());
        transaction.setTransaction_type(object.getTransaction_type());
        transaction.setTransaction_status(object.getTransaction_status());
        transaction.setTransaction_code(object.getTransaction_code());
        transaction.setTransaction_date(object.getTransaction_date());
        transaction.setTransaction_date(object.getEffective_date());

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
