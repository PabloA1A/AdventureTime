package dev.pabloabad.adventuretime.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import dev.pabloabad.adventuretime.roles.Role;

public class SecurityUserTest {

    private SecurityUser securityUser;
    private User user;

    @BeforeEach
    public void setUp() {
        user = mock(User.class);
        when(user.getPassword()).thenReturn("password123");
        when(user.getUsername()).thenReturn("username");

        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        when(user.getRoles()).thenReturn(roles);

        securityUser = new SecurityUser(user);
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", securityUser.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", securityUser.getUsername());
    }

    @Test
    public void testGetAuthorities() {
        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        when(user.getRoles()).thenReturn(roles);

        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        assertTrue(authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
        assertEquals(1, authorities.size());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(securityUser.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(securityUser.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(securityUser.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(securityUser.isEnabled());
    }
}
