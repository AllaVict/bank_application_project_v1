package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.TransactionAdminRestService;
import com.bank.model.dto.transaction.*;
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
public class TransactionRestAdminController {
    private final TransactionAdminRestService transactionAdminRestService;

    /**
     GET   http://localhost:8080/api/v1/admin/transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<FindAllTransactionsResponse> findAllTransactions(){
        FindAllTransactionsResponse response = transactionAdminRestService.findAll();
        log.info("Reading all transactions ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/admin/transactions/3
     */
    @GetMapping("/transactions/{id}")
    public TransactionReadDTO findByIdTransaction(@PathVariable("id") Long id) {
        log.info("Reading client by id " +id);
        return transactionAdminRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/admin/transactions
     "bankAccount": { "id": 1, "client": {  "id": 1,  "manager": {
     {
    "accountId": "3",
     "sender": "Garry Bred",
     "sourceAccount": "11000022220000333300002222",
     "beneficiary": "Kerry Truemen",
     "destinationAccount": "11000066660000666600006666",
     "transactionAmount": 1.00,
     "description": "payment for rent",
     "interestRate": 0.00
     }

     "transactionType": "INTERNAL",
     "transactionStatus": "DRAFT_INVALID"

     {
     "clientId": "1",
     "accountId": "1",
     "beneficiary": "Vens Disel",
     "destination_account": "11000066660000666600006666",
     "transaction_amount": 11.00,
     "description": "payment for rent"
     }

     */

    @PostMapping("/transactions")
    public ResponseEntity<CreateUpdateTransactionResponse> createTransaction(@RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionResponse response  = transactionAdminRestService.create(request);
        log.info("Creating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/admin/transactions/5
     {
     "id": "4",
     "accountId": "3",
     "sender": "Garry Bred",
     "sourceAccount": "11000022220000333300002222",
     "beneficiary": "Kerry Truemen",
     "destinationAccount": "11000066660000666600006666",
     "transactionAmount": 50.00,
     "description": "payment for rent",
     "interestRate": 0.00,
     }
     "transactionType": "INTERNAL",
     "transactionStatus": "DRAFT_INVALID"

     */
    @PutMapping("/transactions/{id}")
    public ResponseEntity<CreateUpdateTransactionResponse> updateTransaction(@PathVariable("id") Long id,
                                                                   @RequestBody TransactionCreateUpdateDTO request){

        //CreateUpdateTransactionResponse response = transactionRestService.update(id,request);
        CreateUpdateTransactionResponse response = transactionAdminRestService.update(request.getId(),request);
        log.info("Updating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/admin/transactions/{id}
     */
    @DeleteMapping("/transactions/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteTransactionResponse> deleteTransaction(@PathVariable("id") Long id) {
        DeleteTransactionResponse response =  transactionAdminRestService.delete(id);
        log.info("Deleting transaction " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}