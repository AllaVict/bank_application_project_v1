package com.bank.core.util.product;

import com.bank.core.util.Converter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Product;
import com.bank.model.dto.product.ProductReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductReadConverter implements Converter<Product, ProductReadDTO> {

    private final ManagerReadConverter managerReadConverter;

    @Override
    public ProductReadDTO convert(Product product) {
        ManagerReadDTO manager = Optional.ofNullable(product.getManager())
                .map(managerReadConverter::convert)
                .orElse(null);

        return new ProductReadDTO(
                product.getId(),
                manager,
                product.getProductStatus(),
                product.getProductName(),
                product.getCurrencyCode(),
                product.getInterestRate(),
                product.getCreditLimit(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }


}
