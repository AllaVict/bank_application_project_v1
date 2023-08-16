package com.bank.core.util.product;

import com.bank.core.util.Converter;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReadToUpdateConverter implements Converter<ProductReadDTO, ProductCreateUpdateDTO> {

    @Override
    public ProductCreateUpdateDTO convert(ProductReadDTO productReadDTO) {
        return new ProductCreateUpdateDTO(
                productReadDTO.getId(),
                productReadDTO.getManager().getId(),
                productReadDTO.getProduct_status(),
                productReadDTO.getProduct_name(),
                productReadDTO.getCurrency_code(),
                productReadDTO.getInterest_rate(),
                productReadDTO.getCredit_limit(),
                productReadDTO.getCreated_at(),
                productReadDTO.getUpdated_at()

        );

    }


}
