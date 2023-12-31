package com.bank.model.dto.bankaccount;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllBankAccountsResponse {
    private List<BankAccountReadDTO> bankAccounts;
    private List<CoreError> errors;

}
