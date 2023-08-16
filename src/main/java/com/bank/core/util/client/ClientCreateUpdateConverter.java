package com.bank.core.util.client;

import com.bank.core.util.Converter;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.entity.Client;
import com.bank.model.entity.Manager;
import com.bank.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ClientCreateUpdateConverter implements Converter<ClientCreateUpdateDTO, Client> {

    private final ManagerRepository managerRepository;


    @Override
    public Client convert(ClientCreateUpdateDTO fromClientCreateUpdateDTO, Client toClient) {
        copy(fromClientCreateUpdateDTO, toClient);
        return toClient;
    }

    @Override
    public Client convert(ClientCreateUpdateDTO clientCreateUpdateDTO) {
        Client client=new Client();
        copy(clientCreateUpdateDTO, client);
        return client;
    }
    private void copy(ClientCreateUpdateDTO object, Client client) {
        client.setId(object.getId());
        client.setManager(getManager(object.getManager_id()));
        client.setStatus(object.getStatus());
        client.setTax_code(object.getTax_code());
        client.setFirst_name(object.getFirst_name());
        client.setLast_name(object.getLast_name());
        client.setEmail(object.getEmail());
        client.setAddress(object.getAddress());
        client.setPhone(object.getPhone());
        client.setCreated_at(object.getCreated_at());
        client.setUpdated_at(object.getUpdated_at());
    }

    public Manager getManager(Long managerId) {
        return Optional.ofNullable(managerId)
                .flatMap(managerRepository::findById)
                .orElse(null);
    }
}
