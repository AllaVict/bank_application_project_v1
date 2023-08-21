package com.bank.core.util.product;

import com.bank.core.util.Converter;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.bank.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ProductCreateUpdateConverter implements Converter<ProductCreateUpdateDTO, Product> {

    private final ManagerRepository managerRepository;

    @Override
    public Product convert(ProductCreateUpdateDTO fromProductCreateUpdateDTO, Product toProduct) {
        copy(fromProductCreateUpdateDTO, toProduct);
        return toProduct;
    }

    @Override
    public Product convert(ProductCreateUpdateDTO productCreateUpdateDTO) {
        Product product=new Product();
        copy(productCreateUpdateDTO, product);
        return product;
    }

    private void copy(ProductCreateUpdateDTO object, Product product) {
        product.setId(object.getId());
        product.setManager(getManager(object.getManagerId()));
        product.setProductStatus(object.getProductStatus());
        product.setProductName(object.getProductName());
        product.setCurrencyCode(object.getCurrencyCode());
        product.setInterestRate(object.getInterestRate());
        product.setCreditLimit(object.getCreditLimit());
        product.setCreatedAt(object.getCreatedAt());
        product.setUpdatedAt(object.getUpdatedAt());

    }

    public Manager getManager(Long managerId) {
        return Optional.ofNullable(managerId)
                .flatMap(managerRepository::findById)
                .orElse(null);
    }

}
