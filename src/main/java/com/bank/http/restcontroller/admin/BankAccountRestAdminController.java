package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.BankAccountAdminRestService;
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
@RequestMapping(value = "/api/v1/admin")
public class BankAccountRestAdminController {
    private final BankAccountAdminRestService BankAccountAdminRestService;

    /**
     GET   http://localhost:8080/api/v1/admin/bankAccounts
     */
    @GetMapping("/bankAccounts")
    public ResponseEntity<FindAllBankAccountsResponse> findAllBankAccounts(){//(@RequestBody String accessKey){
        FindAllBankAccountsResponse response = BankAccountAdminRestService.findAll();
        log.info("Reading all bankAccounts ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET    http://localhost:8080/api/v1/admin/bankAccounts/1
     {
     "id": 1,
     "client": { "id": 1,    "manager": {   "id": 2,     },     },
     "accountName": "current_per_PLN",
     "accountNumber": "11000022220000333300002222",
     "accountType": "PERSONAL_PLN",
     "status": "ACTIVATED",
     "balance": 750.00,
     "currencyCode": "PLN",
     "createdAt": "2023-07-11T00:00:00",
     "updatedAt": null
     }
     */
    @GetMapping("/bankAccounts/{id}")
    public BankAccountReadDTO findByIdBankAccount(@PathVariable("id") Long id) {
        log.info("Reading bankAccount by id " +id);
        return BankAccountAdminRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post    http://localhost:8080/api/v1/admin/bankAccounts

     {
     "clientId": "17",
     "productId": "1",
     "accountName": "test_per_PLN",
     "accountNumber": "11000011110000444400001111",
     "accountType": "PERSONAL_PLN",
     "status": "CREATED",
     "balance": 0.00,
     "currencyCode": "PLN"
     }
     */

    @PostMapping("/bankAccounts")
    public ResponseEntity<CreateUpdateBankAccountResponse> createBankAccount(@RequestBody BankAccountCreateUpdateDTO request){
        CreateUpdateBankAccountResponse response  = BankAccountAdminRestService.create(request);
        log.info("Creating bankAccount " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put    http://localhost:8080/api/v1/admin/bankAccount/22

     {
     "id": "22",
     "clientId": "17",
     "productId": "1",
     "accountName": "test_per_PLN",
     "accountNumber": "11000011110000444400001111",
     "accountType": "PERSONAL",
     "status": "CHECKING",
     "balance": 0.00,
     "currencyCode": "PLN"
     }
     "accountType": "BUSINESS",
     "status": "ACTIVATED",

     */
    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<CreateUpdateBankAccountResponse> updateBankAccount(@PathVariable("id") Long id,
                                                                     @RequestBody BankAccountCreateUpdateDTO request){
        CreateUpdateBankAccountResponse response = BankAccountAdminRestService.update(request.getId(),request);
        log.info("Updating bankAccount " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete    http://localhost:8080/api/v1/admin/bankAccount/{id}
     */
    @DeleteMapping("/bankAccounts/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteBankAccountResponse> deleteBankAccount(@PathVariable("id") Long id) {
        DeleteBankAccountResponse response =  BankAccountAdminRestService.delete(id);
        log.info("Deleting bankAccount " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





