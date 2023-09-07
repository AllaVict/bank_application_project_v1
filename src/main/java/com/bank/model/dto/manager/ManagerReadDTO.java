package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 @Value is the immutable variant of @Data ; all fields are made private and final by default,
 and setters are not generated. The class itself is also made final by default,
 because immutability is not something that can be forced onto a subclass.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ManagerReadDTO {
    Long id;
    String firstName;
    String lastName;
    ManagerStatus managerStatus;
    String description;
    LocalDateTime createdAt;


    public ManagerReadDTO(Long managerId) {
    }
}


