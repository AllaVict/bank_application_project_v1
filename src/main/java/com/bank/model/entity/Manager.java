package com.bank.model.entity;

import com.bank.model.enums.ManagerStatus;
import com.bank.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 @Data is a convenient shortcut annotation that bundles the features of @ToString,
 @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together:
 In other words, @Data generates all the boilerplate that is normally associated with
 simple POJOs (Plain Old Java Objects) and beans:
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ProductStatus product_status;

    @NotEmpty(message = "First name name does not empty")
    @Size(min = 1, max = 50, message = "Product name must be from 1 to 50")
    String first_name;

    @NotEmpty(message = "Last name name does not empty")
    @Size(min = 1, max = 50, message = "Product name must be from 1 to 50")
    String last_name;

    @Enumerated(EnumType.STRING)
    ManagerStatus manager_status;

    @NotEmpty(message = "Last name name does not empty")
    @Size(min = 1, max = 255, message = "Product name must be from 1 to 255")
    String description;

    LocalDateTime created_at;

    //!!!!!!!!!! -  Necessarily  Constructor
//    public Manager(Long id, String first_name, String last_name, ManagerStatus manager_status, String description, LocalDateTime created_at) {
//        this.id = id;
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.manager_status = manager_status;
//        this.description = description;
//        this.created_at = created_at;
//    }
}




