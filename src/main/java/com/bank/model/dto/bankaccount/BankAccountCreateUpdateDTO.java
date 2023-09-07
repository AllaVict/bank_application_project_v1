package com.bank.model.dto.bankaccount;

import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** @Value is the immutable variant of @Data ;
 all fields are made private and final by default, and setters are not generated.
 */
//@Value
@Data
@AllArgsConstructor
public class BankAccountCreateUpdateDTO {

    Long id;
    Long clientId;
    Long productId;

    @NotEmpty(message = "Account name code does not empty")
    @Size(min = 1, max = 20, message = "Account name must be from 1 to 20")
    String accountName;

    @NotEmpty(message = "Account number  does not empty")
    @Size(min = 1, max = 26, message = "Account number must be 26")
    String accountNumber;

    @Enumerated(EnumType.STRING)
    BankAccountType bankAccountType;

    @Enumerated(EnumType.STRING)
    BankAccountStatus status;

    @Min(value=0, message = "balance must be 0 or bigger then 0 ")
    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    CurrencyCode currencyCode;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}

