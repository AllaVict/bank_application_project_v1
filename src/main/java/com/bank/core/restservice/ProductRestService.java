package com.bank.core.restservice;


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
public class ProductRestService {

    private final ProductRepository productRepository;
    private final ProductReadConverter productReadConverter;
    private final ProductCreateUpdateConverter productCreateUpdateConverter;

    public FindAllProductsResponse findAll(String accessKey) {
        List<ProductReadDTO> allProducts;
        if (!productRepository.findAll().isEmpty()) {
           allProducts = productRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(product ->productReadConverter.convert(product))
                    .map(productReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllProductsResponse(allProducts, new ArrayList<>());
    }

    public Optional<ProductReadDTO> findById(Long id) {
        return Optional.of(productRepository.findById(id)
                .map(productReadConverter::convert).orElseThrow());
    }

    public CreateUpdateProductResponse create(ProductCreateUpdateDTO productCreateUpdateDTO) {
        productCreateUpdateDTO.setCreated_at(LocalDateTime.now());
        ProductReadDTO productReadDTO= Optional.of(productCreateUpdateDTO)
                .map(productCreateUpdateConverter::convert)//productCreateConverter  managerCreateDTO ->Manager
                .map(productRepository::save)   // .map(product -> productRepository.save(product))
                .map(productReadConverter::convert) //productReadConverter  Product -> ProductReadDTO
                .orElseThrow();
        return new CreateUpdateProductResponse(productReadDTO, new ArrayList<>());
    }

    public CreateUpdateProductResponse update(Long id, ProductCreateUpdateDTO productCreateUpdateDTO) {
        Optional<Product> productForUpdate = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Product not found")));
        productCreateUpdateDTO.setCreated_at(productForUpdate.get().getCreated_at());
        productCreateUpdateDTO.setUpdated_at(LocalDateTime.now());
        return new CreateUpdateProductResponse(
                productForUpdate.map(product -> productCreateUpdateConverter.convert(productCreateUpdateDTO, (Product) product))
                        .map(productRepository::saveAndFlush) // save productCreateDTO
                        .map(productReadConverter::convert).orElseThrow()  // Product -> ProductReadDTO
                , new ArrayList<>());
    }

    public DeleteProductResponse delete(Long request){
        Product productforDelete =  productRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Product not found"));
        productRepository.delete(productforDelete);
        return new DeleteProductResponse(productReadConverter.convert(productforDelete), new ArrayList<>());

    }

}
