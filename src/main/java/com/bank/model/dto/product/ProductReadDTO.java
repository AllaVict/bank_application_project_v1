package com.bank.model.dto.product;

import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.enums.CurrencyCode;
import com.bank.model.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 @Value is the immutable variant of @Data ; all fields are made private and final by default,
 and setters are not generated. The class itself is also made final by default,
 because immutability is not something that can be forced onto a subclass.
 */

@Value
//@Data
@AllArgsConstructor
public class ProductReadDTO {

    Long id;
    ManagerReadDTO manager;
    ProductStatus productStatus;
    String productName;
    CurrencyCode currencyCode;
    BigDecimal interestRate;
    BigDecimal creditLimit;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;



}
