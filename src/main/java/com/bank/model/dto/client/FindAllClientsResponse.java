package com.bank.model.dto.client;

import com.bank.core.validation.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class FindAllClientsResponse {
    private List<ClientReadDTO> clients;
    private List<CoreError> errors;
}
