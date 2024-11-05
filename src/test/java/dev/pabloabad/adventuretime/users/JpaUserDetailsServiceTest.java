package dev.pabloabad.adventuretime.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        
        String username = "testuser";
        User user = new User(username, "password"); 
        @SuppressWarnings("unused")
        SecurityUser securityUser = new SecurityUser(user); 
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);

      
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserDoesNotExist() {
      
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

     
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername(username);
        });
        assertEquals("User not found " + username, thrown.getMessage());
    }
}
