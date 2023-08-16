package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
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
public class ManagerReadDTO {
    Long id;
    String first_name;
    String last_name;
    ManagerStatus manager_status;
    String description;
    LocalDateTime created_at;

}


