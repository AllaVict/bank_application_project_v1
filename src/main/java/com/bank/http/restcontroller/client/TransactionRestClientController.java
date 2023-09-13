package com.bank.http.restcontroller.client;

import com.bank.core.restservice.client.TransactionClientRestService;
import com.bank.core.util.transaction.FindOneTransactionConverter;
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
@RequestMapping(value = "/api/v1/client/transacts")
public class TransactionRestClientController {
    private final TransactionClientRestService transactionClientRestService;
    private final TransactionReadToUpdateConverter transactionReadToUpdateConverter;
    private final FindOneTransactionConverter findOneTransactionConverter;

    //  findByIdTransaction     --- /client/transaction/{id} --- client/transaction
    //  createTransaction       --- @PostMapping()           --- client/newTransaction
    //  updateTransaction       --- /client/transaction/{id} --- client/updateTransaction
    //  delete                  --- /client/transaction/delete/{id} --- client/updateTransaction

    /**
     GET   http://localhost:8080/api/v1/client/transacts/8
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<FindAllTransactionsForClientResponse> findAllTransactions(@PathVariable("accountId") Long id){
        FindAllTransactionsForClientResponse response
                = transactionClientRestService.findAllByBankAccountId(id);
        log.info("Reading all bankAccount transactions ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/client/transacts/transact/1
     */
    @GetMapping("/transact/{id}")
    public FindTransactionForClient findByIdTransaction(@PathVariable("id") Long id) {
        log.info("Reading Transaction by id " +id);
        return transactionClientRestService.findByIdAndClientId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   http://localhost:8080/api/v1/client/transacts/transact

     {
     "clientId": "3",
     "accountId": "8",
     "beneficiary": "Vens Disel",
     "destinationAccount": "11000066660000666600006666",
     "transactionAmount": 11.00,
     "description": "payment salary"
     }
     */

    @PostMapping("/transact")
    public ResponseEntity<CreateUpdateTransactionForClientResponse> createTransaction(@RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionForClientResponse response  = transactionClientRestService.create(request);
        log.info("Creating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/client/transacts/transact/5
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
    @PutMapping("/transact/{id}")
    public ResponseEntity<CreateUpdateTransactionForClientResponse> updateTransaction(@PathVariable("id") Long id,
                                                                   @RequestBody TransactionCreateUpdateDTO request){
        CreateUpdateTransactionForClientResponse response
                = transactionClientRestService.update(request.getId(),request);
        log.info("Updating transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**      Put   http://localhost:8080/api/v1/client/transacts/transact/auth/5
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

    @PutMapping("/transact/auth/{id}")
    public ResponseEntity<AuthorizeTransactionResponse> authorize(@PathVariable("id") Long id){
        FindTransactionForClient transactionDTO = transactionClientRestService.findByIdAndClientId(id)
                        .orElseThrow();
        AuthorizeTransactionResponse response = transactionClientRestService.authorize(transactionDTO.getId());
        log.info("Authorizing transaction " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/client/transacts/transact/{id}
     */
    @DeleteMapping("/transact/{id}")
    public ResponseEntity<DeleteTransactionForClientResponse> deleteTransaction(@PathVariable("id") Long id) {
        DeleteTransactionForClientResponse response =  transactionClientRestService.delete(id);
        log.info("Deleting transaction " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}