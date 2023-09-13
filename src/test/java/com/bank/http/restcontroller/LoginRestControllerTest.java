package com.bank.http.restcontroller;

import com.bank.config.SpringSecurityWebAuxTestConfig;
import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.util.loginentity.LoginEntityReadConverter;
import com.bank.repository.LoginEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc//(addFilters = true)
@ExtendWith(MockitoExtension.class)
class LoginRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginEntityRepository loginEntityRepository;
    @MockBean
    private LoginEntityReadConverter loginEntityReadConverter;
    @MockBean
    private LoginEntityRestService loginEntityRestService;
//    @Autowired
//    protected WebApplicationContext wac;
//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;
//
//    @BeforeEach
//    public void applySecurity() {
//        this.mockMvc = webAppContextSetup(wac)
//                .apply(springSecurity(springSecurityFilterChain))
//                .build();
//    }

    @Test
    @WithUserDetails("client")
    void loginController_shouldUnAuthenticateUser() throws Exception   {
        String request = "{\"username\":\"client\",\"password\":\"client\"}";
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                           .accept(MediaType.APPLICATION_JSON)

                      )
           .andExpect(status().isUnauthorized());
    }


}

