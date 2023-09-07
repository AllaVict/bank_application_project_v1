package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BankAccountReadConverter implements Converter<BankAccount, BankAccountReadDTO> {

    private final ClientReadConverter clientReadConverter;
    private final ProductReadConverter productReadConverter;

    @Override
    public BankAccountReadDTO convert(BankAccount bankAccount) {
        ClientReadDTO client = Optional.ofNullable(bankAccount.getClient())
                .map(clientReadConverter::convert)
                .orElse(null);

        ProductReadDTO product = Optional.ofNullable(bankAccount.getProduct())
                .map(productReadConverter::convert)
                .orElse(null);

        return new BankAccountReadDTO(
                bankAccount.getId(),
                client,
                product,
                bankAccount.getAccountName(),
                bankAccount.getAccountNumber(),
                bankAccount.getAccountType(),
                bankAccount.getStatus(),
                bankAccount.getBalance(),
                bankAccount.getCurrencyCode(),
                bankAccount.getCreatedAt(),
                bankAccount.getUpdatedAt()
        );
    }


}