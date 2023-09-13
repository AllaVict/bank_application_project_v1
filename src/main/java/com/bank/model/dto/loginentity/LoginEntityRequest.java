package com.bank.model.dto.loginentity;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEntityRequest {
    @NotBlank
    String username;
    @NotBlank
    String password;

}
