package com.mateo.urlshortener.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Base62TokenGenerator implements TokenGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_LENGTH = 12;
    private final Base62Encoder encoder;

    Base62TokenGenerator() {
        this.encoder = new Base62Encoder();
    }

    @Override
    public String generate() {
        byte[] randomBytes = getRandomBytes();
        return encoder.toBase62(randomBytes);
    }

    private byte[] getRandomBytes() {
        byte[] bytes = new byte[Base62TokenGenerator.TOKEN_LENGTH];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

}
