package com.bank.http.restcontroller;

import com.bank.model.dto.loginentity.LoginEntityRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/auth")
public class LoginRestController {
    // @Autowired
   private AuthenticationManager authenticationManager;

    @Autowired
    public LoginRestController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

   @PostMapping("/login")
   public ResponseEntity<String> authenticateUser
   (@RequestBody LoginEntityRequest loginEntityRequest,
    BindingResult bindingResult,
    HttpServletRequest request ) throws Exception {


      if (bindingResult.hasErrors()) {
         throw new Exception("Invalid username or password");
      }
      try {
         Authentication authentication = authenticationManager
                 .authenticate(new UsernamePasswordAuthenticationToken(
                 loginEntityRequest.getUsername(), loginEntityRequest.getPassword()));
         log.info("You login successfully");
         SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (BadCredentialsException e) {
         throw new Exception("Invalid username or password");
      }
      return new ResponseEntity<>("You login successfully!.", HttpStatus.OK);
   }

   @PostMapping("/logout")
   public ResponseEntity<String>  logout(HttpServletRequest request) throws ServletException {
      request.logout();
      return new ResponseEntity<>("You logout successfully!.", HttpStatus.OK);
   }




   }

