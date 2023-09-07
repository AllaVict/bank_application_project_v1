package com.bank.core.util.client;

import com.bank.core.util.Converter;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
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
                clientReadDTO.getTaxCode(),
                clientReadDTO.getFirstName(),
                clientReadDTO.getLastName(),
                clientReadDTO.getEmail(),
                clientReadDTO.getAddress(),
                clientReadDTO.getPhone(),
//                clientReadDTO.getPassword(),
//                clientReadDTO.getRole(),
                clientReadDTO.getCreatedAt(),
                clientReadDTO.getUpdatedAt()

        );

    }

}
