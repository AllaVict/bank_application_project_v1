package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
import lombok.*;

import java.time.LocalDateTime;


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


