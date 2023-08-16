package com.bank.model.dto.manager;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class FindAllManagersResponse {
    private List<ManagerReadDTO> managers;
    private List<CoreError> errors;
}
