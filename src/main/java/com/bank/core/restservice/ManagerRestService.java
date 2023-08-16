package com.bank.core.restservice;


import com.bank.core.util.manager.ManagerCreateUpdateConverter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.manager.*;
import com.bank.model.entity.Manager;
import com.bank.repository.ManagerRepository;
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
public class ManagerRestService {

    private final ManagerRepository managerRepository;
    private final ManagerReadConverter managerReadConverter;
    private final ManagerCreateUpdateConverter managerCreateUpdateConverter;

    public FindAllManagersResponse findAll(String accessKey) {
        List<ManagerReadDTO> allManagers;
        if (!managerRepository.findAll().isEmpty()) {
           allManagers = managerRepository.findAll().stream()
                    //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                    //.map(manager ->managerReadConverter.convert(manager))
                    .map(managerReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is ADMIN`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllManagersResponse(allManagers, new ArrayList<>());
    }

    public Optional<ManagerReadDTO> findById(Long id) {
        return Optional.of(managerRepository.findById(id)
                .map(managerReadConverter::convert).orElseThrow());
    }

    public CreateUpdateManagerResponse create(ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        managerCreateUpdateDTO.setCreated_at(LocalDateTime.now());
        ManagerReadDTO managerReadDTO= Optional.of(managerCreateUpdateDTO)
                .map(managerCreateUpdateConverter::convert)//productCreateConverter  managerCreateDTO ->Manager
                .map(managerRepository::save)   // .map(product -> productRepository.save(product))
                .map(managerReadConverter::convert) //productReadConverter  Product -> ProductReadDTO
                .orElseThrow();
        return new CreateUpdateManagerResponse(managerReadDTO, new ArrayList<>());
    }

    public CreateUpdateManagerResponse update(Long id, ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        Optional<Manager> managerForUpdate = Optional.ofNullable(managerRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Manager not found")));
        managerCreateUpdateDTO.setCreated_at(managerForUpdate.get().getCreated_at());
        return new CreateUpdateManagerResponse(
                managerForUpdate.map(manager -> managerCreateUpdateConverter.convert(managerCreateUpdateDTO, manager))
                        .map(managerRepository::saveAndFlush) // save managerCreateUpdateDTO
                        .map(managerReadConverter::convert).orElseThrow()  // ProduManagert -> managerReadDTO
                , new ArrayList<>());
    }

    public DeleteManagerResponse delete(Long request){
        Manager managerforDelete =  managerRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Manager not found"));
        managerRepository.delete(managerforDelete);
        return new DeleteManagerResponse(managerReadConverter.convert(managerforDelete), new ArrayList<>());

    }

}
