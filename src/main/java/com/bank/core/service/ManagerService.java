package com.bank.core.service;

import com.bank.core.util.manager.ManagerCreateUpdateConverter;
import com.bank.core.util.manager.ManagerReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Manager;
import com.bank.repository.ManagerRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerReadConverter managerReadConverter;
    private final ManagerCreateUpdateConverter managerCreateUpdateConverter;

    public List<ManagerReadDTO> findAll() {
        return managerRepository.findAll().stream()
                //<R> Stream<R> map(Function<? super T, ? extends R> mapper);
                //.map(manger ->converterReadManager.convert(manger))
                .map(managerReadConverter::convert)
                .toList();
    }

    public Optional<ManagerReadDTO> findById(Long id) {
        return managerRepository.findById(id)  // findById(ID id) return Optional<T>
                //<U> Optional<U> map(Function<? super T, ? extends U> mapper)
                // .map(manager -> managerReadConverter.convert(manager))
                .map(managerReadConverter::convert); // Manager -> ManagerReadDTO
    }


    // Optional.of Returns:an Optional with the value present Throws: NullPointerException – if value is null
    //public static <T> Optional<T> of(T value) {  return new Optional<>(Objects.requireNonNull(value))}
    // managerCreateConverter  managerCreateDTO ->Manager
    // managerReadConverter  Manager -> ManagerReadDTO
    @Transactional
    public ManagerReadDTO create(ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        managerCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        return Optional.of(managerCreateUpdateDTO)
                .map(managerCreateUpdateConverter::convert)
                //.map(manager -> manager.setCreated_at(LocalDateTime.now()) )
                // .map(manager -> managerRepository.save(manager))
                .map(managerRepository::save)
                .map(managerReadConverter::convert)
                .orElseThrow();
    }

    @Transactional
    public  Optional<ManagerReadDTO> update(Long id, ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        // managerRepository.findById(id)// findById Manager
        // .map(manager ->managerRepository.saveAndFlush(managerCreateEditDTO));
        Optional<Manager> managerForUpdate = Optional.ofNullable(managerRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Manager not found")));
        managerCreateUpdateDTO.setCreatedAt(managerForUpdate.get().getCreatedAt());
        return managerRepository.findById(id) // findById(ID id) return Optional<T>
         // convert(managerCreateUpdateDTO, manager) -- copy(fromManagerCreateUpdateDTO, toManager);
        .map(manager -> managerCreateUpdateConverter.convert(managerCreateUpdateDTO, manager))
        .map(managerRepository::saveAndFlush) // save Manager managerCreateDTO
        .map(managerReadConverter::convert);  // Manager -> ManagerReadDTO

    }



    @Transactional
    public boolean delete(Long id) {
        return managerRepository.findById(id)
                .map(manager -> {
                    managerRepository.delete(manager);
                    managerRepository.flush();
                    return true;
                })
                .orElse(false);
    }


}
