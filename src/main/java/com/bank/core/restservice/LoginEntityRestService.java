package com.bank.core.restservice;

import com.bank.core.util.loginentity.LoginEntityReadConverter;
import com.bank.model.dto.loginentity.LoginEntityReadDTO;
import com.bank.repository.LoginEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginEntityRestService implements UserDetailsService {

    //@Autowired
    private LoginEntityRepository loginEntityRepository;

    private final LoginEntityReadConverter loginEntityReadConverter;

    @Autowired
    public LoginEntityRestService(LoginEntityRepository loginEntityRepository, LoginEntityReadConverter loginEntityReadConverter) {
        this.loginEntityRepository = loginEntityRepository;
        this.loginEntityReadConverter = loginEntityReadConverter;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return loginEntityRepository.findByUsername(username)
                .map(loginEntity -> new org.springframework.security.core.userdetails.User(
                        loginEntity.getUsername(),
                        loginEntity.getPassword(),
                        Collections.singleton(loginEntity.getRole())
                ))
                .orElseThrow(
                  () -> new UsernameNotFoundException("Failed to retrieve username: " + username));
    }

    public Optional<LoginEntityReadDTO> findByUsername(String username){
        String loginEntityUsername = this.loadUserByUsername(username).getUsername();
        return  Optional.of(loginEntityRepository.findByUsername(loginEntityUsername)
                .map(loginEntityReadConverter::convert).orElseThrow());

    }
    public Long getClientId(){
        return this.findByUsername(getUsername())
                .orElseThrow().getClient().getId();
    }
    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
