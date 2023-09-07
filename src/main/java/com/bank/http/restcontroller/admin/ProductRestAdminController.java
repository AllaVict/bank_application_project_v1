package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ProductAdminRestService;

import com.bank.model.dto.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class ProductRestAdminController {
    private final ProductAdminRestService productAdminRestService;

    /**
     GET   http://localhost:8080/api/v1/admin/products
     */
    @GetMapping("/products")
    public ResponseEntity<FindAllProductsResponse> findAllProducts(){
        FindAllProductsResponse response = productAdminRestService.findAll();
        log.info("Reading all products ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/admin/products/{id}
     */
    @GetMapping("/products/{id}")
    public ProductReadDTO findByIdProduct(@PathVariable("id") Long id) {
        log.info("Reading product by id " +id);
        return productAdminRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   http://localhost:8080/api/v1/admin/products

     {
     "managerId": "2",
     "productStatus": "ACTIVE",
     "productName": "current_personal_PLN",
     "currencyCode": "PLN",
     "interestRate": "2",
     "creditLimit": "77000.00"
     }
     */
    @PostMapping("/products")
    public ResponseEntity<CreateUpdateProductResponse> createProduct(@RequestBody ProductCreateUpdateDTO request){
        CreateUpdateProductResponse response  = productAdminRestService.create(request);
        log.info("Creating product " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/admin/products/22
     {
     "id": "22",
     "managerId": "3",
     "productStatus": "PROMOTIONAL",
     "productName": "current_personal_PLN",
     "currencyCode": "PLN",
     "interestRate": 8,
     "creditLimit": 88000.00
     }


     */
    @PutMapping("/products/{id}")
    public ResponseEntity<CreateUpdateProductResponse> updateProduct(@PathVariable("id") Long id,
                                                                     @RequestBody ProductCreateUpdateDTO request){
        CreateUpdateProductResponse response = productAdminRestService.update(request.getId(),request);
        log.info("Updating product " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/admin/products/{id}
     */
    @DeleteMapping("/products/{id}")
    //public void deleteProduct(@RequestBody DeleteProductRequest request) {
    public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable("id") Long id) {
        DeleteProductResponse response =  productAdminRestService.delete(id);
        log.info("Deleting product " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





