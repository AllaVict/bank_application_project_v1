package com.bank.model.dto.product;

import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.enums.CurrencyCode;
import com.bank.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Value;

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
    ProductStatus product_status;
    String product_name;
    CurrencyCode currency_code;
    BigDecimal interest_rate;
    BigDecimal credit_limit;
    LocalDateTime created_at;
    LocalDateTime updated_at;



}
