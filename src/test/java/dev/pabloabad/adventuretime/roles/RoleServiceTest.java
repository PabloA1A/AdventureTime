package dev.pabloabad.adventuretime.roles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.pabloabad.adventuretime.roles.exceptions.RoleNotFoundException;

public class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById_Success() {
     
        Long roleId = 1L;
        Role mockRole = new Role();
        mockRole.setId(roleId);
        when(repository.findById(roleId)).thenReturn(Optional.of(mockRole));

      
        Role result = roleService.getById(roleId);

    
        assertNotNull(result);
        assertEquals(roleId, result.getId());
        verify(repository, times(1)).findById(roleId);
    }

    @Test
    public void testGetById_RoleNotFound() {
       
        Long roleId = 1L;
        when(repository.findById(roleId)).thenReturn(Optional.empty());

    
        RoleNotFoundException thrown = assertThrows(RoleNotFoundException.class, () -> {
            roleService.getById(roleId);
        });
        assertEquals("Role not found", thrown.getMessage());
        verify(repository, times(1)).findById(roleId);
    }
}
