package com.bank.core.restservice;

import com.bank.core.util.loginentity.LoginEntityReadConverter;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.dto.bankaccount.CreateUpdateBankAccountResponse;
import com.bank.model.dto.loginentity.LoginEntityReadDTO;
import com.bank.model.dto.loginentity.LoginEntityRequest;
import com.bank.model.dto.loginentity.LoginEntityResponse;
import com.bank.model.entity.BankAccount;
import com.bank.repository.LoginEntityRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginEntityRestService {// implements UserDetailsService {

    @Autowired
    private LoginEntityRepository loginEntityRepository;

    private final LoginEntityReadConverter loginEntityReadConverter;

//    @GetMapping("/login")
//    public ResponseEntity<LoginEntityResponse> loginPage(@RequestBody LoginEntityRequest loginEntityRequest) {
//        LoginEntityResponse response = loginEntityRestService.login(loginEntityRequest);
//        log.info("You login successfully");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//    public LoginEntityResponse login(LoginEntityRequest loginEntityRequest){
//        String username = loginEntityRequest.getUsername();
//        String loginEntityUsername = this.loadUserByUsername(username).getUsername();
//        Optional<LoginEntityReadDTO> loginEntityReadDTO =Optional.of(loginEntityRepository.findByUsername(loginEntityUsername)
//                .map(loginEntityReadConverter::convert).orElseThrow());
//
//        System.out.println("!!!!!!!!!!!!!!!!!!! with role: "+loginEntityReadDTO.get().getRole());
//        log.info("You login successfully with role: "+loginEntityReadDTO.get().getRole());
//        return new LoginEntityResponse(
//                loginEntityReadDTO.get().getClient().getFirstName(),
//                loginEntityReadDTO.get().getClient().getLastName(),
//                loginEntityReadDTO.get().getRole()
//        );
//
//    }
//
//    public Optional<LoginEntityReadDTO> findByUsername(String username){
//        String loginEntityUsername = this.loadUserByUsername(username).getUsername();
//        return  Optional.of(loginEntityRepository.findByUsername(loginEntityUsername)
//                .map(loginEntityReadConverter::convert).orElseThrow());
//
//    }
//
//    public Long getClientId(){
//        return this.findByUsername(getUsername())
//                .orElseThrow().getClient().getId();
//    }
//
//    public String getUsername(){
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return loginEntityRepository.findByUsername(username)
//                .map(loginEntity -> new org.springframework.security.core.userdetails.User(
//                        loginEntity.getUsername(),
//                        loginEntity.getPassword(),
//                        Collections.singleton(loginEntity.getRole())
//                ))
//                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve username: " + username));
//    }
}
