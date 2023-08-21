package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCreateUpdateDTO {

    Long id;

    @NotEmpty(message = "First name does not empty")
    @Size(min = 1, max = 50, message = "First name must be from 1 to 50")
    String firstName;

    @NotEmpty(message = "Last name does not empty")
    @Size(min = 1, max = 50, message = "Last name must be from 1 to 50")
    String lastName;

    @Enumerated(EnumType.STRING)
    ManagerStatus managerStatus;

    @NotEmpty(message = "Description does not empty")
    @Size(min = 1, max = 255, message = "Description must be from 1 to 255")
    String description;


    LocalDateTime createdAt;


}

