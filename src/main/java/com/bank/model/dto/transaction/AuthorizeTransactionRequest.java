package com.bank.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AuthorizeTransactionRequest {
    Long id;
    Long accountId;
    String beneficiary;
    String destinationAccount;
    BigDecimal transactionAmount;
    String description;

}
