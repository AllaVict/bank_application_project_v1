package com.bank.core.util.manager;

import com.bank.core.util.Converter;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.entity.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ManagerCreateUpdateConverter implements Converter<ManagerCreateUpdateDTO, Manager> {

    @Override
    public Manager convert(ManagerCreateUpdateDTO fromManagerCreateUpdateDTO, Manager toManager) {
        copy(fromManagerCreateUpdateDTO, toManager);
        return toManager;
    }

    @Override
    public Manager convert(ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        Manager manager=new Manager();
        copy(managerCreateUpdateDTO, manager);
        return manager;
    }

    private void copy(ManagerCreateUpdateDTO object, Manager manager) {
        manager.setId(object.getId());
        manager.setFirst_name(object.getFirst_name());
        manager.setLast_name(object.getLast_name());
        manager.setManager_status(object.getManager_status());
        manager.setDescription(object.getDescription());
        manager.setCreated_at(object.getCreated_at());

    }

}
