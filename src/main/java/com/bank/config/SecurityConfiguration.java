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
             return NoOpPasswordEncoder.getInstance();
         // return new BCryptPasswordEncoder();
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

     /**
      //
      // https://stackoverflow.com/questions/75874011/using-two-or-more-securityfilterchains-with-spring-security-6-does-not-work-prop
      //  in    --  .authenticationManager(authProviderManager(http))
      @Bean
      public AuthenticationManager authProviderManager(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder authenticationManagerBuilder
      = http.getSharedObject(AuthenticationManagerBuilder.class);
      authenticationManagerBuilder.eraseCredentials(false)
      .userDetailsService(loginEntityService)
      // .passwordEncoder(passwordEncoder());
      .passwordEncoder(NoOpPasswordEncoder.getInstance());
      //        .inMemoryAuthentication()
      //                .withUser("admin")
      //                .password(passwordEncoder().encode("123"))
      //                //.password("123")
      //                .roles("ADMIN");
      // .authorities("ADMIN");

      return authenticationManagerBuilder.build();
      }


      */

     @Bean("securityFilterChain")
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          //  https://shzhangji.com/blog/2023/01/15/restful-api-authentication-with-spring-security/
          // .csrf().disable() !!!Spring Security, by default, enables CSRF protection for all non-idempotent requests, such as POST, DELETE, etc.
          // https://stackoverflow.com/questions/75874011/using-two-or-more-securityfilterchains-with-spring-security-6-does-not-work-prop
          //.authenticationManager(authProviderManager(http))
          http
                  .csrf().disable()
                  .authenticationProvider(daoAuthenticationProvider())
                  //.authenticationManager(authProviderManager(http))
                  .authorizeHttpRequests(authorize -> authorize
                          .requestMatchers("/home","/login","/logout","swagger-ui/**", "/login-app/**").permitAll()
                          // .requestMatchers(POST, "/v1/info", "/api/v1/client/**").hasRole("ADMIN")
                          // .requestMatchers("/admin**","/admin/**","/api/v1/admin/**").hasAuthority("ADMIN")
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

     /**
      @Autowired
      private LoginEntityService loginEntityService;
      @Bean
      public DaoAuthenticationProvider daoAuthenticationProvider() {
      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
      //daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
      daoAuthenticationProvider.setUserDetailsService(loginEntityService);
      return daoAuthenticationProvider;
      }
      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
      }

      */

     /**
      @Autowired
      private LoginEntityService loginEntityService;

      @Bean
      public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
      authenticationManagerBuilder.eraseCredentials(false)
      .userDetailsService(loginEntityService)
      //.passwordEncoder(passwordEncoder);
      .passwordEncoder(NoOpPasswordEncoder.getInstance());

      return authenticationManagerBuilder.build();
      }

      @Bean
      public static PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
      }
      */

     /**
      @Bean
      public UserDetailsService userDetailsService(){
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
      */


}