package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ClientAdminRestService;
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
@RequestMapping(value = "/api/v1/admin")
public class ClientRestAdminController {
    private final ClientAdminRestService clientAdminRestService;

    /**
     GET   http://localhost:8080/api/v1/admin/clients
     */
    @GetMapping("/clients")
    public ResponseEntity<FindAllClientsResponse> findAllClients(){
       FindAllClientsResponse response = clientAdminRestService.findAll();
        log.info("Reading all clients ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/admin/clients/3
     */
    @GetMapping("/clients/{id}")
    public ClientReadDTO findByIdClient(@PathVariable("id") Long id) {
        log.info("Reading client by id " +id);
        return clientAdminRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   http://localhost:8080/api/v1/admin/clients

     {
     "managerId": "3",
     "status": "CREATED",
     "taxCode": "3335556668",
     "firstName": "ClientTest1",
     "lastName": "ClientTestSon1",
     "email": "clienttest1@gmail.com",
     "address": "Wroclaw",
     "phone": "+48500600700"
     }

     */

    @PostMapping("/clients")
    public ResponseEntity<CreateUpdateClientResponse> createClient(@RequestBody ClientCreateUpdateDTO request){
        CreateUpdateClientResponse response  = clientAdminRestService.create(request);
        log.info("Creating client " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/admin/client/18
     {
     "id": "18",
     "managerId": "4",
     "status": "CHECKING",
     "taxCode": "3335556668",
     "firstName": "ClientTest1",
     "lastName": "ClientTestSon1",
     "email": "clienttest1@gmail.com",
     "address": "Wroclaw",
     "phone": "+48500600700"

     }
     "status": "ACTIVATED",
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<CreateUpdateClientResponse> updateClient(@PathVariable("id") Long id,
                                                                     @RequestBody ClientCreateUpdateDTO request){
        CreateUpdateClientResponse response = clientAdminRestService.update(request.getId(),request);
        log.info("Updating client " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/admin/client/{id}
     */
    @DeleteMapping("/clients/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteClientResponse> deleteClient(@PathVariable("id") Long id) {
        DeleteClientResponse response =  clientAdminRestService.delete(id);
        log.info("Deleting client " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





