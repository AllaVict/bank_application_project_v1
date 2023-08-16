package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.entity.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BankAccountReadConverter implements Converter<BankAccount, BankAccountReadDTO> {

    private final ClientReadConverter clientReadConverter;

    @Override
    public BankAccountReadDTO convert(BankAccount bankAccount) {
        ClientReadDTO client = Optional.ofNullable(bankAccount.getClient())
                .map(clientReadConverter::convert)
                .orElse(null);

        return new BankAccountReadDTO(
                bankAccount.getId(),
                client,
                bankAccount.getAccount_name(),
                bankAccount.getAccount_number(),
                bankAccount.getAccount_type(),
                bankAccount.getStatus(),
                bankAccount.getBalance(),
                bankAccount.getCurrency_code(),
                bankAccount.getCreated_at(),
                bankAccount.getUpdated_at()
        );
    }


}