package com.bank.core.util.manager;

import com.bank.core.util.Converter;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.dto.manager.ManagerReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerReadToUpdateConverter implements Converter<ManagerReadDTO, ManagerCreateUpdateDTO> {

    @Override
    public ManagerCreateUpdateDTO convert(ManagerReadDTO managerReadDTO) {
        return new ManagerCreateUpdateDTO(
                managerReadDTO.getId(),
                managerReadDTO.getFirst_name(),
                managerReadDTO.getLast_name(),
                managerReadDTO.getManager_status(),
                managerReadDTO.getDescription(),
                managerReadDTO.getCreated_at()

        );

    }


}
