package com.bank.model.dto.product;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateUpdateProductResponse {
    private ProductReadDTO product;
    private List<CoreError> errors;

}
