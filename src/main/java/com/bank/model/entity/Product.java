package com.bank.model.entity;

import com.bank.model.enums.CurrencyCode;
import com.bank.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    Manager manager;

    @Enumerated(EnumType.STRING)
    ProductStatus product_status;

    @NotEmpty(message = "Product name does not empty")
    @Size(min = 3, max = 70, message = "Product name must be from 3 to 70")
    String product_name;

    @Enumerated(EnumType.STRING)
    CurrencyCode currency_code;

    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal interest_rate;

    @Min(value=0, message = "Credit limit must be 0 or bigger then 0 ")
    BigDecimal  credit_limit;

    LocalDateTime created_at;

    LocalDateTime updated_at;


}





