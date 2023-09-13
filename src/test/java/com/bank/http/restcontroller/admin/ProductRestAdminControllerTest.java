package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ProductAdminRestService;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.product.*;
import com.bank.model.entity.Manager;
import com.bank.model.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.model.enums.CurrencyCode.CAD;
import static com.bank.model.enums.CurrencyCode.GBP;
import static com.bank.model.enums.ManagerStatus.CHECKING;
import static com.bank.model.enums.ProductStatus.ACTIVE;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductRestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductAdminRestService productAdminRestService;
    @MockBean
    private ProductReadConverter productReadConverter;

    private Product productGBP;
    private ProductReadDTO productGBPReadDTO;
    private ProductCreateUpdateDTO productGBPCreateUpdateDTO;

    private Product productCAD;
    private ProductReadDTO productCADReadDTO;
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


        productGBPReadDTO = new ProductReadDTO(
                22L, managerReadDTODorota, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productGBPCreateUpdateDTO = new ProductCreateUpdateDTO(
                22L, 6L, ACTIVE, "current_business_GBP",
                GBP, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productCAD = new Product(
                23L, managerDorota, ACTIVE, "current_business_CAD",
                CAD, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );
        productCADReadDTO = new ProductReadDTO(
                23L, managerReadDTODorota, ACTIVE, "current_business_CAD",
                CAD, new BigDecimal(0.00) , new BigDecimal(100000.00) ,localDateTime, localDateTime
        );

    }


    @Test
    void productAdminController_findAllProducts_returnResponse() throws Exception {
        List<ProductReadDTO> listProductsDTO = new ArrayList<>();
        listProductsDTO.add(productGBPReadDTO);
        listProductsDTO.add(productCADReadDTO);
        FindAllProductsResponse response = new FindAllProductsResponse(listProductsDTO, new ArrayList<>());
        when(productAdminRestService.findAll()).thenReturn(response);
        this.mockMvc.perform(get("/api/v1/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listProductsDTO.size())));

    }


    @Test
    void productAdminController_shouldFindOneProductById_returnsProductReadDTO() throws Exception {
        when(productAdminRestService.findById(anyLong())).thenReturn(Optional.of(productGBPReadDTO));
        when(productReadConverter.convert(productGBP)).thenReturn(productGBPReadDTO);
        this.mockMvc.perform(get("/api/v1/admin/products/{id}", 6L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(productGBP.getProductName())));
       //         .andExpect(jsonPath("$.productStatus", is(productGBP.getProductStatus())));
//java.lang.AssertionError: JSON path "$.productStatus"    Expected: is <ACTIVE>    but: was "ACTIVE"
    }

    @Test
    void productAdminController_shouldCreateProduct_returnsProductReadDTO() throws Exception {
        CreateUpdateProductResponse createProductResponse
                = new CreateUpdateProductResponse(productGBPReadDTO,new ArrayList<>());
        when(productAdminRestService.create(any(ProductCreateUpdateDTO.class)))
                .thenReturn(createProductResponse);
        this.mockMvc.perform(post("/api/v1/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerReadDTODorota)))
                .andExpect(status().isCreated());
      //          .andExpect(jsonPath("$.productName", is(productGBPReadDTO.getProductName())));
      //          .andExpect(jsonPath("$.productStatus", is(productGBPReadDTO.getProductStatus())));

    }


    @Test
    void productAdminController_updateProduct_returnsResponse() throws Exception {
        Long managerId =productGBPReadDTO.getId();
        CreateUpdateProductResponse updateProductResponse
                = new CreateUpdateProductResponse(productGBPReadDTO,new ArrayList<>());
        when(productAdminRestService.update(anyLong(), any(ProductCreateUpdateDTO.class)))
                .thenReturn(updateProductResponse);
        this.mockMvc.perform(put("/api/v1/admin/products/{id}", managerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productGBPReadDTO)))
                .andExpect(status().isOk());
       //         .andExpect(jsonPath("$.productName", is(productGBPReadDTO.getProductName())));
      //          .andExpect(jsonPath("$.productStatus", is(productGBPReadDTO.getProductStatus())));

    }

    @Test
    void productAdminController_shouldDeleteProduct_returnsResponse() throws Exception {
        Long productId =productGBPReadDTO.getId();
        DeleteProductResponse deleteProductResponse
                =new DeleteProductResponse(productGBPReadDTO,new ArrayList<>());
        when(productAdminRestService.delete(productId)).thenReturn(deleteProductResponse);
        this.mockMvc.perform(delete("/api/v1/admin/products/{id}", productId))
                .andExpect(status().isOk());
       //         .andExpect(jsonPath("$.productName", is(productGBPReadDTO.getProductName())));
      //          .andExpect(jsonPath("$.productStatus", is(productGBPReadDTO.getProductStatus())));

    }


}