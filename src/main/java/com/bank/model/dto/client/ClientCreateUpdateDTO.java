package com.bank.model.dto.client;

import com.bank.model.enums.ClientStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
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
public class ClientCreateUpdateDTO {

    Long id;
    Long manager_id;

    @Enumerated(EnumType.STRING)
    ClientStatus status;

    @NotEmpty(message = "Tax code does not empty")
    @Size(min = 1, max = 10, message = "Tax code must be from 1 to 10")
    String tax_code;

    @NotEmpty(message = "First name does not empty")
    @Size(min = 1, max = 50, message = "First name must be from 1 to 50")
    String first_name;

    @NotEmpty(message = "Last name does not empty")
    @Size(min = 1, max = 50, message = "Last name must be from 1 to 50")
    String last_name;

    @Email
    String email;

    @NotEmpty(message = "Address does not empty")
    String address;

    @NotEmpty(message = "Phone does not empty")
    String phone;

    LocalDateTime created_at;
    LocalDateTime updated_at;


}

