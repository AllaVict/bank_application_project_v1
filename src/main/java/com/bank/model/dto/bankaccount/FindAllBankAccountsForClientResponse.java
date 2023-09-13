package com.bank.model.dto.bankaccount;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FindAllBankAccountsForClientResponse {
    private List<FindBankAccountForClient> bankAccounts;
    private List<CoreError> errors;

}
