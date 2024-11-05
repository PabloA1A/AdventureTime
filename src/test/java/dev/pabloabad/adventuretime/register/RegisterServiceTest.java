package dev.pabloabad.adventuretime.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.pabloabad.adventuretime.dtos.RegisterDto;
import dev.pabloabad.adventuretime.facades.EncoderFacade;
import dev.pabloabad.adventuretime.profiles.Profile;
import dev.pabloabad.adventuretime.profiles.ProfileService;
import dev.pabloabad.adventuretime.roles.Role;
import dev.pabloabad.adventuretime.roles.RoleService;
import dev.pabloabad.adventuretime.users.User;
import dev.pabloabad.adventuretime.users.UserRepository;

public class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private ProfileService profileService;

    @Mock
    private EncoderFacade encoderFacade;

    @InjectMocks
    private RegisterService registerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        RegisterDto registerDto = new RegisterDto("testuser", "password", "testemail@example.com");
        String encodedPassword = "bcryptencodedpassword";
        Role defaultRole = new Role();
        defaultRole.setId(1L);

        when(encoderFacade.encode("bcrypt", registerDto.getPassword())).thenReturn(encodedPassword);
        when(roleService.getById(1L)).thenReturn(defaultRole);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setRoles(new HashSet<>(Set.of(defaultRole)));
            return savedUser;
        });
 
        User savedUser = registerService.save(registerDto);

        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals("testuser", savedUser.getUsername(), "Username should match");
        assertEquals(encodedPassword, savedUser.getPassword(), "Encoded password should match");
        assertNotNull(savedUser.getRoles(), "Roles set should not be null");
        assertTrue(savedUser.getRoles().contains(defaultRole), "User should have the default role");

        verify(encoderFacade).encode("bcrypt", registerDto.getPassword());
        verify(roleService).getById(1L);
        verify(userRepository).save(any(User.class));
        verify(profileService).save(any(Profile.class));
    }

    @Test
    public void testAssignDefaultRole() {
  
        Role defaultRole = new Role();
        defaultRole.setId(1L);

        when(roleService.getById(1L)).thenReturn(defaultRole);

       
        Set<Role> roles = registerService.assignDefaultRole();

    
        assertNotNull(roles, "Roles set should not be null");
        assertEquals(1, roles.size(), "Roles set should contain exactly one role");
        assertTrue(roles.contains(defaultRole), "Roles set should contain the default role");

        verify(roleService).getById(1L);
    }
}
