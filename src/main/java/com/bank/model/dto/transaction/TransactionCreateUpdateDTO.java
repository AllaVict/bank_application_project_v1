package com.bank.model.dto.transaction;

import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateUpdateDTO {

    Long id;
    Long account_id;
    Long product_id;

    @NotEmpty(message = "sender name does not empty")
    @Size(min = 3, max = 100, message = "sender name must be from 3 to 100")
    String sender; // varchar(100),

    @NotEmpty(message = "source_account  does not empty")
    @Size(min = 26, max = 26, message = "source_account  must be 26")
    String source_account;

    @NotEmpty(message = "beneficiary does not empty")
    @Size(min = 3, max = 70, message = "beneficiary name must be from 3 to 100")
    String beneficiary; //  varchar(100),

    @NotEmpty(message = "destination account  does not empty")
    @Size(min = 26, max = 26, message = "destination account  must be 26")
    String destination_account;

    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal transaction_amount; // numeric(25,2),

    String description;//varchar(150),

    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal interest_rate;

    @Enumerated(EnumType.STRING)
    TransactionType transaction_type;

    @Enumerated(EnumType.STRING)
    TransactionStatus transaction_status;

    String transaction_code;
    LocalDateTime transaction_date;
    LocalDateTime effective_date;



}
