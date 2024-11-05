package dev.pabloabad.adventuretime.participants.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ParticipantExceptionTest {

    @Test
    void testExceptionWithMessage() {
        String errorMessage = "Test message";
        ParticipantException exception = new ParticipantException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithMessageAndCause() {
        String errorMessage = "Test message";
        Throwable cause = new RuntimeException("Cause of the exception");
        ParticipantException exception = new ParticipantException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
