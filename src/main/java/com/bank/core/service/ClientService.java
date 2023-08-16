package com.bank.core.service;

import com.bank.core.util.client.ClientCreateUpdateConverter;
import com.bank.core.util.client.ClientReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.dto.client.ClientReadDTO;
import com.bank.model.entity.Client;
import com.bank.repository.ClientRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Data
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientReadConverter clientReadConverter;
    private final ClientCreateUpdateConverter clientCreateUpdateConverter;

    public List<ClientReadDTO> findAll() {
        return clientRepository.findAll().stream()
                //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                //.map(manger ->converterReadManager.convert(manger))
                .map(clientReadConverter::convert)
                .toList();
    }

    public Optional<ClientReadDTO> findById(Long id) {
        return  clientRepository.findById(id)// findById(ID id) return Optional<T>
                //<U> Optional<U> map(Function<? super T, ? extends U> mapper)
                // .map(client -> clientReadConverter.convert(client))
                .map(clientReadConverter::convert); // client -> clientReadDTO
    }

    // Optional.of Returns:an Optional with the value present Throws: NullPointerException – if value is null
    //public static <T> Optional<T> of(T value) {  return new Optional<>(Objects.requireNonNull(value))}
    // clientCreateConverter  clientCreateDTO ->client
    // clientReadConverter  client -> clientReadDTO
    @Transactional
    public ClientReadDTO create(ClientCreateUpdateDTO clientCreateUpdateDTO) {
        //clientRepository.save(clientCreateEditDTO);
        return Optional.of(clientCreateUpdateDTO)
                .map(clientCreateUpdateConverter::convert)
                // .map(client -> clientRepository.save(client))
                .map(clientRepository::save)
                .map(clientReadConverter::convert)
                .orElseThrow();
    }

    @Transactional
    public  Optional<ClientReadDTO> update(Long id, ClientCreateUpdateDTO clientCreateUpdateDTO) {
        // managerRepository.findById(id)// findById Client
        // .map(Client ->ClientRepository.saveAndFlush(ClientCreateEditDTO));
        Optional<Client> clientForUpdate = Optional.ofNullable(clientRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Client not found")));
        clientCreateUpdateDTO.setCreated_at(clientForUpdate.get().getCreated_at());
        return clientRepository.findById(id) // findById(ID id) return Optional<T>
                // convert(clientCreateUpdateDTO, client) -- copy(fromClientCreateUpdateDTO, toClient);
                .map(client -> clientCreateUpdateConverter.convert(clientCreateUpdateDTO, client))
                .map(clientRepository::saveAndFlush) // save Client clientCreateUpdateDTO
                .map(clientReadConverter::convert);  // Client -> ClientReadDTO

    }

    @Transactional
    public boolean delete(Long id) {
        return clientRepository.findById(id)
                .map(manager -> {
                    clientRepository.delete(manager);
                    clientRepository.flush();
                    return true;
                })
                .orElse(false);
    }



}

