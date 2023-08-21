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
    Long managerId;
    ProductStatus productStatus;
    @NotEmpty(message = "Product name does not empty")
    @Size(min = 3, max = 70, message = "Product name must be from 3 to 70")
    String productName;
    CurrencyCode currencyCode;
    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal interestRate;
    @Min(value=0, message = "Credit limit must be 0 or bigger then 0 ")
    BigDecimal creditLimit;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    LocalDateTime updatedAt;



}
