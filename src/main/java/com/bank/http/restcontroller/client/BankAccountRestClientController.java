package com.bank.http.restcontroller.client;

import com.bank.core.restservice.client.BankAccountClientRestService;
import com.bank.model.dto.bankaccount.FindAllBankAccountsForClientResponse;
import com.bank.model.dto.bankaccount.FindBankAccountForClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/client/bankAccounts")
public class BankAccountRestClientController {

    private final BankAccountClientRestService bankAccountClientRestService;

    /**
     GET  http://localhost:8080/api/v1/client/bankAccounts
     */
    @GetMapping()
    public ResponseEntity<FindAllBankAccountsForClientResponse> findAllBankAccountByClientId(){
        FindAllBankAccountsForClientResponse response =
                bankAccountClientRestService.findAllBankAccountByClientId();
        log.info("Reading all client`s bankAccounts ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     GET   http://localhost:8080/api/v1/client/bankAccounts/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<FindBankAccountForClient> findByIdAndClientId(@PathVariable("id") Long id) {
        log.info("Reading client`s bankAccount by id " +id);
        FindBankAccountForClient response
                = bankAccountClientRestService.findByIdAndClientId(id)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }



}





