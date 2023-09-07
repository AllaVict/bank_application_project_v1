package com.bank.core.restservice.admin;


import com.bank.core.util.client.ClientCreateUpdateConverter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.client.*;
import com.bank.model.entity.Client;
import com.bank.repository.ClientRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ClientAdminRestService {

    private final ClientRepository clientRepository;
    private final ClientReadConverter clientReadConverter;
    private final ClientCreateUpdateConverter clientCreateUpdateConverter;

    public FindAllClientsResponse findAll() {
        List<ClientReadDTO> allClients;
        if (!clientRepository.findAll().isEmpty()) {
            allClients = clientRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(client ->clientReadConverter.convert(client))
                    .map(clientReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllClientsResponse(allClients, new ArrayList<>());
    }

    public Optional<ClientReadDTO> findById(Long id) {
        return Optional.of(clientRepository.findById(id)
                .map(clientReadConverter::convert).orElseThrow());
    }

    public CreateUpdateClientResponse create(ClientCreateUpdateDTO clientCreateUpdateDTO) {
        clientCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        ClientReadDTO clientReadDTO= Optional.of(clientCreateUpdateDTO)
                .map(clientCreateUpdateConverter::convert)//ClientCreateConverter  ClientCreateDTO ->Client
                .map(clientRepository::save)   // .map(Client -> ClientRepository.save(Client))
                .map(clientReadConverter::convert) //ClientReadConverter  Client -> ClientReadDTO
                .orElseThrow();
        return new CreateUpdateClientResponse(clientReadDTO, new ArrayList<>());
    }

    public CreateUpdateClientResponse update(Long id, ClientCreateUpdateDTO clientCreateUpdateDTO) {
        Optional<Client> clientForUpdate = Optional.ofNullable(clientRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Client not found")));
        clientCreateUpdateDTO.setCreatedAt(clientForUpdate.get().getCreatedAt());
        clientCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());
        return new CreateUpdateClientResponse(
                clientForUpdate.map(client -> clientCreateUpdateConverter.convert(clientCreateUpdateDTO, client))
                        .map(clientRepository::saveAndFlush) // save clientCreateUpdateDTO
                        .map(clientReadConverter::convert).orElseThrow()  // client -> clientReadDTO
                , new ArrayList<>());
    }

    public DeleteClientResponse delete(Long request){
        Client clientForDelete =  clientRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Client not found"));
        clientRepository.delete(clientForDelete);
        return new DeleteClientResponse(clientReadConverter.convert(clientForDelete), new ArrayList<>());

    }

}
