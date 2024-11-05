package dev.pabloabad.adventuretime.users;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.pabloabad.adventuretime.profiles.Profile;
import dev.pabloabad.adventuretime.roles.Role;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testConstructorWithUsernameAndPassword() {
        User user = new User("testUser", "testPassword");
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertNull(user.getProfile());
    }

    @Test
    void testConstructorWithUsernamePasswordAndProfile() {
        Profile profile = new Profile();
        User user = new User("testUser", "testPassword", profile);
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals(profile, user.getProfile());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());

        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());

        Profile profile = new Profile();
        user.setProfile(profile);
        assertEquals(profile, user.getProfile());

        Set<Role> roles = new HashSet<>();
        Role role = Mockito.mock(Role.class);
        roles.add(role);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testProfileRelationship() {
        Profile profile = Mockito.mock(Profile.class);
        User user = new User();
        user.setProfile(profile);
        assertEquals(profile, user.getProfile());
    }

    @Test
    void testAddRole() {
        User user = new User();
        Role role = Mockito.mock(Role.class);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        assertTrue(user.getRoles().contains(role));
    }
}
