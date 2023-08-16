package com.bank.http.restcontroller;

import com.bank.core.restservice.TransactionRestService;
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
@RequestMapping(value = "/api/v1")
public class TransactionRestController {
    private final TransactionRestService transactionRestService;

    /**
     GET   /api/v1/transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<FindAllTransactionsResponse> findAllTransactions(@RequestBody String accessKey){
        FindAllTransactionsResponse response = transactionRestService.findAll(accessKey);
        log.info("Reading all transactions ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   /api/v1/transactions/3
     */
    @GetMapping("/transactions/{id}")
    public TransactionReadDTO findByIdTransaction(@PathVariable("id") Long id) {
        log.info("Reading client by id " +id);
        return transactionRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/transaction
     {
     "id": 1,
     "bankAccount": { "id": 1, "client": {  "id": 1,  "manager": {
     "product": { "id": 1, ...

     {
     "id": "4",
     "client_id": "3",
     "product_id": "1",
     "sender": "Garry Bred",
     "source_account": "11000022220000333300002222",
     "beneficiary": "Kerry Truemen",
     "destination_account": "11000066660000666600006666",
     "transaction_amount": 50.00,
     "description": "payment for rent",
     "interest_rate": 0.00,
     "transaction_type": "INTERNAL",
     "transaction_status": "DRAFT_INVALID"
     }

     */

    @PostMapping("/transactions")
    public ResponseEntity<CreateUpdateTransactionResponse> createTransaction(@RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionResponse response  = transactionRestService.create(request);
        log.info("Creating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   /api/v1/transactions/5
     {
     "id": "5",
     "client_id": "3",
     "product_id": "1",
     "sender": "Garry Bred",
     "source_account": "11000022220000333300002222",
     "beneficiary": "Kerry Truemen",
     "destination_account": "11000066660000666600006666",
     "transaction_amount": 50.00,
     "description": "payment for rent",
     "interest_rate": 0.00,
     "transaction_type": "INTERNAL",
     "transaction_status": "DRAFT_INVALID"
     }

     */
    @PutMapping("/transactions/{id}")
    public ResponseEntity<CreateUpdateTransactionResponse> updateTransaction(@PathVariable("id") Long id,
                                                                   @RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionResponse response = transactionRestService.update(request.getId(),request);
        log.info("Updating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   /api/v1/transactions/{id}
     */
    @DeleteMapping("/transactions/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteTransactionResponse> deleteTransaction(@PathVariable("id") Long id) {
        DeleteTransactionResponse response =  transactionRestService.delete(id);
        log.info("Deleting transaction " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}