package com.bank.model.dto.transaction;

import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FindTransactionForClient {
    Long id;
    String clientName;
    String sender;
    String sourceAccount;
    String beneficiary;
    String destinationAccount;
    BigDecimal transactionAmount;
    String description;
    BigDecimal interestRate;
    TransactionType transactionType;
    TransactionStatus transactionStatus;
    String transactionCode;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime transactionDate;
    LocalDateTime effectiveDate;

}
