package com.bank.core.util.client;

import com.bank.core.util.Converter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ClientReadConverter implements Converter<Client, ClientReadDTO> {

    private final ManagerReadConverter managerReadConverter;

    @Override
    public ClientReadDTO convert(Client client) {
        ManagerReadDTO manager = Optional.ofNullable(client.getManager())
                .map(managerReadConverter::convert)
                .orElse(null);

        return new ClientReadDTO(
                client.getId(),
                manager,
                client.getStatus(),
                client.getTax_code(),
                client.getFirst_name(),
                client.getLast_name(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getCreated_at(),
                client.getUpdated_at()
        );
    }




}