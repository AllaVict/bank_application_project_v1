package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountReadToUpdateConverter implements Converter<BankAccountReadDTO, BankAccountCreateUpdateDTO> {

    @Override
    public BankAccountCreateUpdateDTO convert(BankAccountReadDTO bankAccountReadDTO) {

        return new BankAccountCreateUpdateDTO(
                bankAccountReadDTO.getId(),
                bankAccountReadDTO.getClient().getId(),
                bankAccountReadDTO.getAccountName(),
                bankAccountReadDTO.getAccountNumber(),
                bankAccountReadDTO.getAccountType(),
                bankAccountReadDTO.getStatus(),
                bankAccountReadDTO.getBalance(),
                bankAccountReadDTO.getCurrencyCode(),
                bankAccountReadDTO.getCreatedAt(),
                bankAccountReadDTO.getUpdatedAt()

        );

    }

}
