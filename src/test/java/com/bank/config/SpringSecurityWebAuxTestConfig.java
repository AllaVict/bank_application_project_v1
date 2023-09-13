package com.bank.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


//https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean("test")
    public static PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
    }


    @Bean("testUserDet")
    @Primary
    public UserDetailsService userDetailsService() {
        UserDetails client = User.builder()
                .username("client")
                .password(passwordEncoder().encode("client"))
                .roles("CLIENT")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities("ADMIN")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(client, admin);
    }


}