package com.bank.core.util.product;

import com.bank.core.util.Converter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.Product;
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
                product.getProduct_status(),
                product.getProduct_name(),
                product.getCurrency_code(),
                product.getInterest_rate(),
                product.getCredit_limit(),
                product.getCreated_at(),
                product.getUpdated_at()

        );

    }
}
