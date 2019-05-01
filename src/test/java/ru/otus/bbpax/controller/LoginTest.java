package ru.otus.bbpax.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.bbpax.configuration.SecurityConfig;
import ru.otus.bbpax.controller.security.SecurityController;
import ru.otus.bbpax.entity.security.User;
import ru.otus.bbpax.service.AuthorService;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.bbpax.configuration.SecurityConfig.ENCODING_STRENGTH;
import static ru.otus.bbpax.entity.security.Roles.ADMIN;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
@ActiveProfiles("test")
class LoginTest {
    @Configuration
    @Import({
            AuthorController.class,
            SecurityConfig.class,
            SecurityController.class
    })
    static class Config {
        @Bean("customUserDetailsService")
        public UserDetailsService userDetailsService() {
            UserDetailsService mock = mock(UserDetailsService.class);
            User user = new User();
            user.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(ADMIN)));
            user.setUsername("admin");
            user.setPassword(new BCryptPasswordEncoder(ENCODING_STRENGTH).encode("pass"));
            doReturn(user).when(mock).loadUserByUsername(user.getUsername());
            return mock;
        }

    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService service;

    @Test
    void testLogin() throws Exception {
        mvc.perform(get("/author"))
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrl("http://localhost/login"))
                                .andDo(print())
                                .andReturn();

        mvc.perform(get("/login"))
                        .andExpect(status().isOk()).andReturn();

        //Bad credentials
        mvc.perform(post("/login")
                                .param("username","admin")
                                .param("password","admin"))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("/login?error=true"))
                        .andReturn();

        mvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username","admin")
                    .param("password","pass"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/welcome"))
                .andDo(print())
                .andReturn();

    }
}
