package com.bank.config;

import com.bank.model.entity.LoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;

import static com.bank.model.enums.Role.*;

//@Configuration
//public class SecurityConfiguration {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/**","swagger-ui/index", "/index","/api/v1/login","/js/**", "/css/**").permitAll());
////                         .requestMatchers("/client**","/client/**","/api/v1/client/**").hasAuthority(CLIENT.getAuthority())
////                        .requestMatchers("/manager**", "/manager/**","/api/v1/manager/**").hasAnyAuthority(MANAGER.getAuthority(),ADMIN.getAuthority())
////                        .requestMatchers("/admin**","/admin/**","/api/v1/admin/**").hasAuthority(ADMIN.getAuthority())
////                        .anyRequest().authenticated());
////                .logout(logout -> logout
////                        .logoutUrl("/logout")
////                       .logoutSuccessUrl("/index")
////                        .deleteCookies("JSESSIONID"))
////                .formLogin(login -> login
////                        .loginPage("/login")//.loginPage("/api/auth/login")
////                       .defaultSuccessUrl("/client/home")
////                        .permitAll());
//
//        return http.build();
//
//    }
//
//
//}