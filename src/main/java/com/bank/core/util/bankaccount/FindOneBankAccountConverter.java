package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindOneBankAccountConverter implements Converter<BankAccount, FindBankAccountForClient> {

    private final ClientReadConverter clientReadConverter;
    private final ProductReadConverter productReadConverter;

    @Override
    public FindBankAccountForClient convert(BankAccount bankAccount) {
        ClientReadDTO client = Optional.ofNullable(bankAccount.getClient())
                .map(clientReadConverter::convert)
                .orElse(null);

        ProductReadDTO product = Optional.ofNullable(bankAccount.getProduct())
                .map(productReadConverter::convert)
                .orElse(null);

        String clientName = client.getFirstName()+" "+client.getLastName();

        return new FindBankAccountForClient(
                bankAccount.getId(),
                clientName,
                product.getId(),
                bankAccount.getAccountName(),
                bankAccount.getAccountNumber(),
                bankAccount.getAccountType(),
                bankAccount.getStatus(),
                bankAccount.getBalance(),
                bankAccount.getCurrencyCode()
            );
    }


}