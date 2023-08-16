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
        product.setManager(getManager(object.getManager_id()));
        product.setProduct_status(object.getProduct_status());
        product.setProduct_name(object.getProduct_name());
        product.setCurrency_code(object.getCurrency_code());
        product.setInterest_rate(object.getInterest_rate());
        product.setCredit_limit(object.getCredit_limit());
        product.setCreated_at(object.getCreated_at());
        product.setUpdated_at(object.getUpdated_at());

    }

    public Manager getManager(Long managerId) {
        return Optional.ofNullable(managerId)
                .flatMap(managerRepository::findById)
                .orElse(null);
    }

}
