package com.bank.model.dto.bankaccount;

import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FindBankAccountForClient {

    Long id;
    String clientName;
    Long productId;
    String accountName;
    String accountNumber;
    BankAccountType accountType;
    BankAccountStatus status;
    BigDecimal balance;
    CurrencyCode currencyCode;

}
