package com.bank.http.restcontroller;

import com.bank.core.restservice.ClientRestService;
import com.bank.model.dto.client.*;
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
public class ClientRestController {
    private final ClientRestService clientRestService;

    /**
     GET   /api/v1/clients
     */
    @GetMapping("/clients")
    public ResponseEntity<FindAllClientsResponse> findAllClients(@RequestBody String accessKey){
       FindAllClientsResponse response = clientRestService.findAll(accessKey);
        log.info("Reading all clients ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   /api/v1/clients/3
     */
    @GetMapping("/clients/{id}")
    public ClientReadDTO findByIdClient(@PathVariable("id") Long id) {
        log.info("Reading client by id " +id);
        return clientRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/clients
     {
     "manager_id": "3",
     "status": "INVALID",
     "tax_code": "33355566688",
     "first_name": "ClientTest1",
     "last_name": "ClientSon1",
     "email": "clienttest1@gmail.com",
     "address": "Wroclaw",
     "phone": "+48500600700"
     }

     */

    @PostMapping("/clients")
    public ResponseEntity<CreateUpdateClientResponse> createClient(@RequestBody ClientCreateUpdateDTO request){
        CreateUpdateClientResponse response  = clientRestService.create(request);
        log.info("Creating client " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   /api/v1/client/12
     {
     "id": "12",
     "manager_id": "3",
     "status": "VALID",
     "tax_code": "33355566688",
     "first_name": "ClientTest1",
     "last_name": "ClientSon1",
     "email": "clienttest1@gmail.com",
     "address": "Wroclaw",
     "phone": "+48500600700"
     }
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<CreateUpdateClientResponse> updateClient(@PathVariable("id") Long id,
                                                                     @RequestBody ClientCreateUpdateDTO request){
        CreateUpdateClientResponse response = clientRestService.update(request.getId(),request);
        log.info("Updating client " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   /api/v1/client/{id}
     */
    @DeleteMapping("/clients/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteClientResponse> deleteClient(@PathVariable("id") Long id) {
        DeleteClientResponse response =  clientRestService.delete(id);
        log.info("Deleting client " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





