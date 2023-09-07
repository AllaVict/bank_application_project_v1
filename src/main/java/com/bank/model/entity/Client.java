package com.bank.model.entity;

import com.bank.model.enums.ClientStatus;
import com.bank.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    Manager manager;

    @Enumerated(EnumType.STRING)
    ClientStatus status;

    @NotEmpty(message = "Tax code does not empty")
    @Size(min = 10, max = 10, message = "Tax code must be from 10 to 10")
    String taxCode;

    @NotEmpty(message = "First name does not empty")
    @Size(min = 1, max = 50, message = "First name must be from 1 to 50")
    String firstName;

    @NotEmpty(message = "Last name does not empty")
    @Size(min = 1, max = 50, message = "Last name must be from 1 to 50")
    String lastName;

    @Email
    String email;

    @NotEmpty(message = "Address does not empty")
    String address;

    @NotEmpty(message = "Phone does not empty")
    String phone;

//    @NotEmpty(message = "password does not empty")
//    @Size(min = 1, max = 100, message = "password must be from 1 to 10")
//    String password;
//
//    @Enumerated(EnumType.STRING)
//    Role role;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}


