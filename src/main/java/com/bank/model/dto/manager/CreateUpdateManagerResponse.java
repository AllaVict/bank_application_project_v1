package com.bank.model.dto.manager;

import com.bank.core.validation.CoreError;
import com.bank.model.dto.product.ProductReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateUpdateManagerResponse {
    private ManagerReadDTO manager;
    private List<CoreError> errors;

}
