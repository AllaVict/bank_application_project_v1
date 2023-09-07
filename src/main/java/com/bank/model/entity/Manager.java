package com.bank.model.entity;

import com.bank.model.enums.ManagerStatus;
import com.bank.model.enums.Role;
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

    public Manager(String firstName, String lastName, ManagerStatus managerStatus, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerStatus = managerStatus;
        this.description = description;
    }


}





