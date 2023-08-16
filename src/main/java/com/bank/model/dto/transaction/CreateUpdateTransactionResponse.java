package com.bank.model.dto.transaction;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateUpdateTransactionResponse {
    private TransactionReadDTO transaction;
    private List<CoreError> errors;

}
