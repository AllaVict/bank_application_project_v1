package com.bank.model.dto.manager;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateUpdateManagerResponse {
    private ManagerReadDTO manager;
    private List<CoreError> errors;

}
