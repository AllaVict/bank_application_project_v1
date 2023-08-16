package com.bank.core.util.client;

import com.bank.core.util.Converter;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientReadToUpdateConverter implements Converter<ClientReadDTO, ClientCreateUpdateDTO> {

    @Override
    public ClientCreateUpdateDTO convert(ClientReadDTO  clientReadDTO) {

        return new ClientCreateUpdateDTO(
                clientReadDTO.getId(),
                clientReadDTO.getManager().getId(),
                clientReadDTO.getStatus(),
                clientReadDTO.getTax_code(),
                clientReadDTO.getFirst_name(),
                clientReadDTO.getLast_name(),
                clientReadDTO.getEmail(),
                clientReadDTO.getAddress(),
                clientReadDTO.getPhone(),
                clientReadDTO.getCreated_at(),
                clientReadDTO.getUpdated_at()

        );

    }

}
