package com.bank.model.dto.product;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class FindAllProductsResponse {
    private List<ProductReadDTO> products;
    private List<CoreError> errors;
}
