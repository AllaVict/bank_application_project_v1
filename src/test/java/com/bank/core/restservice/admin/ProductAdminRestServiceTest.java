package com.bank.core.restservice.admin;

import com.bank.core.util.product.ProductCreateUpdateConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.dto.product.ProductReadDTO;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.bank.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.CurrencyCode.CAD;
import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static com.bank.model.enums.ProductStatus.PROMOTIONAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdminRestServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCreateUpdateConverter productCreateUpdateConverter;
    @Mock
    private ProductReadConverter productReadConverter;
    @InjectMocks
    private ProductAdminRestService productAdminRestService;

    private Product productGBP;
    private ProductReadDTO productGBPReadDTO;
    private ProductCreateUpdateDTO productGBPCreateUpdateDTO;

    private Product productCAD;
    private Manager managerDorota;
    private ManagerReadDTO managerReadDTODorota;


    @BeforeEach
    void init() {
        LocalDateTime localDateTime =LocalDateTime.now();
        managerDorota = new Manager(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );
        managerReadDTODorota = new ManagerReadDTO(
                6L, "Dorota", "Dancer",
                CHECKING, "description", localDateTime
        );

        productGBP = new Product(
                22L, managerDorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productCAD = new Product(
                23L, managerDorota, ACTIVE, "current_business_CAD",
                CAD, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

        productGBPReadDTO = new ProductReadDTO(
                22L, managerReadDTODorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productGBPCreateUpdateDTO = new ProductCreateUpdateDTO(
                22L, 6L, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );


    }


    @Test
    void productAdminService_createProduct_returnsProductReadDto() {
        when(productRepository.save(any(Product.class))).thenReturn(productGBP);
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        when(productCreateUpdateConverter.convert(productGBPCreateUpdateDTO)).thenReturn(productGBP);
        ProductReadDTO createdProduct = productAdminRestService.create(productGBPCreateUpdateDTO).getProduct();
        assertNotNull(createdProduct);
        assertThat(createdProduct.getProductName()).isEqualTo("current_business_GBP");
     }

    @Test
    void productAdminService_findProductById_returnsProductReadDTO() {
        Long productId = 6L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(productGBP));
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        ProductReadDTO existingProduct = productAdminRestService.findById(productId).orElseThrow();
        assertNotNull(existingProduct);
        assertThat(existingProduct.getId()).isNotEqualTo(null);
    }


    @Test
    void productAdminService_findProductByIdForException() {
        when(productRepository.findById(8L)).thenReturn(Optional.of(productGBP));
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        assertThrows(RuntimeException.class, () -> {
            productAdminRestService.findById(productGBP.getId());
        });
    }

    @Test
    void productAdminService_findAllProducts_returnResponse() {
        List<Product> list = new ArrayList<>();
        list.add(productGBP);
        list.add(productCAD);
       // List<Product> list = productRepository.findAll();
        when(productRepository.findAll()).thenReturn(list);
        List<ProductReadDTO> result = productAdminRestService.findAll().getProducts();
        assertThat(result).hasSize(2);
        assertEquals(2, result.size());
        assertNotNull(result);

    }


    @Test
    void productAdminService_updateProduct_returnsProductReadDto() {
        LocalDateTime localDateTime =LocalDateTime.now();
        productGBP = new Product(
                22L, managerDorota, PROMOTIONAL, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productGBPReadDTO = new ProductReadDTO(
                22L, managerReadDTODorota, PROMOTIONAL, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productGBPCreateUpdateDTO = new ProductCreateUpdateDTO(
                22L, 6L, PROMOTIONAL, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        Long productId = productGBP.getId();
        when(productRepository.findById(productGBP.getId())).thenReturn(Optional.of(productGBP));
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(productGBP);
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        when(productCreateUpdateConverter.convert(productGBPCreateUpdateDTO, productGBP)).thenReturn(productGBP);
        ProductReadDTO existingProduct =  productAdminRestService.update(productId, productGBPCreateUpdateDTO)
                .getProduct();
        assertNotNull(existingProduct);
        assertEquals(PROMOTIONAL, existingProduct.getProductStatus());

    }


    @Test
    void productAdminService_deleteProductById_returnResponse() {
        Long productId = 6L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productGBP));
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        doNothing().when(productRepository).delete(any(Product.class));
        productAdminRestService.delete(productId);
        verify(productRepository, times(1)).delete(productGBP);

    }


}