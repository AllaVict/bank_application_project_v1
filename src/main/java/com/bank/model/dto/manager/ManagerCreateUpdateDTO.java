package com.bank.model.dto.manager;

import com.bank.model.enums.ManagerStatus;
import com.bank.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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


//    @NotEmpty(message = "manager_login does not empty")
//    @Size(min = 1, max = 50, message = "manager_login must be from 1 to 50")
//    String username;
//
//    @NotEmpty(message = "password does not empty")
//    @Size(min = 1, max = 100, message = "password must be from 1 to 10")
//    String password;
//
//    @Enumerated(EnumType.STRING)
//    Role role;

    LocalDateTime createdAt;

    public ManagerCreateUpdateDTO(String firstName, String lastName, ManagerStatus managerStatus, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerStatus = managerStatus;
        this.description = description;
    }
}

