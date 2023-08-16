package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/** @Value is the immutable variant of @Data ;
 all fields are made private and final by default, and setters are not generated.
 */
//@Value
@Data
@AllArgsConstructor
public class ManagerCreateUpdateDTO {

    Long id;

    @NotEmpty(message = "First name name does not empty")
    @Size(min = 1, max = 50, message = "Product name must be from 1 to 50")
    String first_name;

    @NotEmpty(message = "Last name name does not empty")
    @Size(min = 1, max = 50, message = "Product name must be from 1 to 50")
    String last_name;

    ManagerStatus manager_status;

    @NotEmpty(message = "Last name name does not empty")
    @Size(min = 1, max = 255, message = "Product name must be from 1 to 255")
    String description;

    LocalDateTime created_at;


}

