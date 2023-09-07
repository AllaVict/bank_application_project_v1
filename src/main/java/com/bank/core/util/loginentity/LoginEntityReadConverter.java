package com.bank.core.util.loginentity;

import com.bank.core.util.Converter;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.core.util.product.ProductReadConverter;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.loginentity.LoginEntityReadDTO;
import com.bank.model.dto.transaction.TransactionReadDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.LoginEntity;
import com.bank.model.entity.Transaction;
import com.bank.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginEntityReadConverter implements Converter<LoginEntity, LoginEntityReadDTO> {

    private final ClientReadConverter clientReadConverter;

    @Override
    public LoginEntityReadDTO convert(LoginEntity loginEntity) {
            ClientReadDTO client = Optional.ofNullable(loginEntity.getClient())
                    .map(clientReadConverter::convert)
                    .orElse(null);

        return new LoginEntityReadDTO(
                loginEntity.getId(),
                loginEntity.getUsername(),
                loginEntity.getPassword(),
                loginEntity.getRole(),
                client,
                loginEntity.getCreatedAt(),
                loginEntity.getUpdatedAt()
           );

    }
}
