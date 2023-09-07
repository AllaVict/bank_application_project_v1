package com.bank.http.restcontroller.client;

import com.bank.core.restservice.client.BankAccountClientRestService;
import com.bank.model.dto.bankaccount.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/client")
public class BankAccountRestClientController {

    private final BankAccountClientRestService bankAccountClientRestService;

    private final Long CLIENT_ID=3L;// = loginEntityService.getClientId();
    /**
     GET  http://localhost:8080/api/v1/client/bankAccounts
     */

    @GetMapping("/bankAccounts")
    public ResponseEntity<FindAllBankAccountsResponse> findAllBankAccountByClientId(){
        FindAllBankAccountsResponse response =
                bankAccountClientRestService.findAllBankAccountByClientId(CLIENT_ID);
        log.info("Reading all client`s bankAccounts ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     GET   http://localhost:8080/api/v1/client/bankAccounts/1
     */
    @GetMapping("/bankAccounts/{id}")
    public BankAccountReadDTO findBankAccountById(@PathVariable("id") Long id) {
        log.info("Reading client`s bankAccount by id " +id);
        return bankAccountClientRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}





