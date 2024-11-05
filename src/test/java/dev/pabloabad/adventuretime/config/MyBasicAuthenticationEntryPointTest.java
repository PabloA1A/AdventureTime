package dev.pabloabad.adventuretime.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyBasicAuthenticationEntryPointTest {

    private MyBasicAuthenticationEntryPoint entryPoint;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        entryPoint = new MyBasicAuthenticationEntryPoint();
        entryPoint.afterPropertiesSet();
    }

    @Test
    public void testCommence() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);
    
        when(authException.getMessage()).thenReturn("Unauthorized");
    
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        printWriter.flush(); 
        assertEquals("HTTP Status 401 - Unauthorized\n", stringWriter.toString());
    }

    @Test
    public void testAfterPropertiesSet() {
        assertEquals("Spring Digital Academy", entryPoint.getRealmName());
    }
}
