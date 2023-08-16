package com.bank.http.restcontroller;

import com.bank.core.restservice.ManagerRestService;
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
@RequestMapping(value = "/api/v1")
public class ManagerRestController {
    private final ManagerRestService managerRestService;

    /**
     GET   /api/v1/managers
     */
    @GetMapping("/managers")
    public ResponseEntity<FindAllManagersResponse> findAllManagers(@RequestBody String accessKey){
       FindAllManagersResponse response = managerRestService.findAll(accessKey);
        log.info("Reading all managers ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET   /api/v1/managers/3
     */
    @GetMapping("/managers/{id}")
    public ManagerReadDTO findByIdManager(@PathVariable("id") Long id) {
        log.info("Reading manager by id " +id);
        return managerRestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    /**
     Post   /api/v1/managers
     {
     "first_name": "Test1",
     "last_name": "TestSon1",
     "manager_status": "ACTIVE",
     "description": "USER"
     }
     */

    @PostMapping
    public ResponseEntity<CreateUpdateManagerResponse> createManager(@RequestBody ManagerCreateUpdateDTO request){
        CreateUpdateManagerResponse response  = managerRestService.create(request);
        log.info("Creating manager " +response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**      Put   /api/v1/managers/58
     {
     "id": "7",
     "first_name": "Test1",
     "last_name": "TestSon1",
     "manager_status": "ACTIVE",
     "description": "USER"
     }
     */
    @PutMapping("/managers/{id}")
    public ResponseEntity<CreateUpdateManagerResponse> updateManager(@PathVariable("id") Long id,
                                                                     @RequestBody ManagerCreateUpdateDTO request){
        CreateUpdateManagerResponse response = managerRestService.update(request.getId(),request);
        log.info("Updating manager " +response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     Delete   /api/v1/managers/{id}
     */
    @DeleteMapping("/managers/{id}")
    //public void deleteManager(@RequestBody DeleteManagerRequest request) {
    public ResponseEntity<DeleteManagerResponse> deleteManager(@PathVariable("id") Long id) {
        DeleteManagerResponse response =  managerRestService.delete(id);
        log.info("Deleting manager " +response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}





