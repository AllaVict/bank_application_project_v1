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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotEmpty(message = "username does not empty")
    @Size(min = 1, max = 50, message = "username must be from 1 to 50")
    String username;
    @NotEmpty(message = "password does not empty")
    @Size(min = 1, max = 100, message = "password must be from 1 to 10")
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    Client client;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}

