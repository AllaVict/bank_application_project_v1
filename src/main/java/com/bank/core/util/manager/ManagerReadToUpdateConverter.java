package com.bank.core.util.manager;

import com.bank.core.util.Converter;
import com.bank.model.dto.manager.ManagerReadDTO;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerReadToUpdateConverter implements Converter<ManagerReadDTO, ManagerCreateUpdateDTO> {

    @Override
    public ManagerCreateUpdateDTO convert(ManagerReadDTO managerReadDTO) {
        return new ManagerCreateUpdateDTO(
                managerReadDTO.getId(),
                managerReadDTO.getFirstName(),
                managerReadDTO.getLastName(),
                managerReadDTO.getManagerStatus(),
                managerReadDTO.getDescription(),
                managerReadDTO.getCreatedAt()

        );

    }


}
