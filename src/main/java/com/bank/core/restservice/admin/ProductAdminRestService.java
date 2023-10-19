package com.bank.core.restservice.admin;


import com.bank.core.util.product.ProductCreateUpdateConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.product.*;
import com.bank.model.entity.Product;
import com.bank.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ProductAdminRestService {

    private final ProductRepository productRepository;
    private final ProductReadConverter productReadConverter;
    private final ProductCreateUpdateConverter productCreateUpdateConverter;

    public FindAllProductsResponse findAll() {
        List<ProductReadDTO> allProducts;
        if (!productRepository.findAll().isEmpty()) {
           allProducts = productRepository.findAll().stream()
                    .map(productReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        return new FindAllProductsResponse(allProducts, new ArrayList<>());
    }

    public Optional<ProductReadDTO> findById(Long id) {
        return Optional.of(productRepository.findById(id)
                .map(productReadConverter::convert).orElseThrow());
    }

    public CreateUpdateProductResponse create(ProductCreateUpdateDTO productCreateUpdateDTO) {
        productCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        ProductReadDTO productReadDTO= Optional.of(productCreateUpdateDTO)
                .map(productCreateUpdateConverter::convert)
                .map(productRepository::save)
                .map(productReadConverter::convert)
                .orElseThrow();
        return new CreateUpdateProductResponse(productReadDTO, new ArrayList<>());
    }

    public CreateUpdateProductResponse update(Long id, ProductCreateUpdateDTO productCreateUpdateDTO) {
        Optional<Product> productForUpdate = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Product not found")));
        productCreateUpdateDTO.setCreatedAt(productForUpdate.get().getCreatedAt());
        productCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());
        return new CreateUpdateProductResponse(
                productForUpdate.map(product -> productCreateUpdateConverter.convert(productCreateUpdateDTO, product))
                        .map(productRepository::saveAndFlush)
                        .map(productReadConverter::convert).orElseThrow()
                , new ArrayList<>());
    }

    public DeleteProductResponse delete(Long request){
        Product productforDelete =  productRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Product not found"));
        productRepository.delete(productforDelete);
        return new DeleteProductResponse(productReadConverter.convert(productforDelete), new ArrayList<>());

    }

}
