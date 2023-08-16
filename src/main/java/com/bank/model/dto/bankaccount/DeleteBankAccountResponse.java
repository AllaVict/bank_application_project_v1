package com.bank.model.dto.bankaccount;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class DeleteBankAccountResponse {
    private BankAccountReadDTO bankAccount;
    private List<CoreError> errors;
}
