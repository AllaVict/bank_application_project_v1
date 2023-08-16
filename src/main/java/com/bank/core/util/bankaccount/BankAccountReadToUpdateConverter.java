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
                bankAccountReadDTO.getAccount_name(),
                bankAccountReadDTO.getAccount_number(),
                bankAccountReadDTO.getAccount_type(),
                bankAccountReadDTO.getStatus(),
                bankAccountReadDTO.getBalance(),
                bankAccountReadDTO.getCurrency_code(),
                bankAccountReadDTO.getCreated_at(),
                bankAccountReadDTO.getUpdated_at()

        );

    }

}
