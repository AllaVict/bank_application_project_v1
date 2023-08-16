package com.bank.model.dto.bankaccount;


import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 @Value is the immutable variant of @Data ; all fields are made private and final by default,
 and setters are not generated. The class itself is also made final by default,
 because immutability is not something that can be forced onto a subclass.
 */

@Value
@AllArgsConstructor
public class BankAccountReadDTO {

    Long id;
    ClientReadDTO client;
    String account_name;
    String account_number;
    BankAccountType account_type;
    BankAccountStatus status;
    BigDecimal balance;
    CurrencyCode currency_code;
    LocalDateTime created_at;
    LocalDateTime updated_at;

}
