package com.bank.model.dto.transaction;

import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReadDTO {

    Long id;
    BankAccountReadDTO bankAccount;
  //  ProductReadDTO product;
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
    LocalDateTime transactionDate;
    LocalDateTime effectiveDate;


}
