package dev.pabloabad.adventuretime.events.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventNotFoundExceptionTest {

    @Test
    void testEventNotFoundExceptionWithMessage() {
        String message = "Event not found";
        EventNotFoundException exception = new EventNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testEventNotFoundExceptionWithMessageAndCause() {
        String message = "Event not found";
        Throwable cause = new RuntimeException("Root cause");
        EventNotFoundException exception = new EventNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());

        assertEquals(cause, exception.getCause());
    }
}
