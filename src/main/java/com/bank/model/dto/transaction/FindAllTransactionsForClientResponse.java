package com.bank.model.dto.transaction;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class FindAllTransactionsForClientResponse {
    private List<FindTransactionForClient> transactions;
    private List<CoreError> errors;

}
