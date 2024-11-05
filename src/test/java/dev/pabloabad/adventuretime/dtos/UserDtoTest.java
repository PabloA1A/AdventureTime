package dev.pabloabad.adventuretime.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserDtoTest {

   @Test
    void testGetterAndSetters() {
        UserDto userDto = new UserDto();
        String username = "testUser";
        String password = "testPass";

        userDto.setUsername(username);
        userDto.setPassword(password);

        assertEquals(username, userDto.getUsername());
        assertEquals(password, userDto.getPassword());
    }

    @Test
    void testConstructor() {
        String username = "testUser";
        String password = "testPass";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);

        assertEquals(username, userDto.getUsername());
        assertEquals(password, userDto.getPassword());
    }
}
