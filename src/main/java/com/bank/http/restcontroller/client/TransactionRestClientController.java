package com.bank.http.restcontroller.client;

import com.bank.core.restservice.client.TransactionClientRestService;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
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
@RequestMapping(value = "/api/v1/client")
public class TransactionRestClientController {
    private final TransactionClientRestService transactionClientRestService;
    private final TransactionReadToUpdateConverter transactionReadToUpdateConverter;

    private final Long CLIENT_ID=1L;// = loginEntityService.getClientId();

    //  findByIdTransaction     --- /client/transaction/{id} --- client/transaction
    //  createTransaction       --- @PostMapping()           --- client/newTransaction
    //  updateTransaction       --- /client/transaction/{id} --- client/updateTransaction
    //  delete                  --- /client/transaction/delete/{id} --- client/updateTransaction

    /**
     GET   http://localhost:8080/api/v1/client/transactions/1
     */
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<FindAllTransactionsResponse> findAllTransactions(@PathVariable("accountId") Long id){
        FindAllTransactionsResponse response = transactionClientRestService.findAllByBankAccountId(id);
        log.info("Reading all bankAccount transactions ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/client/transaction/9
     */
    @GetMapping("/transaction/{id}")
    public TransactionReadDTO findByIdTransaction(@PathVariable("id") Long id) {
        log.info("Reading Transaction by id " +id);
        return transactionClientRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   http://localhost:8080/api/v1/client/transaction

     {
     "clientId": "1",
     "accountId": "1",
     "beneficiary": "Vens Disel",
     "destinationAccount": "11000066660000666600006666",
     "transactionAmount": 11.00,
     "description": "payment for rent"
     }
     */

    @PostMapping("/transaction")
    public ResponseEntity<CreateUpdateTransactionResponse> createTransaction(@RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionResponse response  = transactionClientRestService.create(request);
        log.info("Creating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/client/transaction/5
     {
     "id": "5",
     "clientId": "1",
     "accountId": "1",
     "beneficiary": "Vens Disel",
     "destinationAccount": "11000066660000666600006666",
     "transactionAmount": 22.00,
     "description": "payment for rent"
     }

     */
    @PutMapping("/transaction/{id}")
    public ResponseEntity<CreateUpdateTransactionResponse> updateTransaction(@PathVariable("id") Long id,
                                                                   @RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionResponse response = transactionClientRestService.update(request.getId(),request);
        log.info("Updating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/transaction/auth/{id}")
    public ResponseEntity<AuthorizeTransactionResponse> authorize(@PathVariable("id") Long id){
        TransactionCreateUpdateDTO transactionDTO = transactionClientRestService.findById(id)
                        .map(transactionReadToUpdateConverter::convert).orElseThrow();
        AuthorizeTransactionResponse response = transactionClientRestService.authorize(transactionDTO.getId());
        log.info("Authorizing transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/client/transaction/{id}
     */
    @DeleteMapping("/transaction/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteTransactionResponse> deleteTransaction(@PathVariable("id") Long id) {
        DeleteTransactionResponse response =  transactionClientRestService.delete(id);
        log.info("Deleting transaction " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}