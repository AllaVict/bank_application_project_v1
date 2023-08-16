package com.bank.http.restcontroller;

import com.bank.core.restservice.ProductRestService;
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
@RequestMapping(value = "/api/v1")
public class ProductRestController {
    private final ProductRestService productRestService;

    /**
     GET   /api/v1/products
     */
    @GetMapping("/products")
    public ResponseEntity<FindAllProductsResponse> findAllProducts(@RequestBody String accessKey){
        FindAllProductsResponse response = productRestService.findAll(accessKey);
        log.info("Reading all products ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   /api/v1/products/{id}
     */
    @GetMapping("/products/{id}")
    public ProductReadDTO findByIdProduct(@PathVariable("id") Long id) {
        log.info("Reading product by id " +id);
        return productRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/products
     {
     "manager_id": "2",
     "product_status": "ACTIVE",
     "product_name": "current_personal_PLN",
     "currency_code": "PLN"
     "interest_rate": "2"
     "credit_limit": "135000.00"
     }
     */
    @PostMapping("/products")
    public ResponseEntity<CreateUpdateProductResponse> createProduct(@RequestBody ProductCreateUpdateDTO request){
        CreateUpdateProductResponse response  = productRestService.create(request);
        log.info("Creating product " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   /api/v1/products/58
     {
     "id": "58",
     "manager_id": "2",
     "product_status": "PROMOTIONAL",
     "product_name": "current_personal_PLN",
     "currency_code": "PLN",
     "interest_rate": "2",
     "credit_limit": "135000.00"
     }
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<CreateUpdateProductResponse> updateProduct(@PathVariable("id") Long id,
                                                                     @RequestBody ProductCreateUpdateDTO request){
        CreateUpdateProductResponse response = productRestService.update(request.getId(),request);
        log.info("Updating product " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   /api/v1/products/{id}
     */
    @DeleteMapping("/products/{id}")
    //public void deleteProduct(@RequestBody DeleteProductRequest request) {
    public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable("id") Long id) {
        DeleteProductResponse response =  productRestService.delete(id);
        log.info("Deleting product " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





