package com.bank.http.restcontroller;

import com.bank.core.restservice.BankAccountRestService;
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
@RequestMapping(value = "/api/v1")
public class BankAccountRestController {
    private final BankAccountRestService BankAccountRestService;

    /**
     GET   /api/v1/bankAccounts
     */
    @GetMapping("/bankAccounts")
    public ResponseEntity<FindAllBankAccountsResponse> findAllClients(@RequestBody String accessKey){
        FindAllBankAccountsResponse response = BankAccountRestService.findAll(accessKey);
        log.info("Reading all bankAccounts ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   /api/v1/bankAccounts/1
     {
     "id": 1,
     "client": {
     "id": 1,
     "manager": {
     "id": 2,
     },
     },
     "account_name": "current_per_PLN",
     "account_number": "11000022220000333300002222",
     "account_type": "PERSONAL_PLN",
     "status": "ACTIVATED",
     "balance": 750.00,
     "currency_code": "PLN",
     "created_at": "2023-07-11T00:00:00",
     "updated_at": null
     }


     */
    @GetMapping("/bankAccounts/{id}")
    public BankAccountReadDTO findByIdClient(@PathVariable("id") Long id) {
        log.info("Reading bankAccount by id " +id);
        return BankAccountRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/bankAccount
     {
     "client_id": "1",
     "account_name": "current_per_PLN",
     "account_number": "11000011110000444400001111",
     "status": "CREATED",
     "balance": 0.00,
     "currency_code": "PLN"
     }
     */

    @PostMapping("/bankAccounts/{id}")
    public ResponseEntity<CreateUpdateBankAccountResponse> createClient(@RequestBody BankAccountCreateUpdateDTO request){
        CreateUpdateBankAccountResponse response  = BankAccountRestService.create(request);
        log.info("Creating bankAccount " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   /api/v1/bankAccount/12
     {
     "id": 12,
     "client_id": ""
     "account_name": "current_bus_EUR",
     "account_number": "33000022220000333300002222",
     "account_type": "BUSINESS_EUR",
     "status": "CHECKING",
     "balance": 0,
     "currency_code": "EUR"
     }

     */
    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<CreateUpdateBankAccountResponse> updateClient(@PathVariable("id") Long id,
                                                                     @RequestBody BankAccountCreateUpdateDTO request){
        CreateUpdateBankAccountResponse response = BankAccountRestService.update(request.getId(),request);
        log.info("Updating bankAccount " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   /api/v1/bankAccount/{id}
     */
    @DeleteMapping("/bankAccounts/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteBankAccountResponse> deleteClient(@PathVariable("id") Long id) {
        DeleteBankAccountResponse response =  BankAccountRestService.delete(id);
        log.info("Deleting bankAccount " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





