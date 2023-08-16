package com.bank.model.dto.transaction;

import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
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
    ProductReadDTO product;
    String sender;
    String source_account;
    String beneficiary;
    String destination_account;
    BigDecimal transaction_amount;
    String description;
    BigDecimal interest_rate;
    TransactionType transaction_type;
    TransactionStatus transaction_status;
    String transaction_code;
    LocalDateTime transaction_date;
    LocalDateTime effective_date;


}
