package com.bank.model.entity;

import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
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
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    Client client;

    @NotEmpty(message = "Account name  does not empty")
    @Size(min = 1, max = 20, message = "Account name must be from 1 to 20")
    String account_name;

    @NotEmpty(message = "Account number  does not empty")
    @Size(min = 1, max = 26, message = "Account number must be 26")
    String account_number;

    @Enumerated(EnumType.STRING)
    BankAccountType account_type;

    @Enumerated(EnumType.STRING)
    BankAccountStatus status;

    @Min(value=0, message = "balance must be 0 or bigger then 0 ")
    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    CurrencyCode currency_code;

    LocalDateTime created_at;
    
    LocalDateTime updated_at;


}
