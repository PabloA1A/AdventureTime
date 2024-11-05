package dev.pabloabad.adventuretime.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.pabloabad.adventuretime.dtos.RegisterDto;
import dev.pabloabad.adventuretime.users.User;

public class RegisterControllerTest {

    @Mock
    private RegisterService service;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setPassword("password123");

        User user = new User();
        user.setUsername("testuser");

        when(service.save(registerDto)).thenReturn(user);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Register successful");
        expectedResponse.put("username", "testuser");

        ResponseEntity<Map<String, String>> response = registerController.register(registerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}
