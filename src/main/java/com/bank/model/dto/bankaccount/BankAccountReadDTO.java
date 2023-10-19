package com.bank.model.dto.bankaccount;


import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class BankAccountReadDTO {

    Long id;
    ClientReadDTO client;
    ProductReadDTO product;
    String accountName;
    String accountNumber;
    BankAccountType accountType;
    BankAccountStatus status;
    BigDecimal balance;
    CurrencyCode currencyCode;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
