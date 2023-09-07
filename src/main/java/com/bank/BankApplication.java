package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);

    }
}

/**
 localhost:8080/admin/manager/managers
 localhost:8080/admin/product/products
 localhost:8080/admin/account/accounts
 localhost:8080/admin/client/clients
 localhost:8080/admin/transaction/transactions
 */
