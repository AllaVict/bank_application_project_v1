package com.bank.model.dto.loginentity;

import com.bank.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEntityResponse {
    String firstName;
    String lastName;
    Role role;


}
