package dev.pabloabad.adventuretime.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RegisterDtoTest {

    @Test
    public void testDefaultConstructor() {
        RegisterDto dto = new RegisterDto();
        assertEquals(null, dto.getUsername());
        assertEquals(null, dto.getPassword());
        assertEquals(null, dto.getEmail());
    }

    @Test
    public void testParameterizedConstructor() {
        RegisterDto dto = new RegisterDto("testuser", "password123", "test@example.com");
        assertEquals("testuser", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    public void testSetUsername() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("newuser");
        assertEquals("newuser", dto.getUsername());
    }

    @Test
    public void testSetPassword() {
        RegisterDto dto = new RegisterDto();
        dto.setPassword("newpassword");
        assertEquals("newpassword", dto.getPassword());
    }

    @Test
    public void testSetEmail() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", dto.getEmail());
    }
}
