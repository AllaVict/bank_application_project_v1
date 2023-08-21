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
    ProductStatus productStatus;

    @NotEmpty(message = "Product name does not empty")
    @Size(min = 3, max = 70, message = "Product name must be from 3 to 70")
    String productName;

    @Enumerated(EnumType.STRING)
    CurrencyCode currencyCode;

    @Min(value=0, message = "Interest rate must be 0 or bigger then 0 ")
    BigDecimal interestRate;

    @Min(value=0, message = "Credit limit must be 0 or bigger then 0 ")
    BigDecimal creditLimit;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;


}





