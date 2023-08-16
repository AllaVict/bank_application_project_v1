package com.bank.model.dto.product;

import com.bank.model.enums.CurrencyCode;
import com.bank.model.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateUpdateDTO {

    Long id;
    Long manager_id;
    ProductStatus product_status;
    @NotEmpty(message = "Product name does not empty")
    @Size(min = 3, max = 70, message = "Product name must be from 3 to 70")
    String product_name;
    CurrencyCode currency_code;
    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal interest_rate;
    @Min(value=0, message = "Credit limit must be 0 or bigger then 0 ")
    BigDecimal credit_limit;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    LocalDateTime created_at;
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime updated_at;



}
