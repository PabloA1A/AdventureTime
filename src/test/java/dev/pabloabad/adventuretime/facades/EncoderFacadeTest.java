package dev.pabloabad.adventuretime.facades;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import dev.pabloabad.adventuretime.encryptations.Base64Encoder;

@ExtendWith(MockitoExtension.class)
public class EncoderFacadeTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Base64Encoder base64Encoder;

    @InjectMocks
    private EncoderFacade encoderFacade;

    @Test
    void testEncodeBcrypt() {
        String type = "bcrypt";
        String data = "password";
        String encodedData = "$2a$10$sdfihbn34356nkj3n5";

        when(passwordEncoder.encode(data)).thenReturn(encodedData);

        String result = encoderFacade.encode(type, data);

        verify(passwordEncoder).encode(data);
        assertThat(result, is(encodedData));
    }

    @Test
    void testEncodeBase64() {
        String type = "base64";
        String data = "test";
        String encodedData = "dGVzdA==";

        when(base64Encoder.encode(data)).thenReturn(encodedData);

        String result = encoderFacade.encode(type, data);

        verify(base64Encoder).encode(data);
        assertThat(result, is(encodedData));
    }

    @Test
    void testDecodeBase64() {
        String type = "base64";
        String encodedData = "dGVzdA==";
        String decodedData = "test";

        when(base64Encoder.decode(encodedData)).thenReturn(decodedData);

        String result = encoderFacade.decode(type, encodedData);

        verify(base64Encoder).decode(encodedData);
        assertThat(result, is(decodedData));
    }

    @Test
    void testEncodeInvalidType() {
        String type = "invalidType";
        String data = "test";

        String result = encoderFacade.encode(type, data);

        assertThat(result, is(""));
    }

    @Test
    void testDecodeInvalidType() {
        String type = "invalidType";
        String encodedData = "dGVzdA==";

        String result = encoderFacade.decode(type, encodedData);

        assertThat(result, is(""));
    }
}
