package com.bank.model.dto.manager;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllManagersResponse {
    private List<ManagerReadDTO> managers;
    private List<CoreError> errors;

}
