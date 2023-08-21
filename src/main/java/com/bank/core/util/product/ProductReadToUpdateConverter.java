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
                productReadDTO.getProductStatus(),
                productReadDTO.getProductName(),
                productReadDTO.getCurrencyCode(),
                productReadDTO.getInterestRate(),
                productReadDTO.getCreditLimit(),
                productReadDTO.getCreatedAt(),
                productReadDTO.getUpdatedAt()

        );

    }


}
