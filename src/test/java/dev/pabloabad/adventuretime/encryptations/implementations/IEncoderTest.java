package dev.pabloabad.adventuretime.encryptations.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.pabloabad.adventuretime.encryptations.Base64Encoder;
import dev.pabloabad.adventuretime.encryptations.BcryptEncoder;

@ExtendWith(MockitoExtension.class)
public class IEncoderTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testBase64Encode() {
        Base64Encoder encoder = new Base64Encoder();
        String result = encoder.encode("test");
        assertEquals("dGVzdA==", result);
    }

    @Test
    void testBase64Decode() {
        Base64Encoder encoder = new Base64Encoder();
        String result = encoder.decode("dGVzdA==");
        assertEquals("test", result);
    }

    @Test
    void testBcryptEncode() {
        String rawPassword = "password";
        String encodedPassword = "$2a$10$dXXXXXXXXXXXXXXXXXXXXX";
        
        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        
        BcryptEncoder encoder = new BcryptEncoder(bCryptPasswordEncoder);
        String result = encoder.encode(rawPassword);
        
        assertEquals(encodedPassword, result);
    }
}
