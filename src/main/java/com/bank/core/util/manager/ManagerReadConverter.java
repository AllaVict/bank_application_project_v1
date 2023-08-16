package com.bank.core.util.manager;

import com.bank.core.util.Converter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.entity.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerReadConverter implements Converter<Manager, ManagerReadDTO> {

    @Override
    public ManagerReadDTO convert(Manager manager) {
        return new ManagerReadDTO(
                manager.getId(),
                manager.getFirst_name(),
                manager.getLast_name(),
                manager.getManager_status(),
                manager.getDescription(),
                manager.getCreated_at()
        );
    }


}
