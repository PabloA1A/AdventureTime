package dev.pabloabad.adventuretime.roles.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class RoleNotFoundExceptionTest {

    @Test
    void testRoleNotFoundException_WithMessage() {
        String errorMessage = "Role not found";
        RoleNotFoundException exception = new RoleNotFoundException(errorMessage);

        assertThat(exception).isInstanceOf(RoleNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void testRoleNotFoundException_WithMessageAndCause() {
        String errorMessage = "Role retrieval failed";
        Throwable cause = new RuntimeException("Database error");
        RoleNotFoundException exception = new RoleNotFoundException(errorMessage, cause);

        assertThat(exception).isInstanceOf(RoleNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testRoleNotFoundException_ThrownByCode() {
        assertThatThrownBy(() -> {
            throw new RoleNotFoundException("Role not found during operation");
        }).isInstanceOf(RoleNotFoundException.class)
          .hasMessage("Role not found during operation");
    }
}
