package com.bank.model.dto.client;


import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.enums.ClientStatus;
import com.bank.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;


/**
 @Value is the immutable variant of @Data ; all fields are made private and final by default,
 and setters are not generated. The class itself is also made final by default,
 because immutability is not something that can be forced onto a subclass.
 */

@Value
@AllArgsConstructor
public class ClientReadDTO {

    Long id;
    ManagerReadDTO manager;
    ClientStatus status;
    String taxCode;
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
//    String password;
//    Role role;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
