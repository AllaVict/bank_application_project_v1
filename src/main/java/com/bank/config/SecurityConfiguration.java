package com.bank.config;

import com.bank.core.restservice.LoginEntityRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static com.bank.model.enums.Role.*;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
     @Autowired
     private LoginEntityRestService loginEntityService;

     @Bean
     public PasswordEncoder passwordEncoder() {

          return new BCryptPasswordEncoder();
     }
     @Bean
     public DaoAuthenticationProvider daoAuthenticationProvider() {
          DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
          daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
          daoAuthenticationProvider.setUserDetailsService(loginEntityService);
          return daoAuthenticationProvider;
     }
     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
          return authenticationConfiguration.getAuthenticationManager();
     }

     @Bean("securityFilterChain")
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

          http
                  .csrf().disable()
                  .authenticationProvider(daoAuthenticationProvider())
                   .authorizeHttpRequests(authorize -> authorize
                          .requestMatchers("/home","/login","/logout","swagger-ui/**", "/login-app/**").permitAll()
                          .requestMatchers("/api/v1/admin/**").hasAuthority(ADMIN.getAuthority())
                          .requestMatchers("/api/v1/client/**","/home-client").hasAuthority(CLIENT.getAuthority())
                          .requestMatchers("/api/v1/manager/**","/home-manager").hasAuthority(MANAGER.getAuthority())
                          .anyRequest().authenticated()
                  )
                  .exceptionHandling(customizer -> customizer
                          .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                  .httpBasic(withDefaults());
          return http.build();
     }




}