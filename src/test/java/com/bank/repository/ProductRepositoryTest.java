package com.bank.repository;

import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static com.bank.model.enums.ProductStatus.PROMOTIONAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ManagerRepository managerRepository;
    private Product productGBP;
    private Manager managerDorota ;
    private static final Long PRODUCT_ID =18L;
    private static final Long MANAGER_ID = 5L;


    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = managerRepository.findById(MANAGER_ID).get();
        productGBP = new Product(
                22L, managerDorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

    }

    @Test
    @DisplayName("It should save the Product to the database")
    void saveProduct() {
        Product actualResult = productRepository.save(productGBP);
        assertEquals(productGBP.getProductName(), actualResult.getProductName());
        assertEquals(productGBP.getManager().getId(), actualResult.getManager().getId());
        assertNotNull(actualResult);
        assertThat(actualResult.getId()).isNotEqualTo(null);
        assertThat(actualResult.getProductName()).isEqualTo("current_business_GBP");
//        assertSame(userDto.getRole(), actualResult.getRole());

    }

    @Test
    @DisplayName("It should return the Products list with size of ")
    void getAllProducts() {
        List<Product> result = productRepository.findAll();
        assertNotNull(result);
        assertThat(result).isNotNull();
        assertEquals(18, result.size());
        assertThat(result).hasSize(18);
    }


    @Test
    @DisplayName("It should return the Product by its id")
    void findProductById() {
        Optional<Product> maybeProduct = productRepository.findById(PRODUCT_ID);
        assertTrue(maybeProduct.isPresent());
        maybeProduct.ifPresent(product
            -> assertEquals("credit_business_EUR", product.getProductName()));

    }

    @Test
    @DisplayName("It should update the Product with new status")
    void updateProduct() {
        productGBP.setProductStatus(PROMOTIONAL);
        Optional<Product> actualResult = Optional.of(productRepository.saveAndFlush(productGBP));
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(product -> {
            assertEquals(productGBP.getProductName(), product.getProductName());
            assertEquals(productGBP.getManager().getId(), product.getManager().getId());
            assertEquals(productGBP.getProductStatus(), product.getProductStatus());
         //   assertSame(userDto.getRole(), product.getRole());
        });
     }

    @Test
    @DisplayName("It should delete the existing Product")
    void deleteProduct() {
         productRepository.save(productGBP);
         productRepository.deleteById(productGBP.getId());
         Optional<Product> productForDelete = productRepository.findById(productGBP.getId());
         assertFalse(productForDelete.isPresent());
         Assertions.assertThat(productForDelete).isEmpty();

    }


}