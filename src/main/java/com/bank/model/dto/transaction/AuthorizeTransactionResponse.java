package com.bank.model.dto.transaction;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthorizeTransactionResponse {

    private TransactionReadDTO transaction;
    private String message;
    private List<CoreError> errors;

}
