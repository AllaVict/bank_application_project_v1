package com.bank.http.restcontroller.admin;

import com.bank.core.restservice.admin.ManagerAdminRestService;
import com.bank.model.dto.manager.*;
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
public class ManagerRestAdminController {
    private final ManagerAdminRestService managerAdminRestService;

    /**
     GET   http://localhost:8080/api/v1/admin/managers
     */
    @GetMapping("/managers")
    public ResponseEntity<FindAllManagersResponse> findAllManagers(){
       FindAllManagersResponse response = managerAdminRestService.findAll();
        log.info("Reading all managers ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   http://localhost:8080/api/v1/admin/managers/3
     {
     "id": 3,
     "firstName": "Jessika",
     "lastName": "Parker",
     "managerStatus": "ACTIVE",
     "description": "description",
     "createdAt": "2023-07-09T00:00:00"
     }

     */
    @GetMapping("/managers/{id}")
    // public Manager findByIdManager(@PathVariable("id") Long id) {
    public ManagerReadDTO findByIdManager(@PathVariable("id") Long id) {
        log.info("Reading manager by id " +id);
        return managerAdminRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   http://localhost:8080/api/v1/admin/managers
     {
     "firstName": "Test1",
     "lastName": "TestSon1",
     "managerStatus": "MANAGER",
     "description": "description"
     }
     */

    @PostMapping("/managers")
    public ResponseEntity<CreateUpdateManagerResponse> createManager(@RequestBody ManagerCreateUpdateDTO request){
        CreateUpdateManagerResponse response  = managerAdminRestService.create(request);
        log.info("Creating manager " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   http://localhost:8080/api/v1/admin/managers/7
     {
     "id": "7",
     "firstName": "Test1",
     "lastName": "TestSon1",
     "managerStatus": "ACTIVE",
     "description": "USER"
     }
     */
    @PutMapping("/managers/{id}")
    public ResponseEntity<CreateUpdateManagerResponse> updateManager(@PathVariable("id") Long id,
                                                                     @RequestBody ManagerCreateUpdateDTO request){
        CreateUpdateManagerResponse response = managerAdminRestService.update(request.getId(),request);
        log.info("Updating manager " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   http://localhost:8080/api/v1/admin/managers/{id}
     */
    @DeleteMapping("/managers/{id}")
    public ResponseEntity<DeleteManagerResponse> deleteManager(@PathVariable("id") Long id) {
        DeleteManagerResponse response =  managerAdminRestService.delete(id);
        log.info("Deleting manager " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





