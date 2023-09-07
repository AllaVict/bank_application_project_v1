package com.bank.http.restcontroller;

import com.bank.core.restservice.LoginEntityRestService;
import com.bank.model.dto.loginentity.LoginEntityRequest;
import com.bank.model.dto.loginentity.LoginEntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/auth")
public class LoginRestController {
//    @Autowired
//    private AuthenticationManager authenticationManager;

    private final LoginEntityRestService loginEntityRestService;

    /**
     https://bushansirgur.in/spring-security-login-rest-api-with-mysql-database/

     !!!!!!!!!!!!!!!!!!!!!! yне работает
     https://shzhangji.com/blog/2023/01/15/restful-api-authentication-with-spring-security/
     https://github.com/jizhang/blog-demo/tree/master/api-auth
     -----------------------------------------------------------------
     https://springjavatutorial.medium.com/login-and-registration-rest-api-with-spring-security-d7ee48820bd0
      https://www.tutussfunny.com/login-and-registration-rest-api-using-spring-boot-mysql/?expand_article=1
     -----------------------------------------------------------------
     https://www.bezkoder.com/spring-boot-login-example-mysql/

     http://localhost:8080/api/auth/login
     */

    /**
     ADMIN
     {
     "username": "robbywolf",
     "password":"123"
     }
     CLIENT
     {
     "username": "alinasmith",
     "password":"123"
     }
     MANAGER
     {
     "username": "jakhenry",
     "password":"123"
     }
     */
//    @PostMapping("/login")
//    public ResponseEntity<LoginEntityResponse> loginPage(@RequestBody LoginEntityRequest loginEntityRequest) {
//        LoginEntityResponse response = loginEntityRestService.login(loginEntityRequest);
//        log.info("You login successfully");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }



  //  @PostMapping("/login")
 //       public ResponseEntity<String> authenticateUser(@RequestBody LoginEntityRequest loginEntityRequest) throws Exception {

      // https://bushansirgur.in/spring-security-login-rest-api-with-mysql-database/
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authObject);
//        } catch (BadCredentialsException e) {
//            throw new Exception("Invalid credentials");
//        }

//        try {
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(
//                            loginEntityRequest.getUsername(), loginEntityRequest.getPassword()));
//            log.info("You login successfully");
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (BadCredentialsException e) {
//            throw new Exception("Invalid credentials");
//        }
//            return new ResponseEntity<>("You login successfully!.", HttpStatus.OK);
//        }

   }

