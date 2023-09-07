package com.bank.model.dto.loginentity;

import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginEntityReadDTO {

    Long id;
    String username;
    String password;
    Role role;
    ClientReadDTO client;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public LoginEntityReadDTO(String username) {
        this.username = username;
    }
}

