package dev.pabloabad.adventuretime.users;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testAllCanAccessPathRegister() throws Exception {
        mockMvc.perform(post("/api/v1/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/api/v1/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUserUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "pepe", roles = {"USER"})
    void testUserAuthenticatedWithRoleUser() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged"))
                .andExpect(jsonPath("$.username").value("pepe"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }

    @Test
    void testUserAuthenticatedWithCustomUser() throws Exception {
        mockMvc.perform(get("/api/v1/login").with(user("pepa")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged"))
                .andExpect(jsonPath("$.username").value("pepa"))
                .andExpect(jsonPath("$.roles").value("ROLE_ADMIN"));
    }
}
