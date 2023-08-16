package com.bank.core.service;

import com.bank.core.util.product.ProductCreateUpdateConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.Product;
import com.bank.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReadConverter productReadConverter;
    private final ProductCreateUpdateConverter productCreateUpdateConverter;

    public List<ProductReadDTO> findAll() {
        if (!productRepository.findAll().isEmpty()) {
            return productRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(product ->productReadConverter.convert(product))
                    .map(productReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // throw new ValidationException("Admin rights required");
    }

    public Optional<ProductReadDTO> findById(Long id) {
        return Optional.of(productRepository.findById(id)
                .map(productReadConverter::convert).orElseThrow());
    }

    @Transactional
    public ProductReadDTO create(ProductCreateUpdateDTO productCreateUpdateDTO) {
        // productRepository.save(productCreateDTO);
        // Optional.of Returns:an Optional with the value present Throws: NullPointerException – if value is null
        //public static <T> Optional<T> of(T value) {  return new Optional<>(Objects.requireNonNull(value))}
        productCreateUpdateDTO.setCreated_at(LocalDateTime.now());
        return Optional.of(productCreateUpdateDTO)
                //productCreateConverter  managerCreateDTO ->Manager
                .map(productCreateUpdateConverter::convert)
                //.map(product -> product.setCreated_at(LocalDateTime.now()) )
                // .map(product -> productRepository.save(product))
                .map(productRepository::save)
                //productReadConverter  Product -> ProductReadDTO
                .map(productReadConverter::convert)
                .orElseThrow();
    }

    @Transactional
    public Optional<ProductReadDTO> update(Long id, ProductCreateUpdateDTO productCreateUpdateDTO) {
        //productRepository.findById(id)// findById Product
        //.map(manager ->productRepository.saveAndFlush(productCreateEditDTO));
        Optional<Product> productForUpdate = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Product not found")));
        productCreateUpdateDTO.setCreated_at(productForUpdate.get().getCreated_at());
        productCreateUpdateDTO.setUpdated_at(LocalDateTime.now());
        System.out.println("!!!!!! Manager productForUpdate "+productForUpdate.get().getManager());
        System.out.println("!!!!!! Manager productCreateUpdateDTO "+productCreateUpdateDTO.getManager_id());

        return productForUpdate.map(product -> productCreateUpdateConverter.convert(productCreateUpdateDTO, product))
                .map(productRepository::saveAndFlush) // save Product
                .map(productReadConverter::convert);  // Product -> ProductReadDTO

    }

    @Transactional
    public Boolean delete(Long id) {
            return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    productRepository.flush();
                    return true;
                })
                .orElse(false);
        }

    }

